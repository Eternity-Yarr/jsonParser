package ru.nojs.json;

import java.io.Reader;

public interface MappingJsonParser {
    <T> T parse(Reader r, Mapper<T> mapper);

}
