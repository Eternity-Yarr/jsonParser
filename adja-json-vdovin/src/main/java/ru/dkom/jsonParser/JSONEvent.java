package ru.dkom.jsonParser;

import java.util.HashMap;
import java.util.Map;

public class JSONEvent {
    public final static String JSON_OBJECT_START = "JSON_OBJECT_START";
    public final static String JSON_OBJECT_END = "JSON_OBJECT_END";
    public final static String JSON_OBJECT_SEPARATOR = "JSON_OBJECT_SEPARATOR";
    public final static String JSON_ARRAY_START = "JSON_ARRAY_START";
    public final static String JSON_ARRAY_END = "JSON_ARRAY_END";
    public final static String JSON_PRIMITIVE_VALUE_START = "JSON_PRIMITIVE_VALUE_START";
    public final static String QUOTES_DETECTED = "QUOTES_DETECTED";

    public final static String READING_VALUE = "READING_VALUE";
    public final static String NOTHING_HAPPENS = "EMPTY_SYMBOL";


    private final static Character[] JSON_MEANINGLESS_SYMBOLS = {' ','\r','\n','\t'};

    private static Map<Character,String> CharEventMap = new HashMap<Character, String>();
    {
        CharEventMap.put('{',JSON_OBJECT_START);
        CharEventMap.put('}',JSON_OBJECT_END);
        CharEventMap.put(',',JSON_OBJECT_SEPARATOR);
        CharEventMap.put('[',JSON_ARRAY_START);
        CharEventMap.put(']',JSON_ARRAY_END);
        CharEventMap.put(':',JSON_PRIMITIVE_VALUE_START);
        CharEventMap.put('"',QUOTES_DETECTED);
    }

    private String value;


    public JSONEvent(){
        value = null;
    }

    public String checkEvent(Character symbol){
        String event = NOTHING_HAPPENS;
        if(!isMeaningless(symbol)){
            event = READING_VALUE;
            value = Character.toString(symbol);
            if (CharEventMap.get(symbol) != null){
                event = CharEventMap.get(symbol);
            }
        }
        return event;
    }

    public String getValue(){
        return value;
    }


    private Boolean isMeaningless(Character symbol){
        Boolean checkResult = false;
        for (Character c:JSON_MEANINGLESS_SYMBOLS){
            if(c.equals(symbol)){
                checkResult = true;
                break;
            }
        }
        return checkResult;
    }

}
