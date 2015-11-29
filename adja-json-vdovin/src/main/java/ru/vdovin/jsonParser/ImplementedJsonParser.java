package ru.vdovin.jsonParser;

import ru.nojs.json.*;

import java.io.Reader;
import java.util.Arrays;
import java.util.List;

public class ImplementedJsonParser implements StreamingJsonParser {
    private static final List<Character> ELEMENT_REMOVE = Arrays.asList('\n', '\r', '\t', ' ');
    public JSONObjectImpl jsonObject = new JSONObjectImpl();
    private JSONElement jsonElement;

    public JSONElement parse(Reader r) {
        JsonParseReader jpr = new JsonParseReader(r);
        return chooseJson(jpr);
    }

    public JSONElement chooseJson(JsonParseReader jpr){
        jpr.nextElement();
        checkIsRemove(jpr);
        switch (jpr.getElement()){
            case '{': return parseObject(jpr);
            case '[': return parseArray(jpr);
            case '"': return parseString(jpr);
            default:
                return getPrimitive(jpr);}
    }

    public JSONElement parseString(JsonParseReader jpr){
        StringBuffer string = new StringBuffer();
        while (!isBlockedSimbols(jpr.getElement())){
            //FIXME: в яве так нельзя,  в яве строки иммутабельн и += создает каждый
            // раз новый объект, это очень медленно и много мусора. Если хочешь собирать по одной
            // букве используй StringBuilder или StringBuffer
            string.append((char) jpr.getElement());
            jpr.nextElement();
        }
        String s = string.toString();
        s = s.replace("\\", "");
        s = removeUncorrectEl(s);
        if (isQuote(s)){
            return new JSONPrimitiveImpl(s.substring(1, s.length() -1));
        }
        throw new IllegalArgumentException("Parse Error!!!");
    }

    public JSONElement parseObject(JsonParseReader jpr){
        String key = "";
        do{
            jpr.nextElement();
            checkIsRemove(jpr);
            if (jpr.getElement() == '"'){
                jpr.nextElement();
            } else {
                throw new IllegalArgumentException("Error syntax in object");
            }

            while (jpr.getElement()!='"'){
                key+=(char)jpr.getElement();
                jpr.nextElement();
            }

            jpr.nextElement();
            checkIsRemove(jpr);
            jsonElement = chooseJson(jpr);

            jsonObject.add(key, jsonElement);
            key= "";
            if (jpr.getElement()=='"') {
                jpr.nextElement();
            }
            checkIsRemove(jpr);
        } while (jpr.getElement() == ',');
        jpr.nextElement();
        return jsonObject;
    }

    public JSONElement parseArray(JsonParseReader jpr){
        JSONArrayImpl JSONArrayImpl = new JSONArrayImpl();
        do {
            JSONArrayImpl.add(chooseJson(jpr));
        } while (jpr.getElement() == ',');
        jpr.nextElement();
        return JSONArrayImpl;
    }

    public JSONElement getPrimitive(JsonParseReader jpr){
        StringBuffer string = new StringBuffer();
        while (!isBlockedSimbols(jpr.getElement())){
            string.append((char) jpr.getElement());
            jpr.nextElement();
            checkIsRemove(jpr);
        }
        String el = string.toString();
        if (el.matches("[-]?[0-9]+")){
            jsonElement = new JSONPrimitiveImpl(el);
        } else if (isDouble(el)){
            jsonElement = new JSONPrimitiveImpl(Double.parseDouble(el));
        }else if (isQuote(el)){
            jsonElement = new JSONPrimitiveImpl(el);
        } else if (isNull(el)){
            jsonElement = new JSONNullImpl();
        } else if (isBoolean(el)){
            jsonElement = new JSONPrimitiveImpl(Boolean.parseBoolean(el));
        }
        return jsonElement;
    }

    private boolean isBlockedSimbols(int rd){
        return rd == '}' || rd ==']' || rd == -1 || rd == ',';
    }

    private String removeUncorrectEl(String value){
        if (value.endsWith("\n") || value.endsWith("\t") || value.endsWith("\r")){
            return removeUncorrectEl(value.substring(0, value.length() - 1));
        }
        return value;
    }

    private boolean isQuote(String value){
        return value.startsWith("\"")&&value.endsWith("\"");
    }

    private boolean isNull(String value){
        return value.equals("null");
    }

    private boolean isBoolean(String value){
        if (value.equals("true")||value.equals("false")){
            return true;
        } else {
            throw new IllegalArgumentException("Bad syntax!!");
        }
    }

    private boolean isDouble(String value){
        try{
            Double.parseDouble(value);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public void checkIsRemove(JsonParseReader jpr){
        while (ELEMENT_REMOVE.contains((char)(jpr.getElement()))){
            jpr.nextElement();
        }
    }
}
