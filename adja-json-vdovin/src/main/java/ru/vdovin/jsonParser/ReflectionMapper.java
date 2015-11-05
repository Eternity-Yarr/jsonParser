package ru.vdovin.jsonParser;

import org.apache.commons.lang3.text.WordUtils;
import ru.nojs.json.JSONElement;
import ru.nojs.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.stream.Stream;


public class ReflectionMapper {
    public <T> T createObject(JSONElement je, Class<T> targetType) {
        if (je == null) throw new IllegalArgumentException("je null");
        if (!je.isJsonObject()) throw new IllegalArgumentException("je isn't object");

        JSONObject jo = je.getAsJsonObject();

        T obj;
        try {
            Constructor<T> cnstr = targetType.getDeclaredConstructor();
            obj = cnstr.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Bean has no default constructor");
        }

        jo
            .entrySet()
            .forEach(s -> {
                String setterFieldName = setterNameOf(s.getKey());
                try {
                    Method m = Stream.of(targetType.getMethods())
                            .filter(method -> method.getName().equals(setterFieldName))
                            .findFirst()
                            .orElseThrow(() -> new NoSuchMethodException("Can't map field " + s.getKey()));
                    MyJSONPrimitive mjp = (MyJSONPrimitive) s.getValue().getAsJsonPrimitive();
                    m.invoke(obj,mjp.getAsObject());
                } catch (Exception e) {
                    throw new RuntimeException("Some security violations, or I dunno", e);
                }
            });

        return obj;
    }

    /**
     * According to common sense JSON originated from JavaScript, the appropriate naming convention of keys
     * for JSON is and should be in camelCase regardless of what programming language the JSON was formed. (c) so
     * But nevertheless, if we encounter underscores in field_name, we try to convert it to fieldName
     */
    public String fieldNameOf(String jsonFieldName) {

        String name = WordUtils.capitalize(jsonFieldName, new char[]{'_'}).replaceAll("_", "");
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toLowerCase(name.charAt(0)));
        sb.append(name.substring(1));
        return sb.toString();
    }

    /**
     * @see ReflectionMapper#fieldNameOf(String)
     * but this method will also append get as prefix
     */
    public String getterNameOf(String jsonFieldName) {
        return fieldNameOf("get_" + jsonFieldName);
    }

    public String setterNameOf(String jsonFieldName) {
        return fieldNameOf("set_"+jsonFieldName);
    }
}
