package ru.nojs.json;

import java.io.IOException;
import java.io.Reader;

public interface StreamingJsonParser {
    JSONElement parse(Reader r) throws IOException, Exception;
}