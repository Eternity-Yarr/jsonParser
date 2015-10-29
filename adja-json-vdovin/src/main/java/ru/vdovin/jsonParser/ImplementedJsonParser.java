package ru.vdovin.jsonParser;

import ru.nojs.json.JSONElement;
import ru.nojs.json.StreamingJsonParser;

import java.io.IOException;
import java.io.Reader;

public class ImplementedJsonParser implements StreamingJsonParser {

    private final static char START_OBJECT = '{';
    private final static char START_ARRAY = '[';
    private final static char START_STRING = '"';

    public JSONElement parse(Reader r) {

        JSONReader jr = new JSONReader(r);
        try {
            return read(jr);
        } catch (IOException e) {
            throw new IllegalArgumentException("No can parse", e);
        }
    }

    private MyJSONElement read(JSONReader jr) throws IOException {
        jr.read();
        jr.readInsignificantSymbols();
        switch (jr.getCurrent()) {
            case START_OBJECT : { return readJSONObject(jr); }
            case START_ARRAY : { return  readJSONArray(jr); }
            case START_STRING : { return readJSONString(jr); }
            default : {return readJSONValue(jr);}
        }
    }
    private MyJSONElement readJSONString(JSONReader jr) {
        StringBuilder jsonString = new StringBuilder();

        while (!isEndMark(jr.getCurrent())) {
            jsonString.append((char) jr.getCurrent());
            jr.read();
        }



        String stringValue = jsonString.toString().replace("\\", "");
        if (enclosedInQuotes(stringValue)) {
            return new MyJSONPrimitive(stringValue.substring(1, stringValue.length() - 1));
        } else {
            throw new IllegalArgumentException("Error Syntax");
        }

    }

    private MyJSONElement readJSONValue(JSONReader jr) throws IllegalArgumentException, IOException {
        StringBuilder primitive = new StringBuilder();
        MyJSONElement jp;

        while (!isEndMark(jr.getCurrent())){
            primitive.append((char)jr.getCurrent());
            jr.read();
            jr.readInsignificantSymbols();
        }

        String primitiveValue = primitive.toString();

        if (isInteger(primitiveValue)){
            jp = new MyJSONPrimitive(Integer.parseInt(primitiveValue));
        }
        else if (isFloat(primitiveValue)){
            jp = new MyJSONPrimitive(Float.parseFloat(primitiveValue));
        }
        else if (isDouble(primitiveValue)){
            jp = new MyJSONPrimitive(Double.parseDouble(primitiveValue));
        }
        else if (enclosedInQuotes(primitiveValue)) {
            jp = new MyJSONPrimitive(primitiveValue.substring(1, primitiveValue.length() - 1));
        }
        else if (isNull(primitiveValue)) {
            jp = MyJSONNull.INSTANCE;
        }
        else{
            jp = new MyJSONPrimitive(parseBooleanValue(primitiveValue));
        }
        return jp;
    }

    private MyJSONObject readJSONObject(JSONReader jr) throws IllegalArgumentException, IOException {

        MyJSONObject jo = new MyJSONObject();

        do {
            StringBuilder property = new StringBuilder();
            jr.read();
            if (jr.getCurrent() != '"'){
                throw new IllegalArgumentException("Error syntax");
            }else {
                jr.read();
            }

            while (jr.getCurrent() != '"') {
                property.append((char)jr.getCurrent());
                jr.read();
            }

            jr.read();
            if (jr.getCurrent() != ':') {
                throw new IllegalArgumentException("Can't find ':' in object");
            }

            MyJSONElement value = read(jr);

            jo.add(property.toString(), value);
            if (jr.getCurrent() == '"' ){
                jr.read();
            }
        } while (jr.getCurrent() == ',');

        if (jr.getCurrent() != '}'){
            throw new IllegalArgumentException("Can't find '}'");
        }
        jr.read();

        return jo;
    }

    private MyJSONArray readJSONArray(JSONReader jr) throws IOException {
        MyJSONArray ja = new MyJSONArray();
        do {
            ja.add(read(jr));
        } while (jr.getCurrent() == ',');
        jr.read();
        return  ja;
    }

    private boolean parseBooleanValue(String value){
        if (value.equals("true")){
            return true;
        }
        else if (value.equals("false")){
            return false;
        }
        else {
            throw new IllegalArgumentException("Error Syntax");
        }
    }

    private boolean isInteger(String value){
        try {
            Integer.parseInt(value);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    private boolean isFloat(String value){
        try {
            Float.parseFloat(value);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    private boolean isDouble(String value){
        try {
            Double.parseDouble(value);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    private boolean  enclosedInQuotes(String value){
        return value.startsWith("\"") && value.endsWith("\"");
    }

    private boolean isNull(String value){
       return value.equals("null");
    }

    private boolean isEndMark(int ch){
        return ch == ',' || ch == '}' || ch == ']' || ch == -1;
    }
}
