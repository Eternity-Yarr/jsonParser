package ru.vdovin.inject;
import com.google.common.base.Preconditions;
import ru.nojs.inject.Container;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

public class ConteinerImp implements Container {

    private static final String PACKAGEDIR = "ru/nojs/inject/"; //for test
    private static  ConcurrentHashMap<Class, Object> singletonInstances = new ConcurrentHashMap<>();

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

        if (!singletonInstances.containsKey(clazz)) {
            synchronized (singletonInstances) {
                if (!singletonInstances.containsKey(clazz)) {
                    T obj = createObj(clazz);
                    singletonInstances.put(clazz, obj);
                    return obj;
                }
            }
        }
            return (T)singletonInstances.get(clazz);
    }

    private <T> T createObj(Class<T> clazz) {
        T obj ;
        Constructor<T>[] ctors = (Constructor<T>[]) clazz.getConstructors();
        Constructor<T> ctor;

        switch (ctors.length) {
            case 0: { throw new IllegalStateException("Can't find constructor"); }
            case 1: {
                ctor = ctors[0];
                break;
            }
            default: {
                ctor = Stream.of(ctors)
                        .filter(constructor -> constructor.isAnnotationPresent(Inject.class))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Constructor mast have @Inject annotation"));
            }
        }

        try {
            if (ctor.getParameterCount() == 0) {
                    obj = ctor.newInstance();
            } else {
                Parameter[] ctorParams = ctor.getParameters();
                List<Object> params = new ArrayList<>(ctorParams.length);
                Stream.of(ctorParams)
                        .forEach(p -> {
                            if (p.isAnnotationPresent(Named.class)){
                                params.add(getInstance(p.getAnnotation(Named.class).value(), p.getType()));
                            }
                            else {
                                params.add(getInstance(p.getType()));
                            }
                        });

                obj = ctor.newInstance(params.toArray());
            }
        } catch (Exception e) {
            throw new IllegalStateException("Can't create instance", e);
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
