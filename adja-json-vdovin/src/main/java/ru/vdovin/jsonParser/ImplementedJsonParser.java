package ru.vdovin.jsonParser;

import ru.nojs.json.*;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ImplementedJsonParser implements StreamingJsonParser {
    List<Object> list = new ArrayList<>();
    public JSONObjectImpl jsonObject = new JSONObjectImpl();
    private JSONElement jsonElement;
    private static final Pattern pattern = Pattern.compile(",");
    public JSONElement parse(Reader r) {
        list.clear();
        try {
            int data = r.read();
            while (data != -1) {
                char str = (char) data;
                data = r.read();
                list.add(str);
            }
            r.close();
        } catch (Exception e) {
            throw new IllegalStateException("Error!!!!!!");
        }

        if (list.contains('{')&&list.contains(':')&&list.contains('}')){
            String key = "";
            String value = "";
            for (Object element : list){
                element = String.valueOf(element);
                if (element.equals("{")){
                    continue;
                }

                if (element.equals(":")) {
                    value+= element;
                    continue;
                }

                if ((element.equals("\"")||key.startsWith("\""))&&!value.startsWith(":")){
                    key +=element;
                    continue;
                } else if (!value.startsWith(":")&&isUncorrectElement(String.valueOf(element))) {
                    continue;
                }

                if (value.startsWith(":")&&(element.equals("[")||value.contains("["))&&!value.endsWith("]")){
                    value+=element;
                    continue;
                } else if (value.startsWith(":")&&!(element.equals(",")||element.equals("}"))){
                    if (!value.substring(1).startsWith("\"")||value.endsWith("\"")){
                        if (isUncorrectElement(String.valueOf(element))){
                            continue;
                        }
                    }
                    value+=element;
                    continue;
                }

                jsonElement = chooseJson(value.substring(1));
                jsonObject.add(key.substring(1, key.length()-1), jsonElement);
                if (element.equals(",")){
                    key ="";
                    value ="";
                    continue;
                }
            }
            return jsonObject;
        }else if (list.contains('[')&&list.contains(']')){
            String arr= "";
            for (Object obj : list){
                if (isUncorrectElement(String.valueOf(obj))){
                    continue;
                }
                arr += String.valueOf(obj);
            }
            return parseJsonArray(arr);
        } else {
            String primitive ="";
            for (Object obj : list){
                if (!(obj.equals("\"")||primitive.startsWith("\""))) {
                    if (isUncorrectElement(String.valueOf(obj))) {
                        continue;
                    }
                }
                primitive += String.valueOf(obj);
            }
            primitive = primitive.replace("\\", "");
            return parseJsonPrimitive(primitive);
        }
    }

    private JSONElement chooseJson(String value){
        if (value.contains("[")&&value.contains("]")){
            return parseJsonArray(value);
        } else {
            return parseJsonPrimitive(value);
        }
    }

    private JSONElement parseJsonArray(String value){
        JSONArrayImpl JSONArrayImpl = new JSONArrayImpl();
        String[] arrays = pattern.split(value.substring(1, value.length()-1));
        for (String el : arrays){
            JSONArrayImpl.add(parseJsonPrimitive(el));
        }
        return JSONArrayImpl;
    }

    private JSONElement parseJsonPrimitive(String value){
        if (value.matches("[-]?[0-9]+")){
            jsonElement = new JSONPrimitiveImpl(Integer.parseInt(value));
        } else if (isQuote(value)) {
            jsonElement = new JSONPrimitiveImpl(value.substring(1, value.length() - 1));
        } else if (isNull(value)){
            jsonElement = JSONNullImpl.getInstance();
        } else if (isBoolean(value)) {
            jsonElement = new JSONPrimitiveImpl(Boolean.parseBoolean(value));
        }
        return jsonElement;
    }

    private boolean isBoolean(String value){
        if (value.equals("true")){
            return true;
        } else if (value.equals("false")){
            return false;
        }
        throw new IllegalArgumentException("It's not primitive type.");
    }

    private boolean isQuote(String value){
        return value.startsWith("\"")&&value.endsWith("\"");
    }

    private boolean isNull (String value){
        return value.equals("null");
    }
    private boolean isUncorrectElement(String value){
        switch (value){
            case "\t":
            case "\n":
            case "\r":
            case "\\":
            case " ":
                return true;
            default:
                return false;
        }
    }
}