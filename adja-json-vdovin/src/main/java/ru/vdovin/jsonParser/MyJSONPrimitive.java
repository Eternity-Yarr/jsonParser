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
        value =  o;
    }

    public boolean isBoolean() {
        return value instanceof Boolean;
    }

    public boolean isNumber() {
        return value instanceof Number;
    }

    public boolean isString() {
        return value instanceof String;
    }

    public String toString(){
        return value.toString();
    }

    @Override
    public boolean getAsBoolean(){
        if (isBoolean()){
            return (boolean)value;
        }
        throw new IllegalStateException("this is no a boolean");
    }

    @Override
    public String getAsString(){
        if (isString()) {
            return value.toString();
        }
        throw new IllegalStateException("This is not a string");
    }

    @Override
    public int getAsInt(){
        if (isNumber()){
           return (int)value;
        }
        throw new IllegalStateException("This is not a int");
    }

    public Number getAsNumber(){
        if (isNumber()){
            return (Number)value;
        }
        throw new IllegalStateException("This is not number");
    }

    @Override
    public float getAsFloat(){
        if (isNumber()){
            return getAsNumber().floatValue();
        }
        throw new IllegalStateException("This is not a float");
    }

    @Override
    public double getAsDouble(){
        if (isNumber()){
            return getAsNumber().doubleValue();
        }
        throw new IllegalStateException("This is not a double");
    }

}
