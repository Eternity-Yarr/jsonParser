package ru.nojs.inject;

public interface Container {
    <T> T getInstance(Class<T> clazz);
    <T> T getInstance(String name, Class<T> requiredType);
    Object getInstance(String name);
}
