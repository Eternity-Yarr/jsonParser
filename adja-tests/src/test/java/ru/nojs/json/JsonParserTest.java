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
        /* .. */
    }
}