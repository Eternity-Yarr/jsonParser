package ru.dkom.jsonParser;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class SmartStreamReader {

    private StreamReader streamReader;
    private final static Character[] JSON_MEANINGLESS_SYMBOLS = {' ','\r','\n','\t'};
    private final static Integer SCREENING_SYMBOL_CODE = 92;
    private final static Integer QUOTES_SYMBOL_CODE = 34;

    public SmartStreamReader(Reader reader){
        this.streamReader = new StreamReader(reader);
    }

    public String readNext() throws IllegalArgumentException{
        Integer[] chunk = streamReader.readNext();
        if (chunk[0] == -1){
            return "";
        }

        //chunk = removeLeadingQuotes(chunk);
        chunk = removeInsignificantSymbols(chunk);
        //chunk = removeLeadingQuotes(chunk);
        //chunk = removeUnscreenedQuotes(chunk);
        chunk = removeScreeningChar(chunk);
        //chunk = removeLeadingQuotes(chunk);

        for (Integer i:chunk){
            //System.out.print(i);
            //System.out.print(" ");
        }
        //System.out.println();

        String value = convertCodeSequenceToString(chunk);
        if (value.equals(":")){
            value = readNext();
        }

        if (value.equals("")){
            value = readNext();
        }

        return value;
    }

    private Integer[] removeLeadingQuotes(Integer[] chunk){

        if (chunk.length == 0){
            return chunk;
        }

        int firstChar = chunk[0];
        int lastChar = chunk[chunk.length - 1];

        Integer[] noQoutes = chunk;
        if ((firstChar == QUOTES_SYMBOL_CODE)&&(lastChar == QUOTES_SYMBOL_CODE)){
            noQoutes = new Integer[chunk.length - 2];
            for (int i = 0; i < noQoutes.length; i++){
                noQoutes[i] = chunk[i+1];
            }
        }else{
            noQoutes = removeInsignificantSymbols(chunk);
            //noQoutes = removeLeadingQuotes(noQoutes);
        }
        return noQoutes;
    }

    private Integer[] removeUnscreenedQuotes(Integer[] chunk) throws IllegalArgumentException{
        List<Integer> result = new ArrayList<>();
        int quotesNumber = 0;
        for (int i:chunk){
            if (i == QUOTES_SYMBOL_CODE){
                if (result.size() == 0){
                    quotesNumber++;
                    continue;
                }
                if (result.get(result.size()-1) != SCREENING_SYMBOL_CODE) {
                    quotesNumber++;
                    continue;
                }
            }
            result.add(i);
        }

        if (quotesNumber > 0){
            //throw new IllegalArgumentException();
        }

        return convertCodeSequenceToArray(result);
    }

    private Integer[] removeInsignificantSymbols(Integer[] chunk){
        List<Integer> result = new ArrayList<>();
        boolean remove = true;
        for (int probe: chunk){

            if (probe == QUOTES_SYMBOL_CODE){
                if (result.size() == 0){
                    remove = !remove;
                }
                if ((result.size() != 0)&&(result.get(result.size()-1) != SCREENING_SYMBOL_CODE)){
                    remove = !remove;
                }
            }

            if (remove){
                if (!isMeaningless((char)probe)){
                    result.add(probe);
                }
            }else{
                result.add(probe);
            }

        }
        return convertCodeSequenceToArray(result);
    }

    private Integer[] removeScreeningChar(Integer[] chunk){
        //get rid of /" combination
        List<Integer> result = new ArrayList<>();
        for (int i:chunk){
            if (i == QUOTES_SYMBOL_CODE){
                if (result.size() == 0){
                    result.add(i);
                    continue;
                }
                if (result.get(result.size()-1) == SCREENING_SYMBOL_CODE) {
                    result.remove(result.size()-1);
                }
            }
            result.add(i);
        }
        return convertCodeSequenceToArray(result);

    }

    private Integer[] convertCodeSequenceToArray(List<Integer> codes) {
        Integer[] codesSeq = new Integer[codes.size()];
        for (int i = 0; i < codes.size(); i++) {
            codesSeq[i] = codes.get(i);
        }
        return codesSeq;

    }

    private String convertCodeSequenceToString(Integer[] codes){
        char[] codesSeq = new char[codes.length];
        for (int i = 0; i < codes.length; i++){
            codesSeq[i] = (char)(int)(codes[i]);
        }
        return new String(codesSeq);
    }

    private Boolean isMeaningless(Character symbol){
        Boolean checkResult = false;
        for (Character c:JSON_MEANINGLESS_SYMBOLS){
            if(c.equals(symbol)){
                checkResult = true;
                break;
            }
        }
        return checkResult;
    }


}
