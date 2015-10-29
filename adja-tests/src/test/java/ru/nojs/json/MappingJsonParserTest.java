package ru.nojs.json;

import org.junit.Assert;
import org.junit.Test;
import ru.vdovin.jsonParser.ImplementedJsonParser;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;

import static org.mockito.Mockito.*;

public class MappingJsonParserTest {
    private static final MappingJsonParser mjp =  new MappingJsonParser() {

        private final ImplementedJsonParser sjp = new ImplementedJsonParser();

        @Override
        public <T> T parse(Reader r, Mapper<T> mapper) {
            T result = mapper.map(sjp.parse(r));
            return result;
        }
    };



  /*mock(MappingJsonParser.class);
    static { // replace me
        when(mjp.parse(any(), any())).thenThrow(new NotImplementedException());
    }*/

    @Test
    public void testSimpleMapper() {
        String json = "\"abcdef\"";
        Reader r = new StringReader(json);
        Mapper<String> stringMapper = JSONElement::getAsString;
        String result  = mjp.parse(r, stringMapper);
        Assert.assertEquals("I shall not crash", "abcdef", result);


        Mapper<Boolean> sdf = JSONElement::getAsBoolean;
    }

    @Test
    public void testMapMapper() {
        String json = "{\"a\":\"abcdef\", \"b\": \"bgedf\"}";
        Reader r = new StringReader(json);
        Mapper<HashMap<String, String>> hashMapMapper = e -> {
            HashMap<String, String> result = new HashMap<>();
            e.getAsJsonObject()
                    .getAll()
                    .forEach((k,v) -> result.put(k, v.getAsString()));
            return result;
        };
        HashMap<String, String> result = mjp.parse(r, hashMapMapper);
        Assert.assertEquals("key 'a' bounded correctly", "abcdef", result.get("a"));
        Assert.assertEquals("key 'b' bounded correctly", "bgedf", result.get("b"));
    }

    @Test
    public void testHeterogeneousMapper() { // Is it even possible?!
        String json = "{\"a\": \"abcdef\", \"b\": 1234}";
        Reader r = new StringReader(json);
        Mapper<HashMap<String, ?>> hashMapMapper = mock(Mapper.class, "replace me");
        HashMap<String, ?> result = mjp.parse(r, hashMapMapper);
        Assert.assertEquals("key 'a' bounded correctly", "abcdef", result.get("a"));
        Assert.assertEquals("key 'b' bounded correctly", 1234, result.get("b"));
    }

    @Test
    public void testPOJOMapper() {
        String json = "{\"name\": \"Connor McLeod\", \"age\": 4412}";
        Human ref = new Human().setAge(4412).setName("Connor McLeod");
        Reader r = new StringReader(json);
        Mapper<Human> someBeanMapper = mock(Mapper.class, "replace me");
        Human result = mjp.parse(r, someBeanMapper);
        Assert.assertEquals("names match", "Connor McLeod", result.getName());
        Assert.assertEquals("ages match", 4412, result.getAge());
        Assert.assertEquals("we ve got our highlander", ref, result);
    }

    private static class Human { //TODO: incomplete.
        private String name;
        private long age;
        public Human() {

        }

        public String getName() {
            return name;
        }

        public Human setName(String name) {
            this.name = name;

            return this;
        }

        public long getAge() {
            return age;
        }

        public Human setAge(long age) {
            this.age = age;

            return this;
        }
    }

}
