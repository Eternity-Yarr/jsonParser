package ru.vdovin.jsonParser;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Юыху on 25.11.2015.
 */
public class JsonParseReader {

    public JsonParseReader(Reader reader){
        this.reader=reader;
    }

    private Reader reader;

    private static final List<Character> ELEMENT_REMOVE = Arrays.asList('\n', '\r', '\t', ' ');

    private int element;

    public int getElement() {
        return element;
    }

    public void setElement(int element) {
        this.element = element;
    }

    public void nextElement(){
        try{
            setElement(this.reader.read());
        }catch (IOException e) {
            throw new IllegalStateException("Parse Error.", e);
        }
    }

    public void checkIsRemove(){
        while (ELEMENT_REMOVE.contains((char)getElement())){
            this.nextElement();
        }
    }
}
