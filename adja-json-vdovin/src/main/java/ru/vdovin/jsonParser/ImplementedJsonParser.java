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
            case 'T':
            case 't':
            case 'F':
            case 'f': {return readJSONBoolean();}
            default : {return readJSONPrimitive();}
        }

    }

    private MyJSONPrimitive readJSONPrimitive() throws IllegalArgumentException, IOException {
        StringBuilder primitive = new StringBuilder();
        MyJSONPrimitive jp;

        if (current == '"'){
            current = this.reader.read();
        }

        //while (current != '"' && current != ',' && current != '}' && current != ']' && current != -1){
        while (!isEndPrimiteveValue()){
            primitive.append((char)current);
            current = this.reader.read();
        }

        String primitiveValue = primitive.toString();

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
        }while (current == ',');

        if (current != '}'){
            throw new IllegalArgumentException("Can't find '}'");
        }

// {"a":"1","b":"2"}
        return jo;
    }

    private MyJSONPrimitive readJSONBoolean(){
        MyJSONPrimitive jp = new MyJSONPrimitive(readBooleanValue());
        return jp;
    }


    private boolean readBooleanValue(){
        StringBuilder booleanValue = new StringBuilder();

        while (!isCharOfEndValue()){
            booleanValue.append((char)current);
            try {
                current = reader.read();
            }
            catch (IOException e){
                throw new IllegalArgumentException("Error Syntax1");
            }
        }

        if (booleanValue.toString().toUpperCase().equals("TRUE")){
            return true;
        }
        else if (booleanValue.toString().toUpperCase().equals("FALSE")){
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

    private boolean isEndPrimiteveValue(){
        if (current == '"' || isCharOfEndValue()){
            return true;
        }else {
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
