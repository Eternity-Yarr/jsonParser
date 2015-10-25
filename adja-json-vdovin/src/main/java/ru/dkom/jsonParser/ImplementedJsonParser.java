package ru.dkom.jsonParser;

import com.sun.org.apache.xpath.internal.operations.Bool;
import ru.nojs.json.*;

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

    private final static String READING_PRIMITIVE = "READING_PRIMITIVE";

    private final static String READING_JSON_PROPERTY = "READING_JSON_PROPERTY";

    private String jsonState;
    private List<String> jsonStateStack;


    String JSONString;

    public String readStream(Reader r){

        String currentValue = "";
        StringBuilder stringBuilder = new StringBuilder();
        Character symbol = null;

        JSONEvent event = new JSONEvent();
        String evDescriptor = new String();

        while (true){
            symbol = readSymbol(r);

            if (jsonState.equals(END_OF_STREAM)){
                currentValue = stringBuilder.toString();
                break;
            }
            evDescriptor = event.checkEvent(symbol);

            if (evDescriptor.equals(JSONEvent.NOTHING_HAPPENS)){
                continue;
            }

            //System.out.println(evDescriptor);

            updateState(evDescriptor);
            if (event.getValue().equals("")){
                //updateState(evDescriptor);

                if (evDescriptor.equals(JSONEvent.QUOTES_DETECTED)){
                    stringBuilder.append("\"");
                    continue;
                }

                if (stringBuilder.length() == 0){
                    continue;
                }
                currentValue = stringBuilder.toString();
                stringBuilder = new StringBuilder();
                break;
            }else{
                stringBuilder.append(symbol);
            }
        }
        return currentValue;
    }

    @Override
    public JSONElement parse(Reader r) {
        JSONArray jsonArray = new JSONArrayClass();
        JSONObject jsonObject = new JSONObjectClass();
        JSONPrimitive jsonPrimitive = null;

        Boolean itsObject = false;
        Boolean itsPrimitive = false;

        jsonState = WAITING_FOR_INPUT;
        jsonStateStack = new ArrayList<>();
        jsonStateStack.add(jsonState);

        String currentValue = "";
        List<String> values = new ArrayList<>();

        while(!jsonState.equals(END_OF_STREAM)){
            //currentValue = readStream(r);
            values.add(readStream(r));
            currentValue = values.get(values.size()-1);

            if (currentValue.equals("")){
                continue;
            }
            if (jsonState.equals(READING_ARRAY)){
                
                JSONPrimitiveClass p = new JSONPrimitiveClass(currentValue);
                JSONElementClass el = new JSONElementClass(p);
                jsonArray.add(el);
            }

            if (jsonState.equals(READING_JSON_PROPERTY)){
                itsObject = true;
                String propName = values.get(values.size()-2);
                jsonObject.addProperty(propName, currentValue);
            }

            if(jsonStateStack.get(jsonStateStack.size() - 1).equals(READING_PRIMITIVE)){
                itsPrimitive = true;
                jsonPrimitive = new JSONPrimitiveClass(currentValue);

            }
            System.out.println(currentValue + " : " + jsonStateStack.get(jsonStateStack.size() - 1));
            //System.out.println(currentValue + " : " + jsonState);
        }

        jsonStateStack.add(jsonState);

        for (String s:jsonStateStack){
            System.out.println(s);
        }

        JSONElementClass element = null;
        if (itsObject){
            element = new JSONElementClass(jsonObject);
        }if (itsPrimitive){
            element = new JSONElementClass(jsonPrimitive);
        }else {
            element = new JSONElementClass(jsonArray);
        }


        return element;
    }


    private void updateState(String event){
        jsonState = COMPILING_VALUE;

        //primitive detection
        if(event.equals(JSONEvent.READING_VALUE)){
            jsonState = READING_PRIMITIVE;
            if(!jsonStateStack.get(jsonStateStack.size()-1).equals(READING_PRIMITIVE)){
                jsonStateStack.add(jsonState);
            }
        }

        if (event.equals(JSONEvent.JSON_ARRAY_START)){
            jsonState = READING_ARRAY;
            jsonStateStack.add(jsonState);
        }

        if (event.equals(JSONEvent.JSON_ARRAY_END)){
            jsonState = ARRAY_HAS_BEEN_READ;
            jsonStateStack.add(ARRAY_HAS_BEEN_READ);
        }

        if (event.equals(JSONEvent.JSON_OBJECT_SEPARATOR)){
            jsonState = jsonStateStack.get(jsonStateStack.size()-1);
        }

        if (event.equals(JSONEvent.JSON_PRIMITIVE_VALUE_START)){
            jsonState = READING_JSON_PROPERTY;
            jsonStateStack.add(READING_JSON_PROPERTY);
        }

    }

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





    public static void main(String[] args) {
        ImplementedJsonParser sjp = new ImplementedJsonParser();
        String jsonArray = "[1,2,3,4,856]";
        //JSONElement je = sjp.parse(new StringReader(jsonArray));

        String str = "\"test\"";
        str = "test    ";
        str = "\t [\t\t\n\n\r    true , \r\t\n  false\r\t\n] \n";
        JSONElement je = sjp.parse(new StringReader(str));
        //je = sjp.parse(new StringReader(str));

        //String jsnobj = "{\"a\":1}";
        //je = sjp.parse(new StringReader(jsnobj));
    }


}
