package ru.dkom.jsonParser;

import ru.nojs.json.JSONArray;
import ru.nojs.json.JSONElement;
import ru.nojs.json.StreamingJsonParser;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImplementedJsonParser implements StreamingJsonParser{

    private Map<String, JSONObjectClass> objects;
    private Map<String, JSONArrayClass> arrays;

    @Override
    public JSONElement parse(Reader r) {
        JSONArray array = readAsArray(r);
        JSONElementClass element = new JSONElementClass(array);
        //read(r);
        return element;
    }

    public ImplementedJsonParser(){
        objects = new HashMap<>();
        arrays = new HashMap<>();
    }

    private JSONArray readAsArray(Reader r){
        JSONArray array = new JSONArrayClass();
        String rawData = read(r);
        rawData = eliminateBrackets(rawData);
        String[] chunks = rawData.split(",");
        for(String chunk: chunks){
            PrimitiveHelper pr = new PrimitiveHelper(chunk);
            JSONPrimitiveClass p = new JSONPrimitiveClass(pr.getValue());
            JSONElementClass el = new JSONElementClass(p);
            array.add(el);
        }
        return array;
    }

    private String eliminateBrackets(String string){
        String regex = "\\[([^]]+)\\]";
        Matcher m = Pattern.compile(regex).matcher(string);
        while(m.find()){
            return m.group(1);
        }
        return "nothing found";
    }

    private String read(Reader r){
        //List<Character> rawData = new ArrayList<Character>();
        StringBuilder stringBuilder = new StringBuilder();
        try{

            int code = r.read();
            while(code != -1){
                char c = (char) code;
            //    rawData.add(c);
            //    System.out.print(code);
            //    System.out.print(" ");
            //    System.out.println(c);
                code = r.read();
                stringBuilder.append(c);
            }
            r.close();
        }catch (IOException e){
            e.printStackTrace();
        };
        return stringBuilder.toString();
    }

    public static void main(String[] args){
        ImplementedJsonParser sjp = new ImplementedJsonParser();
        String jsonArray = "[1,2,3,4]";
        JSONElement je = sjp.parse(new StringReader(jsonArray));
    }


}
