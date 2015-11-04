package ru.dkom.jsonParser;

import ru.nojs.json.JSONArray;
import ru.nojs.json.JSONNull;
import ru.nojs.json.JSONObject;
import ru.nojs.json.JSONPrimitive;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.DoubleSummaryStatistics;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONPrimitiveClass implements JSONPrimitive{


    private Object value;

    public JSONPrimitiveClass(Object o){
        value = o;
        if (((String)o).toLowerCase().equals(null)){
            value = null;
        }
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
        String probe = (String)value;
        if (probe.toUpperCase().equals("TRUE")){
            return true;
        }
        if (probe.toUpperCase().equals("FALSE")){
            return false;
        }
        throw new IllegalStateException();
        //return Boolean.valueOf(value.toString());
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
        return Double.parseDouble((String)value);
    }

    @Override
    public float getAsFloat() {
        return 0;
    }

    @Override
    public int getAsInt() {
        // return (Integer)value;
        return Integer.parseInt((String)value);
    }

    @Override
    public JSONArray getAsJsonArray() {
        return null;
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
        return this;
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
        // return (String)value;

        String probe = (String)value;
        Character firstChar = probe.charAt(0);
        Character lastChar = probe.charAt(probe.length()-1);

        if ((firstChar.equals((char)34))&&(lastChar.equals((char)34))){
            //remove first and last symbol
            char[] swap = new char[probe.length() - 2];
            for (int i = 1; i < probe.length() - 1; i ++){
                swap[i-1] = probe.charAt(i);
            }
            probe = new String(swap);
            return probe;
        }

        if((firstChar.equals((char)34))||(lastChar.equals((char)34))){
            throw new IllegalArgumentException();
        }

        return value.toString();
    }

    @Override
    public boolean isJsonArray() {
        return false;
    }

    @Override
    public boolean isJsonNull() {
        if (value==null){
            return true;
        }
        if (((String)value).toLowerCase().equals("null")){
            return true;
        }
        return false;
    }

    @Override
    public boolean isJsonObject() {
        return false;
    }

    @Override
    public boolean isJsonPrimitive() {
        return true;
    }
}
