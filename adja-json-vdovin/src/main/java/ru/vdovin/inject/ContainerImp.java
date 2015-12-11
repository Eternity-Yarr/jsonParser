package ru.vdovin.inject;
import com.google.common.base.Preconditions;

import org.reflections.Reflections;
import ru.nojs.inject.Container;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class ContainerImp implements Container {

    private static  ConcurrentHashMap<Class, Object> singletonInstances = new ConcurrentHashMap<>();
    private Reflections reflections;
    private Digraph<Class> diGraph = new Digraph<>();

    public ContainerImp(String basePackage) {
        reflections = new Reflections(basePackage);
        reflections.getTypesAnnotatedWith(Eager.class).stream()
                .filter(c -> c.isAnnotationPresent(Singleton.class))
                .forEach(this::getInstance);
    }

    @Override
    public <T> T getInstance(Class<T> clazz) {
        return (clazz.isAnnotationPresent(Singleton.class)) ? getSingleton(clazz) : createObj(clazz);
    }

    @Override
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


    @Override
    public Object getInstance(String name) {
        return null;
    }


    private <T> T getSingleton(Class<T> clazz) {
        return (T)singletonInstances.computeIfAbsent(clazz,(c) -> createObj(c));
    }

    public  <T> T createObj(Class<T> clazz) {
        Constructor<T>[] ctors = (Constructor<T>[]) clazz.getConstructors();
        switch (ctors.length) {
            case 0: { throw new IllegalStateException("Can't find constructor"); }
            case 1: {
                return createObj(ctors[0]);
            }
            default: {
                return Stream.of(ctors)
                        .filter(constructor -> constructor.isAnnotationPresent(Inject.class))
                        .findFirst()
                        .map(this::createObj)
                        .orElseThrow(() -> new IllegalArgumentException("Constructor mast have @Inject annotation"));
            }
        }
    }

    public  <T> T createObj(Constructor<T> ctor) {
        diGraph.add(ctor.getDeclaringClass());
        try {
            if (ctor.getParameterCount() == 0) {
                return ctor.newInstance();
            } else {
                Parameter[] ctorParams = ctor.getParameters();
                List<Object> params = new ArrayList<>(ctorParams.length);
                Stream.of(ctorParams)
                        .forEach(p -> {
                            diGraph.add(p.getType());
                            if (diGraph.isCircular(ctor.getDeclaringClass(), p.getType())) {
                                throw new IllegalStateException("Circular dependency");
                            } else {
                                diGraph.add(ctor.getDeclaringClass(), p.getType());
                            }

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


    public Map<Class, Object> getAllSingletonObj() {
        ConcurrentHashMap<Class, Object> singletonObj = new ConcurrentHashMap<>();
        singletonObj.putAll(singletonInstances);
        return singletonObj;
    }

}
