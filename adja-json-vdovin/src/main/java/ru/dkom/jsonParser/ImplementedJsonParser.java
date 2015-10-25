package ru.dkom.jsonParser;

import ru.nojs.json.JSONArray;
import ru.nojs.json.JSONElement;
import ru.nojs.json.StreamingJsonParser;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImplementedJsonParser implements StreamingJsonParser {
    private final static String WAITING_FOR_INPUT = "WAITING_FOR_INPUT";
    private final static String COMPILING_VALUE = "COMPILING_VALUE";
    private final static String VALUE_HAS_BEEN_READ = "VALUE_HAS_BEEN_READ";
    private final static String END_OF_STREAM = "_NO_MORE_SYMBOLS_IN_THE_STREAM_";

    private String jsonState;


    String JSONString;

    @Override
    public JSONElement parse(Reader r) {
        String currentValue = "";
        StringBuilder stringBuilder = new StringBuilder();

        Character symbol = readSymbol(r);
        while(!jsonState.equals(END_OF_STREAM)){
            JSONEvent ev = new JSONEvent();
            String event = ev.checkEvent(symbol);

            //currentValue = readJSONValue(r);


            if (event.equals(ev.READING_VALUE)){
                jsonState = COMPILING_VALUE;
            }else{
                jsonState = VALUE_HAS_BEEN_READ;
            }

            if (jsonState.equals(COMPILING_VALUE)){
                stringBuilder.append(ev.getValue());
            }

            if(jsonState.equals(VALUE_HAS_BEEN_READ)){
                currentValue = stringBuilder.toString();
                System.out.println(currentValue);
                stringBuilder = new StringBuilder();
            }
            System.out.println(jsonState + " : " + event);
            //System.out.println(currentValue);
            symbol = readSymbol(r);
        }

        //JSONString = read(r);
        //JSONArray array = readAsArray(r);
        //JSONElementClass element = new JSONElementClass(array);

        //return element;
        return null;
    }

    /*
    private String readJSONValue(Reader r){
        while(!jsonState.equals(END_OF_STREAM)){

        }
    }*/



    public ImplementedJsonParser() {
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
