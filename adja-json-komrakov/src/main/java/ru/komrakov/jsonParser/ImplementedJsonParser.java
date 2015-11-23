package ru.komrakov.jsonParser;

import ru.nojs.json.JSONArray;
import ru.nojs.json.JSONElement;
import ru.nojs.json.StreamingJsonParser;

import java.io.Reader;
import java.io.StringReader;

public class ImplementedJsonParser implements StreamingJsonParser {

    @Override
    public JSONElement parse(Reader r) throws IllegalArgumentException{
        SmartStreamReader reader = new SmartStreamReader(r);
        JSONElement element = null;
        //FIXME: зачем так делать? только бесить варнингами IDEA-инспектора. Лучше убрать, здесь и везде
        element = buildJSON(element, reader);

        return element;
    }

    private JSONElement buildJSON(JSONElement element, SmartStreamReader reader) throws IllegalArgumentException{
        String value = reader.readNext();

        if (value.equals(StreamReader.NO_MORE_SYMBOLS_TO_READ)) {
            return element;
        }

        if (value.equals(StreamReader.JSON_ARRAY_START)) {
            element = new JSONArrayClass();
            while (!value.equals(StreamReader.JSON_ARRAY_END)) {
                JSONElement arrayMember = null;
                arrayMember = buildJSON(arrayMember, reader);
                ((JSONArrayClass) element).add(arrayMember);
                value = reader.readNext();
            }
            return element;
        }
        if (value.equals(StreamReader.JSON_OBJECT_START)) {
            element = new JSONObjectClass();
            while (!value.equals(StreamReader.JSON_OBJECT_END)) {
                value = reader.readNext();//get rid of "{"
                String propertyName = getRidOfQoutes(value);
                JSONElement propertyValue = null;
                propertyValue = buildJSON(propertyValue, reader);
                if (propertyValue == null) {
                    throw new IllegalArgumentException();
                }
                ((JSONObjectClass) element).add(propertyName, propertyValue);
                value = reader.readNext();
            }
            return element;
        }

        element = new JSONPrimitiveClass(value);

        return element;
    }

    private String getRidOfQoutes(String value){
        Character firstChar = value.charAt(0);
        Character lastChar = value.charAt(value.length()-1);
        //FIXME: контест по обфускации? :)
        //String QUOTE = "\"";
        //FIXME value.startsWith(QUOTE) && value.endsWith(QUOTE)
        if ((firstChar.equals((char)34))&&(lastChar.equals((char)34))){
            value = value.substring(1, value.length()-1);
        }
        return value;
    }

    public static void main(String[] args) {
        ImplementedJsonParser sjp = new ImplementedJsonParser();

        //FIXME: в vcs обычно не хранят закоменченый код. На то она и vcs. Если нужно что-то старое - чекаут.
        JSONElement je = null;
        JSONArray a = null;
        String str = "";
        int size = 0;

        //String badSyntax = "{true";
        //je = sjp.parse(new StringReader(badSyntax));
        //size = 0;


        //str = "[{\"a\":true},{\"a\":true},{\"a\":false}]";
        //je = sjp.parse(new StringReader(str));
        //size = 0;

        //String jsonArray = "[1,2,3,4,856]";
        //je = sjp.parse(new StringReader(jsonArray));
        //size = 0;

        //JSONArray a = je.getAsJsonArray();

        //int size = a.size();
        //System.out.println(size);

        //str = "\"test\"";
        //str = "test    ";
        //str = "  true";
        //je = sjp.parse(new StringReader(str));
        //Boolean t = je.getAsBoolean();
        //System.out.println(t);

        //str = "{\"a\":1}";
        //je = sjp.parse(new StringReader(str));
        //JSONObject jo = je.getAsJsonObject();
        //JSONPrimitive numPrimitive = jo.get("a").getAsJsonPrimitive();
        //size = 0;


        //str = "\t [\t\t\n\n\r    true , \r\t\n  false\r\t\n] \n";
        //je = sjp.parse(new StringReader(str));
        //a = je.getAsJsonArray();
        //size = a.size();
        //System.out.println(size);
        //str = "    test    ";
        //sjp.parse(new StringReader(str));

        //String jsnobj = "{\"a\":1}";
        //je = sjp.parse(new StringReader(jsnobj));


        //je = sjp.parse(new StringReader("test\""));
        //System.out.println(je.getAsString());

        je = sjp.parse(new StringReader("\"\t\r\n\\\" \""));
        String test = je.getAsString();
        String model = "\t\r\n\" ";
        System.out.println("Model as string: " + model);
        System.out.println("Test as string: " + test);
        System.out.println(test.equals("\t\r\n\"\"\" "));
        System.out.println(test.equals(model));

        char[] modelChars = new char[model.length()];
        char[] testChars = new char[test.length()];

        modelChars = model.toCharArray();
        testChars = test.toCharArray();

        System.out.print("model: ");
        for (char c: modelChars){
            System.out.print((int) c + " ");
        }
        System.out.println();
        System.out.print("test: ");
        for (char c: testChars){
            System.out.print((int) c + " ");
        }


    }



}
