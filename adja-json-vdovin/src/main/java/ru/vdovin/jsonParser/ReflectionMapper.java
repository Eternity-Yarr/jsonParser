package ru.vdovin.jsonParser;

import ru.nojs.json.JSONElement;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ReflectionMapper {
    public <T> T createObject(JSONElement je, Class<T> targetType) {
        return null;
    }

    /**
     * According to common sense JSON originated from JavaScript, the appropriate naming convention of keys
     * for JSON is and should be in camelCase regardless of what programming language the JSON was formed. (c) so
     * But nevertheless, if we encounter underscores in field_name, we try to convert it to fieldName
     */
    String fieldNameOf(String jsonFieldName) {
        throw new NotImplementedException();
    }

    /**
     * @see ReflectionMapper#fieldNameOf(String)
     * but this method will also append get as prefix
     */
    String getterNameOf(String jsonFieldName) {
        throw new NotImplementedException();
    }
}
