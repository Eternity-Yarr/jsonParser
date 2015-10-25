package ru.dkom.jsonParser;

import ru.nojs.json.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

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
    private JSONStateStack jsonStateStack;

    private String streamState;

    public String readStream(Reader r){

        String currentValue = "";
        StringBuilder stringBuilder = new StringBuilder();
        Character symbol;

        JSONEvent event = new JSONEvent();
        String evDescriptor = new String();
        Boolean readInQuotes = false;

        while (true){
            symbol = readSymbol(r);

            if (jsonState.equals(END_OF_STREAM)){
                updateState(JSONEvent.READING_VALUE);
                streamState = END_OF_STREAM;
                currentValue = stringBuilder.toString();
                break;
            }
            evDescriptor = event.checkEvent(symbol,readInQuotes);

            if (evDescriptor.equals(JSONEvent.INSIGNIFICANT_SYMBOL)){
                continue;
            }

            //updateState(evDescriptor);
            if (event.getValue().equals("")){
                updateState(evDescriptor);
                if (evDescriptor.equals(JSONEvent.QUOTES_DETECTED)){
                    //stringBuilder.append("\"");
                    System.out.println("quotes found");
                    Character lastChar = 'a';
                    if (stringBuilder.length() > 0){
                        lastChar = stringBuilder.charAt(stringBuilder.length()-1);
                    }

                    if (!lastChar.equals(new Character((char)92))) {
                        readInQuotes = !readInQuotes;
                        System.out.println("quotes mode flipped");
                    }else{
                        char[] chars = stringBuilder.toString().toCharArray();
                        chars[chars.length-1] = (char)34;
                        String t = new String(chars);
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(t);
                    }
                    //    System.out.println("quotes mode flipped");
                    //}else{
                        //stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length() - 1, Character.toString((char)34));
                        //stringBuilder.append("\"");
                    //    Boolean bh = true;
                    //}
                    continue;
                }



                if (stringBuilder.length() == 0){
                    continue;
                }
                currentValue = stringBuilder.toString();
                stringBuilder = new StringBuilder();
                break;
            }else{
                //if (!symbol.equals((char)92)){
                    stringBuilder.append(symbol);
                //}else{
                //    stringBuilder.append((char)34);
                //}
            }
        }
        //updateState(evDescriptor);
        return currentValue;
    }

    @Override
    public JSONElement parse(Reader r) {
        JSONArray jsonArray = new JSONArrayClass();
        JSONObject jsonObject = new JSONObjectClass();

        jsonState = WAITING_FOR_INPUT;
        streamState = WAITING_FOR_INPUT;
        jsonStateStack = new JSONStateStack();
        jsonStateStack.push(jsonState);

        JSONElement element = null;

        String currentValue = "";
        List<String> values = new ArrayList<>();

        while(!streamState.equals(END_OF_STREAM)){

            values.add(readStream(r));
            currentValue = values.get(values.size()-1);

            if (currentValue.equals("")){
                continue;
            }

            jsonState = jsonStateStack.readLast();

            if (jsonState.equals(READING_ARRAY)){
                JSONPrimitiveClass p = new JSONPrimitiveClass(currentValue);
                JSONElementClass el = new JSONElementClass(p);
                jsonArray.add(el);
            }

            if (jsonState.equals(ARRAY_HAS_BEEN_READ)){
                JSONPrimitiveClass p = new JSONPrimitiveClass(currentValue);
                JSONElementClass el = new JSONElementClass(p);
                jsonArray.add(el);
                jsonStateStack.push(WAITING_FOR_INPUT);
                element = new JSONElementClass(jsonArray);
                jsonArray = new JSONArrayClass();
            }

            if (jsonState.equals(READING_JSON_PROPERTY)){

                String propName = values.get(values.size()-2);
                jsonObject.addProperty(propName, currentValue);
            }

            if(jsonState.equals(READING_PRIMITIVE)){
                element = new JSONElementClass(new JSONPrimitiveClass(currentValue));
            }
            System.out.println(currentValue + " : " + jsonStateStack.readLast());
        }


        jsonStateStack.push(jsonState);

        //JSONElementClass element = new JSONElementClass(jsonArray);


        System.out.println(jsonStateStack.toString());

        return element;
    }


    private void updateState(String event){
        jsonState = WAITING_FOR_INPUT;

        if (jsonState.equals(END_OF_STREAM)){
            jsonState = READING_PRIMITIVE;
            jsonStateStack.push(jsonState);
        }

        //primitive detection
        if(event.equals(JSONEvent.READING_VALUE)){
            jsonState = READING_PRIMITIVE;
            jsonStateStack.push(jsonState);
        }

        if (event.equals(JSONEvent.JSON_ARRAY_START)){
            jsonState = READING_ARRAY;
            jsonStateStack.push(jsonState);
        }

        if (event.equals(JSONEvent.JSON_ARRAY_END)){
            jsonState = ARRAY_HAS_BEEN_READ;
            jsonStateStack.push(jsonState);
        }

        if (event.equals(JSONEvent.JSON_OBJECT_SEPARATOR)){
            jsonState = jsonStateStack.readLast();
        }

        if (event.equals(JSONEvent.JSON_PRIMITIVE_VALUE_START)){
            jsonState = READING_JSON_PROPERTY;
            jsonStateStack.push(jsonState);
        }

    }

    public ImplementedJsonParser() {
        //jsonStateStack = new ArrayList<>();
        jsonState = WAITING_FOR_INPUT;
        jsonStateStack = new JSONStateStack();
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
        JSONElement je = null;
        JSONArray a = null;
        String str = "";
        int size = 0;

        //String jsonArray = "[1,2,3,4,856]";
        //je = sjp.parse(new StringReader(jsonArray));

        //JSONArray a = je.getAsJsonArray();

        //int size = a.size();
        //System.out.println(size);

        //str = "\"test\"";
        //str = "test    ";
        //str = "\t [\t\t\n\n\r    true , \r\t\n  false\r\t\n] \n";
        //je = sjp.parse(new StringReader(str));
        //a = je.getAsJsonArray();
        //size = a.size();
        //System.out.println(size);

        //String jsnobj = "{\"a\":1}";
        //je = sjp.parse(new StringReader(jsnobj));

        je = sjp.parse(new StringReader("\"\t\r\n\\\" \""));
        String test = je.getAsString();
        String model = "\t\r\n\" ";
        System.out.println(test.equals("\t\r\n\"\"\" "));
        System.out.println(test.equals(model));

        char[] modelChars = new char[model.length()];
        char[] testChars = new char[test.length()];

        modelChars = model.toCharArray();
        testChars = test.toCharArray();

        for (char c: modelChars){
            System.out.print((int) c + " ");
        }
        System.out.println();
        for (char c: testChars){
            System.out.print((int) c + " ");
        }


    }


}
