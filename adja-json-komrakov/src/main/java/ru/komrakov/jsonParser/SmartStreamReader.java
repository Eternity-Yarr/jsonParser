package ru.komrakov.jsonParser;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class SmartStreamReader {

    private StreamReader streamReader;
    // FIXME: это на самом деле Set<Character>
    // Set<Character> INDENTATION_SYMBOLS = ImmutableSet.of(' ', '\t','\n','\r');
    private final static Character[] JSON_MEANINGLESS_SYMBOLS = {' ','\r','\n','\t'};
    //FIXME: здесь и далее везде - без нужды нет необходимости использовать boxed версии примитивов
    //FIXME: Screening? :) ESCAPE_SYMBOL_CODE
    //FIXME: не понимаю зачем вообще надо было использовать коды символов, а не сами символы.
    private final static Integer SCREENING_SYMBOL_CODE = 92;
    private final static Integer QUOTES_SYMBOL_CODE = 34;

    public SmartStreamReader(Reader reader){
        this.streamReader = new StreamReader(reader);
    }

    public String readNext() throws IllegalArgumentException{
        Integer[] chunk = streamReader.readNext();
        //FIXME: была же зачем-то константа END_OF_STREAM, раз уж завел :)
        if (chunk[0] == -1){
            return "";
        }
        chunk = removeInsignificantSymbols(chunk);
        chunk = removeScreeningChar(chunk);

        String value = convertCodeSequenceToString(chunk);
        if (value.equals(":")){
            value = readNext();
        }

        //FIXME: value.isEmpty()
        if (value.equals("")){
            value = readNext();
        }

        return value;
    }

    //FIXME: на такие вещи надо писать юнит-тесты :(
    private Integer[] removeInsignificantSymbols(Integer[] chunk){
        List<Integer> result = new ArrayList<>();
        boolean remove = true;
        for (int probe: chunk){

            if (probe == QUOTES_SYMBOL_CODE){
                if (result.size() == 0){
                    remove = !remove;
                }
                //FIXME: скорее всего это баг. Чаще всего new Integer(1) != new Integer(1)
                //FIXME: сравнение двух объектов делается методом Object::equals()
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
        //FIXME: \" ?
        //get rid of /" combination
        List<Integer> result = new ArrayList<>();
        for (int i:chunk){
            if (i == QUOTES_SYMBOL_CODE){
                if (result.size() == 0){
                    result.add(i);
                    continue;
                }
                //FIXME: тоже самое, IDEA даже подсвечивает, мол, смотри, баг!
                if (result.get(result.size()-1) == SCREENING_SYMBOL_CODE) {
                    result.remove(result.size()-1);
                }
            }
            result.add(i);
        }
        return convertCodeSequenceToArray(result);

    }

    //
    private Integer[] convertCodeSequenceToArray(List<Integer> codes) {
        //FIXME:  return codes.toArray(new Integer[codes.size()]);
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
        //FIXME: Локаль дефолтная не всегда UTF-8, строка же у нас всегда UTF-8, JSON другим не бывает.
        return new String(codesSeq/*FIXME: UTF-8 */);
    }

    //FIXME: с Set<Character> эта функция не нужна, JSON_MEANINGLESS_SYMBOLS.contains()
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
