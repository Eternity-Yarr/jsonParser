package ru.vdovin.jsonParser;

import java.io.IOException;
import java.io.Reader;

public class JSONReader {

    private Reader reader;
    private int current;

    public JSONReader( Reader reader) {
        this.reader = reader;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public void read() {
        try {
            setCurrent(this.reader.read());
        } catch (IOException e) {
            throw new IllegalArgumentException("No can parse", e);
        }
    }
}
