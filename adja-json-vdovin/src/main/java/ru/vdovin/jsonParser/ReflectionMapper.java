package ru.vdovin.jsonParser;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.text.WordUtils;
import ru.nojs.json.JSONCreator;
import ru.nojs.json.JSONElement;
import ru.nojs.json.JSONField;
import ru.nojs.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;


public class ReflectionMapper {
    public <T> T createObject(JSONElement je, Class<T> targetType) {
        Preconditions.checkNotNull(je, "source json can't be null");
        Preconditions.checkArgument(je.isJsonObject(),"It isn't JSON object");
        JSONObject jo = je.getAsJsonObject();

        T obj;

        Constructor[] cnstrs  = targetType.getConstructors();
        try {
            obj = (T)Stream.of(cnstrs)
                    .filter(c -> c.isAnnotationPresent(JSONCreator.class))
                    .findFirst()
                    .map(cnst -> createObjByCnstr(jo, cnst))
                    .orElse(createObjByDefultCnstr(jo, targetType));
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Bean has no default or with \"JSONCreate\" annotation constructor ");
        }

        return obj;
    }

    private <T> T createObjByCnstr(JSONObject jo, Constructor<T> cnstr) {
        T obj;
        List<Object> param = new ArrayList<>();
        try {
            Parameter[] prm =  cnstr.getParameters();
            Stream.of(prm)
                    .filter(f -> f.isAnnotationPresent(JSONField.class))
                    .forEach(p -> {
                            if (!jo.has(p.getAnnotation(JSONField.class).name()) && p.getAnnotation(JSONField.class).required()) {
                                throw new IllegalArgumentException(" Can't find required parametr " + p.getAnnotation(JSONField.class).name());
                            }
                            else {
                                MyJSONPrimitive jp = (MyJSONPrimitive) jo.get(p.getAnnotation(JSONField.class).name()).getAsJsonPrimitive();
                                param.add(jp.getAsObject());
                            }
                    });
            obj = cnstr.newInstance(param);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Bean has no JSONCreator annotation");
        }
        return obj;
    }

    private <T> T createObjByDefultCnstr(JSONObject jo, Class<T> targetType) {
        T obj;
        try {
            Constructor<T> cnstr = targetType.getDeclaredConstructor();
            obj = (T)cnstr.newInstance();
        }
        catch (Exception e) {
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
                                .orElseThrow(() -> new IllegalArgumentException("Can't find setter for " + s.getKey()));
                        if (m.getParameterCount() != 1 ) {
                            throw new IllegalArgumentException("too many parameters in the method" + setterFieldName);
                        }
                        Class parameterClass = m.getParameterTypes()[0];
                        if (parameterClass.isEnum()) {
                            Object enumValue = Stream.of(parameterClass.getEnumConstants())
                                    .filter(e -> e.toString().equals(s.getValue().getAsJsonPrimitive().getAsString()))
                                    .findFirst()
                                    .orElseThrow( () -> new IllegalArgumentException("Can't find enum value of " + s.getValue().getAsJsonPrimitive().getAsString()));
                            m.invoke(obj, enumValue);
                        }
                        else if (parameterClass == Map.class){
                            Map<Object,Object> mapValue = new HashMap<>();
                            JSONObject mjo = s.getValue().getAsJsonObject();
                            mjo
                                    .entrySet()
                                    .forEach(k -> {
                                        MyJSONPrimitive mjp = (MyJSONPrimitive) k.getValue().getAsJsonPrimitive();
                                        mapValue.put(k.getKey(), mjp.getAsObject());
                                    });
                            m.invoke(obj, mapValue);
                        }
                        else if (parameterClass == BigDecimal.class){
                            m.invoke(obj, s.getValue().getAsJsonPrimitive().getAsBigDecimal());
                        }
                        else {
                            MyJSONPrimitive mjp = (MyJSONPrimitive)s.getValue().getAsJsonPrimitive();
                            m.invoke(obj, mjp.getAsObject());
                        }
                    }
                    catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("Can't map field");
                    }
                    catch (Exception e) {
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
