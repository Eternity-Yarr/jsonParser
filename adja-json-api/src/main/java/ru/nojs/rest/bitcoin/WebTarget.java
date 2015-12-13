package ru.nojs.rest.bitcoin;

public interface WebTarget {
    <T> T proxy(Class<T> clazz);
}
