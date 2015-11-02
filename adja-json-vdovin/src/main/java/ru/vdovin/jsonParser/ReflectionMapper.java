package ru.vdovin.jsonParser;

import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import ru.nojs.json.JSONElement;
import ru.nojs.json.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;



public class ReflectionMapper {
    public <T> T createObject(JSONElement je, Class<T> targetType) {
        if (je == null) throw new IllegalArgumentException("je null");
        if (!je.isJsonObject()) throw new IllegalArgumentException("je isn't object");

        JSONObject jo = je.getAsJsonObject();

       /* Constructor cnstr = null;
        try {
             cnstr = targetType.getDeclaredConstructor(targetType.getDeclaredConstructors()[0].getParameterTypes()[0]);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }*/

        Constructor cnstr = null;
        try {
            cnstr = targetType.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        T obj = null;

        try {
            obj = (T) cnstr.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


        final T finalObj = obj;
        jo
                .entrySet()
                .forEach( (s) -> {
                    String fieldName = s.getKey();
                    String setterFieldName = setterNameOf(fieldName);
                    Class[] paramTypes = null;

                    try {

                        for (Method m : targetType.getMethods()){
                            if (m.getName().equals(setterFieldName)) {
                                paramTypes = m.getParameterTypes();
                            }
                        }

                        MyJSONPrimitive mjp = (MyJSONPrimitive) s.getValue().getAsJsonPrimitive();
                        Method m = targetType.getMethod(setterFieldName, paramTypes);
                        m.invoke(finalObj,mjp.getAsObject());

                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                });


        return finalObj;
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
