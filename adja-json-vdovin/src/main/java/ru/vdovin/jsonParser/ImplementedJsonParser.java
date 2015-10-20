package ru.vdovin.jsonParser;

import ru.nojs.json.JSONElement;
import ru.nojs.json.JSONPrimitive;
import ru.nojs.json.StreamingJsonParser;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.io.Reader;

public class ImplementedJsonParser implements StreamingJsonParser {

    private Reader reader;
    private int current;

    public JSONElement parse(Reader r) throws IllegalArgumentException, IOException {
        this.reader = r;
        JSONElement jo = readValue();
        return  jo;
    }

    private MyJSONElement readValue() throws IllegalArgumentException, IOException {
        current = this.reader.read();
        switch (current) {
            case '{': {return readJSONObject();}
            default : {return readJSONPrimitive();}
        }

    }

    private MyJSONPrimitive readJSONPrimitive() throws IllegalArgumentException, IOException {
        StringBuilder primitive = new StringBuilder();
        MyJSONPrimitive jp;

        while (!isCharOfEndValue()){
            primitive.append((char)current);
            current = this.reader.read();
        }

        String primitiveValue = primitive.toString();

        if (checkInt(primitiveValue)){
            jp = new MyJSONPrimitive(Integer.parseInt(primitiveValue));
        }
        else if (checkDouble(primitiveValue)){
            jp = new MyJSONPrimitive(Double.parseDouble(primitiveValue));
        }
        else if (checkFloat(primitiveValue)){
            jp = new MyJSONPrimitive(Float.parseFloat(primitiveValue));
        }else if (checkString(primitiveValue)) {
            jp = new MyJSONPrimitive(primitiveValue.substring(1, primitiveValue.length() - 1));
        }else{
              jp = new MyJSONPrimitive(readBooleanValue(primitiveValue));
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

            MyJSONElement value = readValue();

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

    private boolean readBooleanValue(String s){
        if (s.toUpperCase().equals("TRUE")){
            return true;
        }
        else if (s.toUpperCase().equals("FALSE")){
            return false;
        }
        else {
            throw new IllegalArgumentException("Error Syntax");
        }
    }

    private boolean checkInt(String s){
        try {
            Integer.parseInt(s);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    private boolean checkFloat(String s){
        try {
            Float.parseFloat(s);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    private boolean checkDouble(String s){
        try {
            Double.parseDouble(s);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    private boolean checkString(String s){
        if (s.startsWith("\"") && s.endsWith("\"")) {
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
