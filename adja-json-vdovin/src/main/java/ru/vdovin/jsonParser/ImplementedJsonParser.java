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

        //throw new NotImplementedException();
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

       // current = this.reader.read();
        if (current == '"'){
            current = this.reader.read();
        }

        while (current != '"' && current != ',' && current != '}' && current != ']' && current != -1){
            primitive.append((char)current);
            current = this.reader.read();
        }

        String primitiveValue = primitive.toString();

        MyJSONPrimitive jp;

        if (checkInt(primitiveValue)){
            jp = new MyJSONPrimitive(Integer.parseInt(primitiveValue));
        }
        else if(checkDouble(primitiveValue)){
            jp = new MyJSONPrimitive(Double.parseDouble(primitiveValue));
        }
        else if(checkFloat(primitiveValue)){
            jp = new MyJSONPrimitive(Float.parseFloat(primitiveValue));
        }else {
            jp = new MyJSONPrimitive(primitive.toString());
        }

        return jp;
    }


    private MyJSONObject readJSONObject() throws IllegalArgumentException, IOException {

        MyJSONObject jo = new MyJSONObject();


        do {
            current = this.reader.read();
            StringBuilder property = new StringBuilder();
            current = this.reader.read();
            while (current != '"') {
                property.append((char) current);
                current = this.reader.read();
            }

            if (this.reader.read() != ':') {
                throw new IllegalArgumentException("Can't find ':' in object");
            }

            MyJSONElement value = readValue();

            jo.add(property.toString(), value);
            if (current == '"'){
                current = this.reader.read();
            }
        }while (current == ',');

        if (current != '}'){
            throw new IllegalArgumentException("Can't find '}'");
        }

// {"a":"1","b":"2"}
        return jo;
    }

    private void addValueJsonObject(){

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
}
