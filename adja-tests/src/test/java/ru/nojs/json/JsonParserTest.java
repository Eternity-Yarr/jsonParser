package ru.nojs.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.Assert;
import org.junit.Test;
import ru.vdovin.jsonParser.ImplementedJsonParser;
//import ru.dkom.jsonParser.ImplementedJsonParser;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class JsonParserTest {
    final private StreamingJsonParser sjp = new ImplementedJsonParser();
    final private JsonParser reference = new JsonParser();

    @Test
    public  void testStringPrimitive() throws Exception {
        String str = "\"test\"";
        JSONElement je = sjp.parse(new StringReader(str));
        Assert.assertTrue("We ve got a primitive", je.isJsonPrimitive());
        Assert.assertEquals("Primitive string value parsed correctly too", "test", je.getAsString());
    }

    @Test(expected = IllegalArgumentException.class)
    public  void testErrorStringPrimitive() throws Exception {
        String str = "\"test";
        JSONElement je = sjp.parse(new StringReader(str));
        Assert.assertTrue("We ve got a primitive", je.isJsonPrimitive());
        Assert.assertEquals("Primitive string value parsed correctly too", "test", je.getAsString());
    }

    @Test(expected = IllegalArgumentException.class)
    public  void testErrorStringPrimitive2() throws Exception {
        String str = "test\"";
        JSONElement je = sjp.parse(new StringReader(str));
        Assert.assertTrue("We ve got a primitive", je.isJsonPrimitive());
        Assert.assertEquals("Primitive string value parsed correctly too", "test", je.getAsString());
    }

    @Test
    public void testNumberPrimitive() throws Exception {
        String str = "66";
        JSONElement je = sjp.parse(new StringReader(str));
        Assert.assertTrue("We ve got a primitive", je.isJsonPrimitive());
        Assert.assertEquals("Primitive integer value parsed correctly too", 66, je.getAsInt());
    }

    @Test
    public void testNumberPrimitive2() throws Exception {
        String str1 = "3.14";
        JSONElement je1 = sjp.parse(new StringReader(str1));
        Assert.assertTrue("We ve got a primitive", je1.isJsonPrimitive());
        //System.out.print(je1.toString());
        Assert.assertEquals("Primitive double value parsed correctly too", 3.14, je1.getAsDouble(), 0.001);
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
    public  void testBoolean2() throws Exception {
        String str = "false";
        JSONElement je = sjp.parse(new StringReader(str));
        Assert.assertTrue("We ve got a primitive", je.isJsonPrimitive());
        Assert.assertFalse("Boolean value parsed correctly", je.getAsBoolean());
    }

    @Test(expected = IllegalArgumentException.class)
    public  void testFailBoolean2() throws Exception {
        String str = "FALSE";
        JSONElement je = sjp.parse(new StringReader(str));
    }

    @Test(expected = IllegalStateException.class)
    public  void testBoolean3() throws Exception {
        String str = "\"FALSE\"";
        JSONElement je = sjp.parse(new StringReader(str));
        Assert.assertTrue("We ve got a primitive", je.isJsonPrimitive());
        Assert.assertFalse("Boolean value parsed correctly", je.getAsBoolean());
    }

    @Test(expected = IllegalArgumentException.class)
    public  void testBoolean4() throws Exception {
        String str = "false\"";
        JSONElement je = sjp.parse(new StringReader(str));
    }

    @Test
    public  void testNull() throws Exception {
        String str = "null";
        JSONElement je = sjp.parse(new StringReader(str));
        Assert.assertTrue("We ve got a null", je.isJsonNull());
    }

    @Test
    public  void testNull2() throws Exception {
        String str = "null";
        JSONElement je = sjp.parse(new StringReader(str));
        Assert.assertTrue("We ve got a null", je.isJsonNull());
        JSONNull jn = je.getAsJsonNull();
    }

    @Test
    public  void testFailNull() throws Exception {
        String str = "\"null\"";
        JSONElement je = sjp.parse(new StringReader(str));
        Assert.assertFalse("We ve got a null", je.isJsonNull());
    }

    @Test
    public  void testSmallObject() throws Exception {
        String str = "{\"a\":1}";
        JSONElement je = sjp.parse(new StringReader(str));
        Assert.assertTrue("We ve got a object", je.isJsonObject());
        JSONObject jo = je.getAsJsonObject();
        JSONPrimitive numPrimitive = jo.get("a").getAsJsonPrimitive();
        Assert.assertEquals("Number parsed correctly", 1, numPrimitive.getAsInt());
    }

    @Test
    public void testSmallObject2() throws Exception {
        String someDict = "{\"b\":\"apples\"}";
        JSONElement je1 = sjp.parse(new StringReader(someDict));
        Assert.assertTrue("We ve got object indeed", je1.isJsonObject());
        JSONObject jo1 = je1.getAsJsonObject();
        JSONPrimitive stringPrimitive = jo1.get("b").getAsJsonPrimitive();
        Assert.assertEquals("String parsed correctly", "apples", stringPrimitive.getAsString());
    }

    @Test
    public void testAnotherLittleObject() throws Exception {
        String str = "{\"a\":true}";
        JSONElement je = sjp.parse(new StringReader(str));
        Assert.assertTrue("We ve got an object", je.isJsonObject());
        JSONObject jo = je.getAsJsonObject();
        boolean b = jo.get("a").getAsBoolean();
        Assert.assertTrue("Boolean parsed correctly", b);
    }

    @Test
    public void testAnotherLittleObject2() throws Exception {
        String str = "{\"a\":null}";
        JSONElement je = sjp.parse(new StringReader(str));
        Assert.assertTrue("We ve got an object", je.isJsonObject());
        JSONObject jo = je.getAsJsonObject();
        Assert.assertTrue("Boolean parsed correctly", jo.get("a").isJsonNull());
    }

    @Test
    public void testJsonObjectParse() throws Exception {
        String someDict = "{\"a\":  5,\"b\": \"apples\",\"c\":\"it's work?\"}";
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

    @Test
    public void testInsignificantSymbolsInObject() throws Exception {
        String json = "{           \"a\" : \"abcdef\",             \"b\" : \"bgedf\"}";
        JSONElement je = sjp.parse(new StringReader(json));
        Assert.assertTrue("We ve got object indeed", je.isJsonObject());
        JSONObject jo = je.getAsJsonObject();
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


    @Test
    public void testObjectInObject() throws Exception {
        String str = "{\"a\":{\"b\":\"test\"}}";
        JSONElement je = sjp.parse(new StringReader(str));
        Assert.assertTrue("We ve got an object", je.isJsonObject());
        JSONObject jo1 = je.getAsJsonObject();
        Assert.assertTrue("We ve got an object", jo1.get("a").isJsonObject());
        JSONObject jo2 = jo1.get("a").getAsJsonObject();
        Assert.assertEquals("String parsed correctly", "test", jo2.get("b").getAsString());
    }

    @Test
    public void  testArrayOfInteger() throws Exception {
        String str = "[1,2,3]";
        JSONElement je = sjp.parse(new StringReader(str));
        Assert.assertTrue("We ve got an array", je.isJsonArray());
    }

    @Test
    public void  testArrayOfString() throws Exception {
        String str = "[\"a\",\"b\",\"c\",\"d\"]";
        JSONElement je = sjp.parse(new StringReader(str));
        Assert.assertTrue("We ve got an array", je.isJsonArray());
    }

    @Test
    public void  testArrayOfObject() throws Exception {
        String str = "[{\"a\":true},{\"a\":true},{\"a\":false}]";
        JSONElement je = sjp.parse(new StringReader(str));
        Assert.assertTrue("We ve got an array", je.isJsonArray());
        JSONArray ja = je.getAsJsonArray();
        JSONObject obj = ja.get(ja.size() - 1).getAsJsonObject();
        Assert.assertFalse("We ve got an obj", obj.get("a").getAsBoolean());

    }

    @Test
    public void testCorrectArrayParse() throws Exception {
        String jsonArray = "[1,2,3,4]";
        JSONElement je = sjp.parse(new StringReader(jsonArray));
        Assert.assertTrue("We ve got an array", je.isJsonArray());
        JSONArray array = je.getAsJsonArray();
        Assert.assertEquals("It contains expected number of elements", 4, array.size());
        int sum = StreamSupport
                .stream(array.spliterator(), false)
                .map(JSONElement::getAsInt)
                .reduce(0, (acc, i) -> acc + i);
        Assert.assertEquals("Sum of elements matches", 10, sum);
    }

    @Test
    public void testArrayInObject() throws Exception {
        String jsonArray = "{\"test\":[1,1,1,1]}";
        JSONElement je = sjp.parse(new StringReader(jsonArray));
        Assert.assertTrue("We ve got an array", je.isJsonObject());
        JSONObject jo = je.getAsJsonObject();
        Assert.assertTrue("We ve got an array", jo.get("test").isJsonArray());
        JSONArray array = jo.get("test").getAsJsonArray();
        Assert.assertEquals("It contains expected number of elements", 4, array.size());
    }

    @Test
    public void testInsignificantSymbols() throws  Exception {
        Assert.assertTrue(
                "Leading spaces",
                sjp.parse(new StringReader(" true")).getAsBoolean()
        );
        Assert.assertTrue(
                "Trailing spaces",
                sjp.parse(new StringReader("true ")).getAsBoolean()
        );
        Assert.assertTrue(
                "All sorts of insignificance",
                sjp.parse(new StringReader(" \t\t\n\n\r    true \r\t\n  ")).getAsBoolean()
        );
        Assert.assertTrue(
                "Array indented weirdly",
                sjp.parse(new StringReader("\t [\t\t\n\n\r    true , \r\t\n  false\r\t\n] \n")).getAsJsonArray().size() == 2
        );

        // Bonus level ^_^
        Assert.assertEquals(
                "Strings can have these symbols, and event an escaped quotes",
                "\t\r\n\" ", sjp.parse(new StringReader("\"\t\r\n\\\" \"")).getAsString()
        );
    }

    @Test
    public void testOptimalSizeRepresentation() throws Exception {
        String json = "1.4";
        JSONElement je = sjp.parse(new StringReader(json));
        JsonElement ref = reference.parse(new StringReader(json));
        Assert.assertEquals("We both got primitives", ref.isJsonPrimitive(), je.isJsonPrimitive());
        Assert.assertEquals("And they are equal, within delta", ref.getAsFloat(), je.getAsFloat(), 0.001);
    }


    @Test // Bonus level 2 : Hard
    public void testThreadSafety() throws Exception {
        String jsonObject = "{\"test\":[1,1,1,1,1,1,1,11,1,1,11,1,1,11,1,1,11,1,1,11,1,1,11,1,1,11,1,1,11,1,1,11,1,1,11,1,1,11,1,1]}";
        List<Reader> rdrs = IntStream
                .range(0, 10)
                .mapToObj(x -> new StringReader(jsonObject))
                .collect(Collectors.toList());

        boolean res = rdrs
                .parallelStream()
                .map(sjp::parse)
                .allMatch(je -> je.getAsJsonObject().get("test").getAsJsonArray().size() == 40);
        Assert.assertTrue(res);
    }









}