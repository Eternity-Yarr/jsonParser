package ru.komrakov.jsonParser;

import ru.nojs.json.JSONArray;
import ru.nojs.json.JSONNull;
import ru.nojs.json.JSONObject;
import ru.nojs.json.JSONPrimitive;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONPrimitiveClass implements JSONPrimitive{

    private final static int QUOTES_CODE = 34;


    protected Object value;

    public JSONPrimitiveClass(Object o){
        value = o;
        //FIXME: String::equalsIgnoreCase()
        if (((String)o).toLowerCase().equals("null")){
            value = null;
        }

        if (looksLikeBoolean((String)o)){
            value = o;
            Boolean b = this.getAsBoolean();
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

        String probe = (String) value;

        Character firstChar = probe.charAt(0);
        Character lastChar = probe.charAt(probe.length() - 1);

        //FIXME: DRY: это уже было где-то
        if ((firstChar.equals((char) 34)) && (lastChar.equals((char) 34))) {
            //FIXME: Лучше добавлять сообщения исключениям, проще жить. Что это за IllegalState?
            throw new IllegalStateException();
        }

        if (looksLikeBoolean(probe)) {
            if (probe.equals("true")) {
                return true;
            }
            if (probe.equals("false")) {
                return false;
            }
            throw new IllegalArgumentException();
        }
        throw new IllegalStateException();

    }

    @Override
    public byte getAsByte() {
        return Byte.parseByte((String)value);
    }

    @Override
    public char getAsCharacter() {
        return (Character)value;
    }

    @Override
    public double getAsDouble() {
        return Double.parseDouble((String) value);
    }

    @Override
    public float getAsFloat() {
        return Float.parseFloat((String) value);
    }

    @Override
    public int getAsInt() {
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
        return Long.parseLong((String)value);
    }

    @Override
    public Number getAsNumber() {
        return (Number)value;
    }

    @Override
    public short getAsShort() {
        return Short.parseShort((String)value);
    }

    @Override
    public String getAsString() {

        String probe = (String)value;
        Character firstChar = probe.charAt(0);
        Character lastChar = probe.charAt(probe.length()-1);

        //FIXME: ^_^
        if ((firstChar.equals((char)QUOTES_CODE))&&(!lastChar.equals((char)QUOTES_CODE))){
            throw new IllegalArgumentException();
        }

        if ((!firstChar.equals((char)QUOTES_CODE))&&(lastChar.equals((char)QUOTES_CODE))){
            throw new IllegalArgumentException();
        }

        return getRidOfQoutes((String)value);
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

    private Boolean looksLikeBoolean(String string) {
        String regex = ".*([fF][aA][lL][sS][eE]).*|.*([tT][rR][uU][eE]).*";
        Matcher m = Pattern.compile(regex).matcher(string);
        if (m.matches()){
            return true;
        }
        return false;
    }

    private String getRidOfQoutes(String value){
        Character firstChar = value.charAt(0);
        Character lastChar = value.charAt(value.length() - 1);
        if ((firstChar.equals((char)QUOTES_CODE))&&(lastChar.equals((char)QUOTES_CODE))){
            value = value.substring(1, value.length()-1);
        }
        return value;
    }

}
