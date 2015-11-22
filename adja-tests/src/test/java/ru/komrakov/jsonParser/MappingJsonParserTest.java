package ru.komrakov.jsonParser;

import org.junit.Assert;
import org.junit.Test;
import ru.komrakov.jsonMapper.MapperClass;
import ru.komrakov.jsonMapper.MappingJSONParserClass;
import ru.nojs.json.JSONArray;
import ru.nojs.json.JSONElement;
import ru.nojs.json.Mapper;
import ru.nojs.json.MappingJsonParser;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;


public class MappingJsonParserTest {

    private static final MappingJsonParser mjp = new MappingJSONParserClass();

    /*
    static { // replace me
        when(mjp.parse(any(), any())).thenThrow(new NotImplementedException());
    }
    */

    @Test
    public void testSimpleMapper() {
        String json = "\"abcdef\"";
        Reader r = new StringReader(json);
        Mapper<String> stringMapper = JSONElement::getAsString;
        String result = mjp.parse(r, stringMapper);
        Assert.assertEquals("I shall not crash", "abcdef", result);
    }

    @Test
    public void testSimpleArrayMapper() {
        String json = "[1,2,3,4]";
        Reader r = new StringReader(json);
        Mapper<JSONArray> arrayMapper = JSONElement::getAsJsonArray;
        JSONArray result = mjp.parse(r, arrayMapper);
        result.forEach((arrayEntry) -> System.out.println(arrayEntry.getAsInt()));
        Assert.assertEquals("I shall not crash", "abcdef", result.toString());
    }

    @Test
    public void testMapMapper() {
        String json = "{\"a\": \"abcdef\", \"b\": \"bgedf\"}";
        Reader r = new StringReader(json);
        //Mapper<HashMap<String, String>> hashMapMapper = mock(Mapper.class, "replace me");
        //Mapper<HashMap<String, String>> hashMapMapper = new MapperClass();
        Mapper<HashMap<String, String>> hashMapMapper = new MapperClass();
        HashMap<String, String> result = mjp.parse(r, hashMapMapper);
        Assert.assertEquals("key 'a' bounded correctly", "abcdef", result.get("a"));
        Assert.assertEquals("key 'b' bounded correctly", "bgedf", result.get("b"));
    }

    @Test
    public void testHeterogeneousMapper() { // Is it even possible?!
        String json = "{\"a\": \"abcdef\", \"b\": 1234}";
        Reader r = new StringReader(json);
        //Mapper<HashMap<String, ?>> hashMapMapper = mock(Mapper.class, "replace me");
        Mapper<HashMap<String, String>> hashMapMapper = new MapperClass();
        HashMap<String, ?> result = mjp.parse(r, hashMapMapper);
        Assert.assertEquals("key 'a' bounded correctly", "abcdef", result.get("a"));
        Assert.assertEquals("key 'b' bounded correctly", 1234, result.get("b"));
    }

    @Test
    public void testPOJOMapper() {
        String json = "{\"name\": \"Connor McLeod\", \"age\": 4412}";
        Human ref = new Human().setAge(4412).setName("Connor McLeod");
        Reader r = new StringReader(json);
        //Mapper<Human> someBeanMapper = mock(Mapper.class, "replace me");
        Mapper<Human> someBeanMapper = new MapperClass();
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


