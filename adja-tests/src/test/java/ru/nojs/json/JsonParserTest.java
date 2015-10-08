package ru.nojs.json;

import org.junit.Assert;
import org.junit.Test;
import ru.vdovin.jsonParser.ImplementedJsonParser;

import java.io.StringReader;
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

    @Test(expected = IllegalArgumentException.class)
    public void testSyntaxError() {
        String badSyntax = "{true";
        JSONElement je = sjp.parse(new StringReader(badSyntax));
        boolean invalid = je.getAsBoolean();
        // It's not boolean either, we expect exception to be thrown
    }

    @Test
    public void testJsonObjectParse() {
        String someDict = "{\"a\": 5, \"b\": \"apples\"}";
        JSONElement je = sjp.parse(new StringReader(someDict));
        Assert.assertTrue("We ve got object indeed", je.isJsonObject());
        JSONObject jo = je.getAsJsonObject();
        JSONPrimitive numPrimitive = jo.get("a").getAsJsonPrimitive();
        Assert.assertEquals("Number parsed correctly", 5, numPrimitive.getAsInt());
        JSONPrimitive stringPrimitive = jo.get("b").getAsJsonPrimitive();
        Assert.assertEquals("String parsed correctly too", "apples", stringPrimitive.getAsString());
    }
}