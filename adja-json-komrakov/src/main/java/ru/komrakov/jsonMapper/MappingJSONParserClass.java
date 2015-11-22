package ru.komrakov.jsonMapper;

import ru.komrakov.jsonParser.ImplementedJsonParser;
import ru.nojs.json.JSONElement;
import ru.nojs.json.Mapper;
import ru.nojs.json.MappingJsonParser;
import ru.nojs.json.StreamingJsonParser;

import java.io.Reader;

public class MappingJSONParserClass implements MappingJsonParser{


    @Override
    public <T> T parse(Reader r, Mapper<T> mapper) {

        T result = null;
        JSONElement je = ((StreamingJsonParser)(new ImplementedJsonParser())::parse).parse(r);

        try {
            //result = (T)mapper.map(((StreamingJsonParser)(new ImplementedJsonParser())::parse).parse(r));
            result = (T)mapper.map(je);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return result;
    }
}
