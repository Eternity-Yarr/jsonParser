package ru.vdovin.jsonParser;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import ru.nojs.json.JSONPrimitive;

import java.math.BigDecimal;

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
        return value.toString();
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
        try{
            return getAsNumber().floatValue();
        }
        catch (Exception e) {
            throw new IllegalStateException("This is not a float");
        }
    }

    @Override
    public double getAsDouble(){
        try {
            return getAsNumber().doubleValue();
        }
        catch (Exception e) {
            throw new IllegalStateException("This is not a double");
        }
    }

    public Object getAsObject(){
        return value;
    }

    @Override
    public long getAsLong(){
        if (isNumber()) return getAsNumber().longValue();
        throw new IllegalStateException("this is no a long");
    }

    @Override
    public BigDecimal getAsBigDecimal() {
        try {
            return new BigDecimal(getAsString());
        }
        catch (Exception e) {
            throw  new IllegalStateException("Can't parse BigDecimal");
        }
    }

    @Override
    public boolean equals(Object obj) {
        return new EqualsBuilder().reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(this)
                .toHashCode();
    }

}
