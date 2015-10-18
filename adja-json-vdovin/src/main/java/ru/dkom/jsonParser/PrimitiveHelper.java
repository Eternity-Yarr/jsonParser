package ru.dkom.jsonParser;

public class PrimitiveHelper {
    private Integer i;
    private Float f;
    private String s;


    public PrimitiveHelper(String s){
        try{
            i = Integer.parseInt(s);
        }catch (NumberFormatException e){
            this.s = s;
        };


    }

    public Integer getValue(){
        return i;
    }
}
