package ru.vdovin.inject;
import com.google.common.base.Preconditions;
import ru.nojs.inject.Container;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.inject.Named;
import javax.inject.Singleton;

public class ConteinerImp implements Container {

    private static final String PACKAGEDIR = "ru/nojs/inject/"; //for test
    private Map<Class, Object> singletonInstances = new HashMap<>();

    @Override
    public <T> T getInstance(Class<T> clazz) {

        T obj = null;
        if (clazz.isAnnotationPresent(Singleton.class)) {
            obj = getSingleton(clazz);
        }
        else {
            obj = createObj(clazz);
        }
        return obj;
    }


    private <T> T getSingleton(Class<T> clazz) {
        if (singletonInstances.containsKey(clazz)) {
            return (T)singletonInstances.get(clazz);
        }
        else {
            T obj = createObj(clazz);
            singletonInstances.put(clazz, obj);
            return obj;
        }
    }

    private <T> T createObj(Class<T> clazz) {
        T obj = null;
        try {
            Constructor<T> ctor =
                    Stream.of(clazz.getConstructors())
                            .findFirst()
                            .map(c -> (Constructor<T>) c)
                            .orElseThrow(() -> new IllegalArgumentException("Can't find constructor"));
            if (ctor.getParameterCount() == 0) {
                obj = ctor.newInstance();
            } else {
                Parameter[] ctorParams = ctor.getParameters();
                List<Object> params = new ArrayList<>(ctorParams.length);
                Stream.of(ctorParams)
                        .forEach(p -> {
                            params.add(getInstance(p.getType()));
                        });
                obj = ctor.newInstance(params.toArray());
                //throw new UnsupportedOperationException();
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Can't create instance", e);
        }
        return obj;
    }

    @Override
    public <T> T getInstance(String name, Class<T> requiredType) {

        List<Class> listClasses = new ArrayList<>();
        List<Class<T>> implClasses = new ArrayList<>();

        if (requiredType.isInterface()) {

            File dirClasses = new File(ClassLoader.getSystemClassLoader().getResource(PACKAGEDIR).getPath());
            String[] classes = dirClasses.list();

            String packDir = PACKAGEDIR.replace("/", ".");
            Stream.of(classes)
                    .forEach(c -> {
                        try {
                            listClasses.add(Class.forName(packDir + c.substring(0, c.length() - 6)));
                        } catch (ClassNotFoundException e) {
                            throw new IllegalStateException("test... cant class.forName", e);
                        }
                    });

            listClasses.forEach(c -> Stream.of(c.getInterfaces())
                    .forEach(f -> {
                        if (f == requiredType) {
                            implClasses.add(c);
                        }
                    }));

            Class namedClass = implClasses.stream()
                    .filter(f -> f.isAnnotationPresent(Named.class) && f.getAnnotation(Named.class).value().equals(name))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Beans must have @Named annotation and name = " + name));

            return (T)getInstance(namedClass);

        }
        else {

            Preconditions.checkArgument(
                    requiredType.isAnnotationPresent(Named.class),
                    "Beans must have @Named annotation");
            Preconditions.checkArgument(
                    requiredType.getAnnotation(Named.class).value().equals(name),
                    "Cant find name = " + name);

            return getInstance(requiredType);
        }
    }

    @Override
    public Object getInstance(String name) {
        return null;
    }
}
