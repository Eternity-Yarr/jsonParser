package ru.vdovin.jsonParser;

import ru.nojs.json.JSONPrimitive;

public class MyJSONPrimitive extends MyJSONElement implements JSONPrimitive{

    private Object value;

    public MyJSONPrimitive(String str){
        setValue(str);
    }

    public MyJSONPrimitive(Boolean bool) {
        setValue(bool);
    }

    public MyJSONPrimitive(Character c) {
        setValue(c);
    }

    MyJSONPrimitive(Object obj){
        setValue(obj);
    }

    void setValue(Object o){
        this.value =  o;
    }

    public boolean isBoolean() {
        return this.value instanceof Boolean;
    }

    public boolean isNumber() {
        return this.value instanceof Number;
    }

    public boolean isString() {
        return this.value instanceof String;
    }

    public String toString(){
        return this.value.toString();
    }

    @Override
    public boolean getAsBoolean(){
        if (isBoolean()){
            return (boolean)this.value;
        }
        throw new IllegalStateException("this is no a boolean");
    }

    @Override
    public String getAsString(){
        if (isString()) {
            return this.value.toString();
        }
        throw new IllegalStateException("This is not a string");
    }


    @Override
    public int getAsInt(){
        if (isNumber()){
           return (int)this.value;
        }
        throw new IllegalStateException("This is not a int");
    }

    @Override
    public float getAsFloat(){
        if (isNumber()){
            return (float)this.value;
        }
        throw new IllegalStateException("This is not a float");
    }

    @Override
    public double getAsDouble(){
        if (isNumber()){
            return (double)this.value;
        }
        throw new IllegalStateException("This is not a double");
    }








}
