package ru.nojs.json;

import org.junit.Assert;
import org.junit.Test;
import ru.vdovin.jsonParser.ImplementedJsonParser;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class JsonParserTest {
    final private StreamingJsonParser sjp = new ImplementedJsonParser();

    @Test
    public void testCorrectArrayParse() {
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

    @Test(expected = IllegalStateException.class)
    public void testWrongType() {
        String jsonPrimitive = "66";
        JSONElement je = sjp.parse(new StringReader(jsonPrimitive));
        boolean invalid = je.getAsBoolean();
        // It's not a boolean, exception should be thrown
    }

    @Test
    public void testInteger() {
        String jsonPrimitive = "-66";
        JSONElement je = sjp.parse(new StringReader(jsonPrimitive));
        int integer = je.getAsInt();
        Assert.assertEquals("Negative integers parsed correctly", -66, integer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadInteger() {
        String jsonPrimitive = "6-6";
        JSONElement je = sjp.parse(new StringReader(jsonPrimitive));
        //Not an integer.
    }


    @Test(expected = IllegalArgumentException.class)
    public void testSyntaxError() {
        String badSyntax = "{true";
        JSONElement je = sjp.parse(new StringReader(badSyntax));
        boolean invalid = je.getAsBoolean();
        // It's not boolean either, we expect exception to be thrown
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

    @Test
    public void testEqualsPrimitive() {
        JSONPrimitive jp1 = new JSONPrimitiveImpl("test");
        JSONPrimitive jp2 = new JSONPrimitiveImpl("test");

        Assert.assertTrue(jp1.equals(jp2));
    }

    @Test
    public void testEqualsArray() {

        JSONPrimitive jp1 = new JSONPrimitiveImpl("test");
        JSONPrimitive jp2 = new JSONPrimitiveImpl("test");

        JSONArray ja1 = new JSONArrayImpl();
        ja1.add(jp1);
        JSONArray ja2 = new JSONArrayImpl();
        ja2.add(jp2);

        Assert.assertTrue(ja1.equals(ja2));
    }

    @Test
    public void testEqualsObj() {

        JSONPrimitive jp1 = new JSONPrimitiveImpl("test");
        JSONPrimitive jp2 = new JSONPrimitiveImpl("test");

        JSONArray ja1 = new JSONArrayImpl();
        ja1.add(jp1);
        JSONArray ja2 = new JSONArrayImpl();
        ja2.add(jp2);

        JSONObject jo1 = new JSONObjectImpl();
        jo1.add("test", ja1);
        JSONObject jo2 = new JSONObjectImpl();
        jo2.add("test", ja2);

        Assert.assertTrue(jo1.equals(jo2));
    }


    @Test
    public void testBigObj() {
        String noSuchField =
                "{\n" +
                        "  \"a\": \"a\",\n" +
                        "  \"b\": 2,\n" +
                        "  \"c\": \"c\"\n" +
                        "}";
        JSONElement je = new ImplementedJsonParser().parse(new StringReader(noSuchField));
    }
}
