package ru.komrakov.jsonMapper;

import ru.komrakov.jsonParser.ImplementedJsonParser;
import ru.nojs.json.Mapper;
import ru.nojs.json.MappingJsonParser;
import ru.nojs.json.StreamingJsonParser;

import java.io.Reader;

public class MappingJSONParserClass implements MappingJsonParser{


    @FunctionalInterface //not mandatory
    interface Concatenator{
        String add(String s1, String s2);
    };

    class LambdaTest{
        public String useLambda(Concatenator lambda, String s1, String s2){
            return lambda.add(s1, s2);
        }
    }

    @Override
    public <T> T parse(Reader r, Mapper<T> mapper) {
        //some lambda fun
        Concatenator conc = (s1, s2) -> {return (s1 + s2);}; //general approach
        Concatenator conc2 = (s1, s2) -> (s1 + " 100_500 "  + s2); //can be used for single statement


        System.out.println(conc.add("hello", "world")); //helloworld
        System.out.println(conc2.add("hello", "world")); //hello100_500_world

        Concatenator s1 = conc::add;

        System.out.println(s1.add("bbb "," zzz")); // bbb  zzz

        Concatenator s2 = conc2::add;
        System.out.println(s2.add("zzz "," ttt")); //zzz 100_500 ttt

        //using lambda as argument
        LambdaTest c = new LambdaTest();
        System.out.println(c.useLambda(conc, "add ", "this")); //add this
        System.out.println(c.useLambda(conc2, "add ", "this")); // add 100_500 this


        T result = null;

        //StreamingJsonParser sjp = new ImplementedJsonParser()::parse;
        //JSONElement o = sjp.parse(r);

        //JSONElement o = new ImplementedJsonParser().parse(r);

        //StreamingJsonParser fff = (reader) -> (new ImplementedJsonParser().parse(reader)); //A
        //here reader = new LOCAL variable (type commanded by interface StreamingJsonParser)
        //StreamingJsonParser parser = (new ImplementedJsonParser())::parse; //equivalent of A. Use this when
        //lambda expression invokes an existing method, so you can use a method reference instead of a lambda expression:
        //https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html

        //JSONElement o = parser.parse(r);

        //Mapper s = mapper::map;

        try {
            //result = (T) s.map(o);
            //result = (T) mapper.map(o);
            //result = (T)(mapper.map((new ImplementedJsonParser()).parse(r)));
            //result = (T)mapper.map(fff.parse(r));
            //result = (T)mapper.map(parser.parse(r));
            result = (T)mapper.map(((StreamingJsonParser)(new ImplementedJsonParser())::parse).parse(r));
            //result = (T)mapper.map((StreamingJsonParser)((reader) ->((new ImplementedJsonParser().parse(reader)))).parse(r)); //not working. why????
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        return result;
    }
}
