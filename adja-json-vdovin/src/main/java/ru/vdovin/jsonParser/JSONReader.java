package ru.vdovin.jsonParser;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

public class JSONReader {

    private Reader reader;
    private int current;
    private final static List<Integer> INSIGNIFICANT_SYMBOLS = Arrays.asList(10, 13, 9, 32);
    // \n = 10
    // \r = 13
    // \t = 9
    // \" = 34
    // " " = 32
    public JSONReader( Reader reader) {
        this.reader = reader;
    }

    public int getCurrent() {
        return current;
    }

    private void setCurrent(int current) {
        this.current = current;
    }

    public void read() {
        try {
            setCurrent(this.reader.read());
        } catch (IOException e) {
            throw new IllegalArgumentException("No can parse", e);
        }
    }

    public void readInsignificantSymbols(){
        while ( INSIGNIFICANT_SYMBOLS.contains(getCurrent()) ){
            this.read();
        }
    }
}
