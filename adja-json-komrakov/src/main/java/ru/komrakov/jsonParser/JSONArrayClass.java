package ru.komrakov.jsonParser;

import ru.nojs.json.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JSONArrayClass implements JSONArray{

    private List<JSONElement> array;

    public JSONArrayClass(){
        array = new ArrayList<>();
    }

    @Override
    public void add(JSONElement element) {
        //this.array.add((JSONElementClass) element);
        this.array.add(element);
    }

    @Override
    public void addAll(JSONArray array) {
        for (JSONElement element:array){
            this.array.add(element);
        }
    }

    @Override
    public boolean contains(JSONElement element) {
        return array.contains(element);
    }

    @Override
    public JSONElement get(int i) {
        if(i < 0 || i > array.size()){
            //FIXME: null? ну нет, нечего промахиваться.. OutOfBoundsException. Причем его уже кидает за тебя List
            return null;
        }
        return array.get(i);
    }

    @Override
    public int size() {
        return array.size();
    }

    @Override
    public Iterator<JSONElement> iterator() {
        return array.iterator();
    }

    @Override
    public BigDecimal getAsBigDecimal() {
        return null;
    }

    @Override
    public BigInteger getAsBigInteger() {
        return null;
    }

    @Override
    public boolean getAsBoolean() {
        return false;
    }

    @Override
    public byte getAsByte() {
        return 0;
    }

    @Override
    public char getAsCharacter() {
        return 0;
    }

    @Override
    public double getAsDouble() {
        return 0;
    }

    @Override
    public float getAsFloat() {
        return 0;
    }

    @Override
    public int getAsInt() {
        return 1;
    }

    @Override
    public JSONArray getAsJsonArray() {
        return this;
    }

    @Override
    public JSONNull getAsJsonNull() {
        return null;
    }

    @Override
    public JSONObject getAsJsonObject() {
        return null;
    }

    @Override
    public JSONPrimitive getAsJsonPrimitive() {
        return null;
    }

    @Override
    public long getAsLong() {
        return 0;
    }

    @Override
    public Number getAsNumber() {
        return null;
    }

    @Override
    public short getAsShort() {
        return 0;
    }

    @Override
    public String getAsString() {
        return null;
    }

    @Override
    public boolean isJsonArray() {
        return true;
    }

    @Override
    public boolean isJsonNull() {
        return false;
    }

    @Override
    public boolean isJsonObject() {
        return false;
    }

    @Override
    public boolean isJsonPrimitive() {
        return false;
    }
}
