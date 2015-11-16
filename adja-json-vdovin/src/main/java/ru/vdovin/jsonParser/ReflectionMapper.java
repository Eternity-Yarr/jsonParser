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

        Constructor[] ctors  = targetType.getConstructors();
        try {

                    obj = (T)Stream.of(ctors)
                    .filter(ctor -> ctor.isAnnotationPresent(JSONCreator.class))
                    .findFirst()
                    .map(ctor -> createObjByCnstr(jo, ctor))
                    .orElseGet(() -> createObjByDefaultCnstr(jo, targetType));

        }
        catch (Exception e) {
            throw new IllegalArgumentException("Bean has no default or with \"JSONCreate\" annotation constructor ", e);
        }

        return obj;
    }

    private <T> T createObjByCnstr(JSONObject jo, Constructor<T> ctor) {
        try {
            Parameter[] ctorParams =  ctor.getParameters();
            Preconditions.checkArgument(
                    Stream.of(ctorParams).allMatch(f->f.isAnnotationPresent(JSONField.class)),
                    "All constructor parameters must have @JSONField annotation"
            );
            List<Object> param = new ArrayList<>(ctorParams.length);
            Stream.of(ctorParams)
                    .map(f -> f.getAnnotation(JSONField.class))
                    .forEach(field -> {
                            if (!jo.has(field.name()) && field.required()) {
                                throw new IllegalArgumentException("Missing required parameter " + field.name());
                            } else {
                                if (jo.has(field.name())) {
                                    MyJSONPrimitive jp = (MyJSONPrimitive) jo.get(field.name()).getAsJsonPrimitive();
                                    param.add(jp.getAsObject());
                                }
                                else {
                                    param.add(null);
                                }
                            }
                    });
            return ctor.newInstance(param.toArray());
        } catch (Exception e) {
            throw new IllegalArgumentException("Bean has no JSONCreator annotation or something bad happened", e);
        }
    }

    private <T> T createObjByDefaultCnstr(JSONObject jo, Class<T> targetType) {
        T obj;
        try {
            Constructor<T> ctor = targetType.getDeclaredConstructor();
            obj = ctor.newInstance();
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Bean has no default constructor");
        }


        jo
                .entrySet()
                .forEach(je -> {
                    String setterFieldName = setterNameOf(je.getKey());
                    try {
                        Method method = Stream.of(targetType.getMethods())
                                .filter(mtd -> mtd.getName().equals(setterFieldName))
                                .filter(mtd -> mtd.getParameterCount() == 1)
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("Can't find setter for " + je.getKey() + " or too many parameters in the method"));

                        Class parameterClass = method.getParameterTypes()[0];
                        if (parameterClass.isEnum()) {
                            Object enumValue = Stream.of(parameterClass.getEnumConstants())
                                    .filter(enumName -> enumName.toString().equals(je.getValue().getAsJsonPrimitive().getAsString()))
                                    .findFirst()
                                    .orElseThrow(() -> new IllegalArgumentException("Can't find enum value of " + je.getValue().getAsJsonPrimitive().getAsString()));
                            method.invoke(obj, enumValue);
                        } else if (parameterClass == Map.class) {
                            Map<Object, Object> mapValue = new HashMap<>();
                            JSONObject mjo = je.getValue().getAsJsonObject();
                            mjo
                                    .entrySet()
                                    .forEach(k -> {
                                        MyJSONPrimitive mjp = (MyJSONPrimitive) k.getValue().getAsJsonPrimitive();
                                        mapValue.put(k.getKey(), mjp.getAsObject());
                                    });
                            method.invoke(obj, mapValue);
                        } else if (parameterClass == BigDecimal.class) {
                            method.invoke(obj, je.getValue().getAsJsonPrimitive().getAsBigDecimal());
                        } else {
                            MyJSONPrimitive mjp = (MyJSONPrimitive) je.getValue().getAsJsonPrimitive();
                            method.invoke(obj, mjp.getAsObject());
                        }
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("Can't map field", e);
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
