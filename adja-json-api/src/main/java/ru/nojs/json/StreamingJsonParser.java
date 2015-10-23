package ru.nojs.json;

import java.io.Reader;

public interface StreamingJsonParser {
    JSONElement parse(Reader r);
}