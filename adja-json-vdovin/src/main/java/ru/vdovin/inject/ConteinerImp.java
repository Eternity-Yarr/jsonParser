package ru.vdovin.inject;
import com.google.common.base.Preconditions;
import org.reflections.Reflections;
import ru.nojs.inject.Container;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

public class ConteinerImp implements Container {

    private static  ConcurrentHashMap<Class, Object> singletonInstances = new ConcurrentHashMap<>();

    @Override
    public <T> T getInstance(Class<T> clazz) {
        return (clazz.isAnnotationPresent(Singleton.class)) ? getSingleton(clazz) : createObj(clazz);
    }

    private <T> T getSingleton(Class<T> clazz) {
        return (T)singletonInstances.computeIfAbsent(clazz,(c) -> createObj(c));
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

        if (requiredType.isInterface()) {
            String packageDir = requiredType.isAnnotationPresent(ScanPackage.class)
                    ? requiredType.getAnnotation(ScanPackage.class).value() : requiredType.getPackage().getName();

            Reflections reflections = new Reflections(packageDir);
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
}
