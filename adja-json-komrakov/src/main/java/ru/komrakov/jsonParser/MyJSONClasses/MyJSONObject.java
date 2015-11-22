package ru.komrakov.jsonParser.MyJSONClasses;

import ru.komrakov.jsonParser.JSONObjectClass;
import ru.nojs.json.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.Set;

/**
 * Created by User on 08.11.2015.
 */
public class MyJSONObject implements JSONObject{

    private JSONObject jsonObject;

    public MyJSONObject(){
        jsonObject = new JSONObjectClass();
    }

    @Override
    public boolean equals(Object o){
        /*
        JSONObject foreignObject = (JSONObjectClass)o;
        if (jsonObject.isJsonPrimitive()&&(foreignObject.isJsonPrimitive())){

        }*/
        return false;
    }

    @Override
    public void add(String property, JSONElement value) {
        jsonObject.add(property, value);
    }

    @Override
    public void addProperty(String property, Boolean value) {
        jsonObject.addProperty(property, value);
    }

    @Override
    public void addProperty(String property, Number value) {
        jsonObject.addProperty(property, value);
    }

    @Override
    public void addProperty(String property, String value) {
        jsonObject.addProperty(property, value);
    }

    @Override
    public Set<Map.Entry<String, JSONElement>> entrySet() {
        jsonObject.entrySet();
        return null;
    }

    @Override
    public JSONElement get(String memberName) {
        return jsonObject.get(memberName);
    }

    @Override
    public JSONArray getAsJsonArray(String memberName) {
        return jsonObject.getAsJsonArray(memberName);
    }

    @Override
    public JSONObject getAsJsonObject(String memberName) {
        return jsonObject.getAsJsonObject(memberName);
    }

    @Override
    public JSONPrimitive getAsJsonPrimitive(String memberName) {
        return jsonObject.getAsJsonPrimitive(memberName);
    }

    @Override
    public boolean has(String memberName) {
        return jsonObject.has(memberName);
    }

    @Override
    public JSONElement remove(String property) {
        return jsonObject.remove(property);
    }

    @Override
    public BigDecimal getAsBigDecimal() {
        return jsonObject.getAsBigDecimal();
    }

    @Override
    public BigInteger getAsBigInteger() {
        return jsonObject.getAsBigInteger();
    }

    @Override
    public boolean getAsBoolean() {
        return jsonObject.getAsBoolean();
    }

    @Override
    public byte getAsByte() {
        return jsonObject.getAsByte();
    }

    @Override
    public char getAsCharacter() {
        return jsonObject.getAsCharacter();
    }

    @Override
    public double getAsDouble() {
        return jsonObject.getAsDouble();
    }

    @Override
    public float getAsFloat() {
        return jsonObject.getAsFloat();
    }

    @Override
    public int getAsInt() {
        return jsonObject.getAsInt();
    }

    @Override
    public JSONArray getAsJsonArray() {
        return jsonObject.getAsJsonArray();
    }

    @Override
    public JSONNull getAsJsonNull() {
        return jsonObject.getAsJsonNull();
    }

    @Override
    public JSONObject getAsJsonObject() {
        return jsonObject.getAsJsonObject();
    }

    @Override
    public JSONPrimitive getAsJsonPrimitive() {
        return jsonObject.getAsJsonPrimitive();
    }

    @Override
    public long getAsLong() {
        return jsonObject.getAsLong();
    }

    @Override
    public Number getAsNumber() {
        return jsonObject.getAsNumber();
    }

    @Override
    public short getAsShort() {
        return jsonObject.getAsShort();
    }

    @Override
    public String getAsString() {
        return jsonObject.getAsString();
    }

    @Override
    public boolean isJsonArray() {
        return jsonObject.isJsonArray();
    }

    @Override
    public boolean isJsonNull() {
        return jsonObject.isJsonNull();
    }

    @Override
    public boolean isJsonObject() {
        return jsonObject.isJsonObject();
    }

    @Override
    public boolean isJsonPrimitive() {
        return jsonObject.isJsonPrimitive();
    }
}
