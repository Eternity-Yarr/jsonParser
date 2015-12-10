package ru.vdovin.inject;
import org.reflections.Reflections;
import ru.nojs.inject.Container;

import java.util.Map;
import java.util.concurrent.*;

import javax.inject.Singleton;

public class ContainerImp implements Container {

    private static  ConcurrentHashMap<Class, Object> singletonInstances = new ConcurrentHashMap<>();
    private Reflections reflections;


    public ContainerImp(String basePackage) {
        reflections = new Reflections(basePackage);
        reflections.getTypesAnnotatedWith(Eager.class).stream()
                .filter(c -> c.isAnnotationPresent(Singleton.class))
                .forEach(this::getInstance);
    }

    @Override
    public <T> T getInstance(Class<T> clazz) {
        ObjectFactory factory = new ObjectFactory(reflections, singletonInstances);
        return factory.getInstance(clazz);
    }

    @Override
    public <T> T getInstance(String name, Class<T> requiredType) {
        ObjectFactory factory = new ObjectFactory(reflections, singletonInstances);
        return factory.getInstance(name,requiredType);
    }

    @Override
    public Object getInstance(String name) {
        return null;
    }

    public Map<Class, Object> getAllSingletonObj() {
        ConcurrentHashMap<Class, Object> singletonObj = new ConcurrentHashMap<>();
        singletonObj.putAll(singletonInstances);
        return singletonObj;
    }

}
