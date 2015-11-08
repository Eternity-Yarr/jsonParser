package ru.dkom.jsonParser;

import java.io.Reader;
import java.util.List;

/**
 * Created by User on 08.11.2015.
 */
public class SmartStreamReader {

    private StreamReader streamReader;

    public SmartStreamReader(Reader reader){
        this.streamReader = new StreamReader(reader);
    }

    public String readNext(){
        Integer[] chunk = streamReader.readNext();
        if (chunk[0] == -1){
            return "";
        }

        chunk = removeQuotes(chunk);

        for (Integer i:chunk){
            //System.out.print(i);
            //System.out.print(" ");
        }
        //System.out.println();

        String value = convertCodeSequenceToString(chunk);
        if (value.equals(":")){
            value = readNext();
        }

//        if (value.equals(",")){
//            value = readNext();
//        }

        return value;
    }

    private String convertCodeSequenceToString(Integer[] codes){
        char[] codesSeq = new char[codes.length];
        for (int i = 0; i < codes.length; i++){
            codesSeq[i] = (char)(int)(codes[i]);
        }
        return new String(codesSeq);
    }

    private Integer[] removeQuotes(Integer[] chunk){
        int firstChar = chunk[0];
        int lastChar = chunk[chunk.length - 1];

        Integer[] noQoutes = chunk;
        if ((firstChar == 34)&&(lastChar == 34)){
            noQoutes = new Integer[chunk.length - 2];
            for (int i = 0; i< noQoutes.length; i++){
                noQoutes[i] = chunk[i+1];
            }

        }
        return noQoutes;
    }


}
