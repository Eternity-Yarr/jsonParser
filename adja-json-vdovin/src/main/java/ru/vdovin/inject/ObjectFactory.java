package ru.vdovin.inject;

import com.google.common.base.Preconditions;
import org.reflections.Reflections;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class ObjectFactory {

    private List<Class> diList = new ArrayList<>();
    private Reflections reflections;
    private ConcurrentHashMap<Class, Object> singletonInstances;

    public ObjectFactory(Reflections reflections, ConcurrentHashMap singletonInstances) {
        this.reflections = reflections;
        this.singletonInstances = singletonInstances;
    }

    public <T> T getInstance(Class<T> clazz) {

        if (diList.contains(clazz)) {
            throw new IllegalStateException("Circul");
        }else {
            diList.add(clazz);
            if (clazz.isAnnotationPresent(Singleton.class)) {
                return (T)singletonInstances.computeIfAbsent(clazz,(c) -> createObj(c));
            }
            else {
                return createObj(clazz);
            }
        }
    }

    public <T> T getInstance(String name, Class<T> requiredType) {

        if (requiredType.isInterface()) {
            Class namedClass = reflections.getSubTypesOf(requiredType).stream()
                    .filter(subClass -> subClass.isAnnotationPresent(Named.class)
                            && subClass.getAnnotation(Named.class).value().equals(name))
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

    public Object getInstance(String name) {
        return null;
    }


    public  <T> T createObj(Class<T> clazz) {
        T obj ;
        Constructor<T>[] ctors = (Constructor<T>[]) clazz.getConstructors();

        switch (ctors.length) {
            case 0: { throw new IllegalStateException("Can't find constructor"); }
            case 1: {
                obj = createObj(ctors[0]);
                break;
            }
            default: {
                obj = Stream.of(ctors)
                        .filter(constructor -> constructor.isAnnotationPresent(Inject.class))
                        .findFirst()
                        .map(this::createObj)
                        .orElseThrow(() -> new IllegalArgumentException("Constructor mast have @Inject annotation"));
            }
        }

        return obj;
    }

    public  <T> T createObj(Constructor<T> ctor) {
        try {
            if (ctor.getParameterCount() == 0) {
                return ctor.newInstance();
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
                return ctor.newInstance(params.toArray());
            }
        } catch (Exception e) {
            throw new IllegalStateException("Can't create instance", e);
        }
    }



}
