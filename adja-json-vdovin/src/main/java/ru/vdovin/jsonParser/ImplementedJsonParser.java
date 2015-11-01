package ru.vdovin.jsonParser;

import ru.nojs.json.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ImplementedJsonParser implements StreamingJsonParser {
    List<Object> list = new ArrayList<>();
    public JSONArrayImpl JSONArrayImpl = new JSONArrayImpl();
    public JSONPrimitiveImpl jpi;
    private String s= "";
    public JSONElement parse(Reader r) {
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

            if (list.contains("[")&&list.contains(",")&&list.contains("]")){
                for (Object element : list){
                    if (element.equals("[")||element.equals(",")||element.equals("]")){
                        continue;
                    }
                    JSONArrayImpl.add(new JSONPrimitiveImpl(element));
                }
                return JSONArrayImpl;
            } else {
                for (Object obj : list){
                    s += String.valueOf(obj);
                    }
                if (s.matches("\\d+")){
                    jpi = new JSONPrimitiveImpl(Integer.parseInt(s));
                } else if (s.toLowerCase().equals("true") || s.toLowerCase().equals("false")){
                    jpi = new JSONPrimitiveImpl(Boolean.parseBoolean(s));
                } else if (s.matches("\\w+")){
                    jpi = new JSONPrimitiveImpl(s);
                } else {
                    throw new IllegalArgumentException("Bad syntax");
                }
                return jpi;
            }
    }
}
