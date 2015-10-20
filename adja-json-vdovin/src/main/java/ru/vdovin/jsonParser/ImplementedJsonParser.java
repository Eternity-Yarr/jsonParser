package ru.vdovin.jsonParser;

import ru.nojs.json.JSONElement;
import ru.nojs.json.JSONPrimitive;
import ru.nojs.json.StreamingJsonParser;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.io.Reader;

public class ImplementedJsonParser implements StreamingJsonParser {

    private final char START_OBJECT = '{';
    private final char START_ARRAY = '[';

    private Reader reader;
    private int current;

    public JSONElement parse(Reader r) throws IllegalArgumentException, IOException {
        this.reader = r;
        JSONElement jo = read();
        return  jo;
    }

    private MyJSONElement read() throws IllegalArgumentException, IOException {
        current = this.reader.read();
        switch (current) {
            case START_OBJECT: {return readJSONObject();}
            default : {return readJSONValue();}
        }
    }

    private MyJSONElement readJSONValue() throws IllegalArgumentException, IOException {
        StringBuilder primitive = new StringBuilder();
        MyJSONElement jp;

        while (!isCharOfEndValue()){
            primitive.append((char)current);
            current = this.reader.read();
        }

        String primitiveValue = primitive.toString();

        if (isInteger(primitiveValue)){
            jp = new MyJSONPrimitive(Integer.parseInt(primitiveValue));
        }
        else if (isDouble(primitiveValue)){
            jp = new MyJSONPrimitive(Double.parseDouble(primitiveValue));
        }
        else if (isFloat(primitiveValue)){
            jp = new MyJSONPrimitive(Float.parseFloat(primitiveValue));
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


    private MyJSONObject readJSONObject() throws IllegalArgumentException, IOException {

        MyJSONObject jo = new MyJSONObject();

        do {
            StringBuilder property = new StringBuilder();
             current = this.reader.read();
            if (current != '"'){
                throw new IllegalArgumentException("Error syntax");
            }else {
                current = this.reader.read();
            }

            while (current != '"') {
                property.append((char) current);
                current = this.reader.read();
            }

            current = this.reader.read();
            if (current != ':') {
                throw new IllegalArgumentException("Can't find ':' in object");
            }

            MyJSONElement value = read();

            jo.add(property.toString(), value);
            if (current == '"' ){
                current = this.reader.read();
            }
        } while (current == ',');

        if (current != '}'){
            throw new IllegalArgumentException("Can't find '}'");
        }

        return jo;
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
        if (value.equals("null")){
            return true;
        } else {
            return false;
        }
    }

    private boolean isCharOfEndValue(){
        if (current == ',' || current == '}' || current == ']' || current == -1){
            return true;
        }else {
            return false;
        }
    }
}
