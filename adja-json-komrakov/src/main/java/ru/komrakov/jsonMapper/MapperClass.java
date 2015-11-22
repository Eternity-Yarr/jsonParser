package ru.komrakov.jsonMapper;

import ru.nojs.json.JSONElement;
import ru.nojs.json.Mapper;


public class MapperClass implements Mapper{



    @Override
    public Object map(JSONElement e) throws NoSuchFieldException, IllegalAccessException {
        return e.getAsString();
    }
    /*
    @Override
    public Object map(JSONElement e) throws NoSuchFieldException, IllegalAccessException {
        //return null;
        return e.getAsString();
    }*/
    
}
