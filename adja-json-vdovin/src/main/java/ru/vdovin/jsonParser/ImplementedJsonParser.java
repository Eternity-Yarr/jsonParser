package ru.vdovin.jsonParser;

import ru.nojs.json.JSONElement;
import ru.nojs.json.StreamingJsonParser;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Reader;

public class ImplementedJsonParser implements StreamingJsonParser {
    public JSONElement parse(Reader r) {
        throw new NotImplementedException();
    }
}
