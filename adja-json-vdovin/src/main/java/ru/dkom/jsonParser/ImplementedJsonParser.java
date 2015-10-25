package ru.dkom.jsonParser;

import ru.nojs.json.JSONArray;
import ru.nojs.json.JSONElement;
import ru.nojs.json.StreamingJsonParser;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImplementedJsonParser implements StreamingJsonParser {
    private final static String WAITING_FOR_INPUT = "WAITING_FOR_INPUT";
    private final static String COMPILING_VALUE = "COMPILING_VALUE";
    private final static String VALUE_HAS_BEEN_READ = "VALUE_HAS_BEEN_READ";
    private final static String READING_ARRAY = "READING_ARRAY";
    private final static String ARRAY_HAS_BEEN_READ = "ARRAY_HAS_BEEN_READ";
    private final static String END_OF_STREAM = "END_OF_STREAM";

    private String jsonState;
    private List<String> jsonStateStack;


    String JSONString;

    @Override
    public JSONElement parse(Reader r) {
        jsonStateStack.add(jsonState);

        String currentValue = "";
        StringBuilder stringBuilder = new StringBuilder();

        Character symbol = readSymbol(r);
        while(!jsonState.equals(END_OF_STREAM)){
            JSONEvent ev = new JSONEvent();
            String event = ev.checkEvent(symbol);

            //currentValue = readJSONValue(r);
            /*
            if (event.equals(ev.READING_VALUE)){
                jsonState = COMPILING_VALUE;
            }else{
                jsonState = VALUE_HAS_BEEN_READ;
            }*/

            updateState(event);

            currentValue = readValue(stringBuilder, ev.getValue());

            /*
            if (jsonState.equals(COMPILING_VALUE)){
                stringBuilder.append(ev.getValue());
            }

            if(jsonState.equals(VALUE_HAS_BEEN_READ)){
                currentValue = stringBuilder.toString();
                System.out.println(currentValue);
                stringBuilder = new StringBuilder();
            }*/

            if (jsonState.equals(READING_ARRAY)){

            }
            //System.out.println(jsonState + " : " + event);
            System.out.println(currentValue);
            symbol = readSymbol(r);
        }

        jsonStateStack.add(jsonState);

        for (String s:jsonStateStack){
            System.out.println(s);
        }

        //JSONString = read(r);
        //JSONArray array = readAsArray(r);
        //JSONElementClass element = new JSONElementClass(array);

        //return element;
        return null;
    }

    private String readValue(StringBuilder sb, String value){
        String currentValue = new String();
        if (jsonState.equals(COMPILING_VALUE)){
            sb.append(value);
        }

        if(jsonState.equals(VALUE_HAS_BEEN_READ)){
            currentValue = sb.toString();
            //System.out.println(currentValue);
            sb = new StringBuilder();
        }
        return currentValue;
    }

    private void updateState(String event){
        /*
        if (event.equals(JSONEvent.READING_VALUE)){
            jsonState = COMPILING_VALUE;
        }else{
            jsonState = VALUE_HAS_BEEN_READ;
            jsonStateStack.add(jsonState);
        }*/
        jsonState = COMPILING_VALUE;

        if (event.equals(JSONEvent.JSON_OBJECT_SEPARATOR)||event.equals(JSONEvent.JSON_ARRAY_END)||event.equals(JSONEvent.JSON_OBJECT_END)){
            jsonState = VALUE_HAS_BEEN_READ;
            jsonStateStack.add(jsonState);
        }

        if (event.equals(JSONEvent.JSON_ARRAY_START)){
            jsonState = READING_ARRAY;
            jsonStateStack.add(jsonState);
        }

        if (event.equals(JSONEvent.JSON_ARRAY_END)){
            jsonState = VALUE_HAS_BEEN_READ;
            //jsonStateStack.add(jsonState);
            jsonStateStack.add(ARRAY_HAS_BEEN_READ);
        }

        //if (!jsonState.equals(COMPILING_VALUE)){
        //    jsonStateStack.add(jsonState);
        //}



    }

    /*
    private String readJSONValue(Reader r){
        while(!jsonState.equals(END_OF_STREAM)){

        }
    }*/



    public ImplementedJsonParser() {
        jsonStateStack = new ArrayList<>();
        jsonState = WAITING_FOR_INPUT;
    }

    private Character readSymbol(Reader r) {
        Character c = null;
        try {
            int code = r.read();
            c = (char)code;
            if (code == -1){
                jsonState = END_OF_STREAM;
                r.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return c;
    }


    private JSONArray readAsArray(Reader r) {
        JSONArray array = new JSONArrayClass();
        //String rawData = read(r);
        String rawData = eliminateBrackets(JSONString);
        String[] chunks = rawData.split(",");
        for (String chunk : chunks) {
            JSONPrimitiveClass p = new JSONPrimitiveClass(chunk);
            JSONElementClass el = new JSONElementClass(p);
            array.add(el);
        }
        return array;
    }

    private String eliminateBrackets(String string) {
        String regex = "\\[([^]]+)\\]";
        Matcher m = Pattern.compile(regex).matcher(string);
        while (m.find()) {
            return m.group(1);
        }
        return "not array";
    }

    private String read(Reader r) {
        //List<Character> rawData = new ArrayList<Character>();
        StringBuilder stringBuilder = new StringBuilder();
        try {
            int code = r.read();
            while (code != -1) {
                char c = (char) code;
                code = r.read();
                stringBuilder.append(c);
            }
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ;
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        ImplementedJsonParser sjp = new ImplementedJsonParser();
        String jsonArray = "[1,2,3,4,856]";
        JSONElement je = sjp.parse(new StringReader(jsonArray));
    }


}
