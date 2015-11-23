package ru.komrakov.jsonParser;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by User on 08.11.2015.
 */
public class StreamReader {

    public static int END_OF_STREAM = -1;
    public static String NO_MORE_SYMBOLS_TO_READ = "";

    //FIXME: DRY: сколько раз в этом проекте пришлось объявить { } и тп? :) все надо собрать в одну кучу.
    //FIXME: причем судя по всему токены лучше хранить в enum. (Android отдельная песня)
    public final static String JSON_OBJECT_START = "{";
    public final static String JSON_OBJECT_END = "}";
    public final static String JSON_OBJECT_SEPARATOR = ",";
    public final static String JSON_ARRAY_START = "[";
    public final static String JSON_ARRAY_END = "]";
    public final static String JSON_OBJECT_VALUE_START = ":";


    private static int READ_AHEAD_BUFFER_SIZE = 10;
    private Reader reader;

    //FIXME: в яве с большой буквы принято называть только типы/классы. Константы - большими буквами.
    private static Map<Character,String> CharEventMap = new HashMap<Character, String>();
    {
        CharEventMap.put('{',JSON_OBJECT_START);
        CharEventMap.put('}',JSON_OBJECT_END);
        CharEventMap.put(',',JSON_OBJECT_SEPARATOR);
        CharEventMap.put('[',JSON_ARRAY_START);
        CharEventMap.put(']',JSON_ARRAY_END);
        CharEventMap.put(':',JSON_OBJECT_VALUE_START);
    }

    public StreamReader(Reader r){
        this.reader = r;
    }

    public Integer[] readNext(){
        return buildSequence();
    }

    private Integer[] buildSequence(){
        List<Integer> codes = new ArrayList<>();
        int code = readCharCodeFromStream();
        markStreamPosition();

        //FIXME: лишние скобки
        if((isTerminal(code))){
            Integer[] a = {END_OF_STREAM};
            return a;
        }

        if (CharEventMap.get((char)code) != null){
            codes = new ArrayList<>();
            codes.add(code);
            return convertCodeSequenceToArray(codes);
        }

        while (!isTerminal(code)){
            codes.add(code);
            code = readCharCodeFromStream();
            if (CharEventMap.get((char)code) != null){
                restoreStreamPosition();
                return convertCodeSequenceToArray(codes);
            }
            markStreamPosition();
        }

        return convertCodeSequenceToArray(codes);
    }

    //FIXME: DRY: я это видел уже где-то!
    private Integer[] convertCodeSequenceToArray(List<Integer> codes) {
        Integer[] codesSeq = new Integer[codes.size()];
        for (int i = 0; i < codes.size(); i++) {
            codesSeq[i] = codes.get(i);
        }
        return codesSeq;

    }

    private Boolean isTerminal(Integer code){
        //FIXME: return code == END_OF_STREAM;
        Boolean isTerminal = false;
        if (code == END_OF_STREAM){
            isTerminal = true;
        }
        return isTerminal;
    }

    private void markStreamPosition(){
        try {
            if (reader != null){
                reader.mark(READ_AHEAD_BUFFER_SIZE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void restoreStreamPosition(){
        try {
            if (reader != null){
                reader.reset();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //FIXME: private char readCharFromStream() наверное избавил бы от кучи лишних кастов и прочих возможных ошибок
    private int readCharCodeFromStream() {
        int code = -1;
        try {
            if (reader != null){
                code = reader.read();
            }else{
                code = -1;
                return code;
            }
            if (code == -1){
                reader.close();
                reader = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return code;
    }
}
