package ru.nojs.json;

import org.junit.Assert;
import org.junit.Test;
import ru.vdovin.jsonParser.ImplementedJsonParser;
import ru.vdovin.jsonParser.MyJSONPrimitive;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;

import static org.mockito.Mockito.*;

public class MappingJsonParserTest {

    private static final ImplementedJsonParser sjp = new ImplementedJsonParser();

    private static final MappingJsonParser mjp =  new MappingJsonParser() {

        @Override
        public <T> T parse(Reader r, Mapper<T> mapper) {
            T result = null;
            try {
                result = mapper.map(sjp.parse(r));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            return result;
        }
    };


    @Test
    public void testSimpleMapper() {
        String json = "\"abcdef\"";
        Reader r = new StringReader(json);
        Mapper<String> stringMapper = JSONElement::getAsString;
        String result  = mjp.parse(r, stringMapper);
        Assert.assertEquals("I shall not crash", "abcdef", result);
    }

    @Test
    public void testMapMapper() {
        String json = "{\"a\":\"abcdef\", \"b\": \"bgedf\"}";
        Reader r = new StringReader(json);
        Mapper<HashMap<String, String>> hashMapMapper = e -> {
            JSONObject jo = e.getAsJsonObject();
            HashMap<String, String> result = new HashMap<>();
            jo
                    .entrySet()
                    .forEach((s) -> result.put(s.getKey(), s.getValue().getAsString()));

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
        Mapper<HashMap<String, ?>> hashMapMapper = e -> {
            JSONObject jo = e.getAsJsonObject();
            HashMap<String,Object> result = new HashMap<>();
            jo
                    .entrySet()
                    .forEach((s) -> {
                        MyJSONPrimitive jp = (MyJSONPrimitive)s.getValue();
                        result.put(s.getKey(), jp.getAsObject());
                    });
            return result;
        };
        HashMap<String, ?> result = mjp.parse(r, hashMapMapper);
        Assert.assertEquals("key 'a' bounded correctly", "abcdef", result.get("a"));
        Assert.assertEquals("key 'b' bounded correctly", 1234, result.get("b"));
    }

    @Test
    public void testPOJOMapper() {
        String json = "{\"name\": \"Connor McLeod\", \"age\": 4412}";
        Human ref = new Human().setAge(4412).setName("Connor McLeod");
        Reader r = new StringReader(json);
        Mapper<Human> someBeanMapper = e -> {
            Human result = new Human();
            JSONObject jo = e.getAsJsonObject();
            result
                    .setName(jo.get("name").getAsString())
                    .setAge(jo.get("age").getAsLong());

            return  result;
        };
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

        @Override
        public boolean equals(Object o) {
            Human human = (Human)o;
            return this.equals(human);
        }

        public boolean equals(Human h) {
            return this.name.equals(h.getName()) && this.age == h.getAge();
        }

    }

}
