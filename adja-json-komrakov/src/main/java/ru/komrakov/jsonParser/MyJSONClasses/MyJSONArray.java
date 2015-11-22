package ru.komrakov.jsonParser.MyJSONClasses;

import ru.komrakov.jsonParser.JSONArrayClass;
import ru.nojs.json.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;

/**
 * Created by User on 08.11.2015.
 */
public class MyJSONArray implements JSONArray{

    private JSONArray jsonArray;

    public MyJSONArray(){
        jsonArray = new JSONArrayClass();
    }

    @Override
    public boolean equals(Object o){
        JSONArray foreignArray = (JSONArray)o;

        if (jsonArray.size() != foreignArray.size()){
            return false;
        }

        for (int i = 0; i < jsonArray.size(); i ++){
            if(!jsonArray.get(i).equals(foreignArray.get(i))){
                return false;
            }
        }
        return true;
    }

    @Override
    public void add(JSONElement element) {
        jsonArray.add(element);
    }

    @Override
    public void addAll(JSONArray array) {
        jsonArray.addAll(array);
    }

    @Override
    public boolean contains(JSONElement element) {
        return jsonArray.contains(element);
    }

    @Override
    public JSONElement get(int i) {
        return jsonArray.get(i);
    }

    @Override
    public int size() {
        return jsonArray.size();
    }

    @Override
    public Iterator<JSONElement> iterator() {
        return jsonArray.iterator();
    }

    @Override
    public BigDecimal getAsBigDecimal() {
        return jsonArray.getAsBigDecimal();
    }

    @Override
    public BigInteger getAsBigInteger() {
        return jsonArray.getAsBigInteger();
    }

    @Override
    public boolean getAsBoolean() {
        return jsonArray.getAsBoolean();
    }

    @Override
    public byte getAsByte() {
        return jsonArray.getAsByte();
    }

    @Override
    public char getAsCharacter() {
        return jsonArray.getAsCharacter();
    }

    @Override
    public double getAsDouble() {
        return jsonArray.getAsDouble();
    }

    @Override
    public float getAsFloat() {
        return jsonArray.getAsFloat();
    }

    @Override
    public int getAsInt() {
        return jsonArray.getAsInt();
    }

    @Override
    public JSONArray getAsJsonArray() {
        return jsonArray.getAsJsonArray();
    }

    @Override
    public JSONNull getAsJsonNull() {
        return jsonArray.getAsJsonNull();
    }

    @Override
    public JSONObject getAsJsonObject() {
        return jsonArray.getAsJsonObject();
    }

    @Override
    public JSONPrimitive getAsJsonPrimitive() {
        return jsonArray.getAsJsonPrimitive();
    }

    @Override
    public long getAsLong() {
        return jsonArray.getAsLong();
    }

    @Override
    public Number getAsNumber() {
        return jsonArray.getAsNumber();
    }

    @Override
    public short getAsShort() {
        return jsonArray.getAsShort();
    }

    @Override
    public String getAsString() {
        return jsonArray.getAsString();
    }

    @Override
    public boolean isJsonArray() {
        return jsonArray.isJsonArray();
    }

    @Override
    public boolean isJsonNull() {
        return jsonArray.isJsonNull();
    }

    @Override
    public boolean isJsonObject() {
        return jsonArray.isJsonObject();
    }

    @Override
    public boolean isJsonPrimitive() {
        return jsonArray.isJsonPrimitive();
    }
}
