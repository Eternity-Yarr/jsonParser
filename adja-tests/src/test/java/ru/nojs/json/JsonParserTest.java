package ru.nojs.json;

import org.junit.Assert;
import org.junit.Test;
import ru.vdovin.jsonParser.ImplementedJsonParser;
import ru.nojs.json.StreamingJsonParser;
import ru.vdovin.jsonParser.MyJSONElement;

import java.io.IOException;
import java.io.StringReader;
import java.util.stream.StreamSupport;

public class JsonParserTest {
    final private StreamingJsonParser sjp = new ImplementedJsonParser();


    @Test
    public  void testStringPrimitive() throws Exception {
        String str = "\"test\"";
        JSONElement je = sjp.parse(new StringReader(str));
        Assert.assertTrue("We ve got a primitive", je.isJsonPrimitive());
        Assert.assertEquals("Primitive value parsed correctly too", "test", je.getAsString());
    }

    @Test
    public void testNumberPrimitive() throws Exception {
        String str = "66";
        JSONElement je = sjp.parse(new StringReader(str));
        Assert.assertTrue("We ve got a primitive", je.isJsonPrimitive());
        Assert.assertEquals("Primitive value parsed correctly too", 66, je.getAsInt());

        String str1 = "3.14";
        JSONElement je1 = sjp.parse(new StringReader(str1));
        Assert.assertTrue("We ve got a primitive", je1.isJsonPrimitive());
        //System.out.print(je1.toString());
        Assert.assertEquals("Primitive value parsed correctly too", 3.14, je1.getAsDouble(), 0.001);
    }

    @Test(expected = IllegalStateException.class)
    public void testWrongType() throws Exception {
        String jsonPrimitive = "66";
        JSONElement je = sjp.parse(new StringReader(jsonPrimitive));
        boolean invalid = je.getAsBoolean();
        // It's not a boolean, exception should be thrown
    }

    @Test
    public  void testBoolean() throws Exception {
        String str = "true";
        JSONElement je = sjp.parse(new StringReader(str));
        Assert.assertTrue("We ve got a primitive", je.isJsonPrimitive());
        Assert.assertTrue("Boolean value parsed correctly", je.getAsBoolean());
    }


    @Test
    public  void testSmallObject() throws Exception {
        String str = "{\"a\":1}";
        JSONElement je = sjp.parse(new StringReader(str));
        Assert.assertTrue("We ve got a object", je.isJsonObject());
        JSONObject jo = je.getAsJsonObject();
        JSONPrimitive numPrimitive = jo.get("a").getAsJsonPrimitive();
        Assert.assertEquals("Number parsed correctly", 1, numPrimitive.getAsInt());

        String someDict = "{\"b\":\"apples\"}";
        JSONElement je1 = sjp.parse(new StringReader(someDict));
        Assert.assertTrue("We ve got object indeed", je1.isJsonObject());
        JSONObject jo1 = je1.getAsJsonObject();
        JSONPrimitive stringPimitive = jo1.get("b").getAsJsonPrimitive();
        Assert.assertEquals("Number parsed correctly", "apples", stringPimitive.getAsString());

    }

    @Test
    public void testJsonObjectParse() throws Exception {
        String someDict = "{\"a\":5,\"b\":\"apples\",\"c\":\"it's work?\"}";
        JSONElement je = sjp.parse(new StringReader(someDict));
        Assert.assertTrue("We ve got object indeed", je.isJsonObject());
        JSONObject jo = je.getAsJsonObject();
        JSONPrimitive numPrimitive = jo.get("a").getAsJsonPrimitive();
        Assert.assertEquals("Number parsed correctly", 5, numPrimitive.getAsInt());
        JSONPrimitive stringPrimitive = jo.get("b").getAsJsonPrimitive();
        Assert.assertEquals("String parsed correctly too", "apples", stringPrimitive.getAsString());

        JSONPrimitive stringPrimitive1 = jo.get("c").getAsJsonPrimitive();
        Assert.assertEquals("String parsed correctly too", "it's work?", stringPrimitive1.getAsString());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectSyntaxError() throws Exception {
        String badSyntax = "{\"true\":";
        JSONElement je = sjp.parse(new StringReader(badSyntax));
        boolean invalid = je.getAsBoolean();
        // It's not boolean either, we expect exception to be thrown
    }


    @Test(expected = IllegalArgumentException.class)
    public void testSyntaxError() throws Exception {
        String badSyntax = "{true";
        JSONElement je = sjp.parse(new StringReader(badSyntax));
        boolean invalid = je.getAsBoolean();
        // It's not boolean either, we expect exception to be thrown
    }







   // @Test
   // public void testCorrectArrayParse() {
   //     String jsonArray = "[1,2,3,4]";
    //    JSONElement je = sjp.parse(new StringReader(jsonArray));
   //     Assert.assertTrue("We ve got an array", je.isJsonArray());
   //     JSONArray array = je.getAsJsonArray();
   //     Assert.assertEquals("It contains expected number of elements", 4, array.size());
   //     int sum = StreamSupport
   //             .stream(array.spliterator(), false)
   //             .map(JSONElement::getAsInt)
   //             .reduce(0, (acc, i) -> acc + i);
   //     Assert.assertEquals("Sum of elements matches", 10, sum);
   // }






}