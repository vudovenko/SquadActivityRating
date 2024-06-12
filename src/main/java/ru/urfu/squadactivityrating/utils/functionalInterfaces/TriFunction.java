package ru.urfu.squadactivityrating.utils.functionalInterfaces;

@FunctionalInterface
public interface TriFunction<T, U, V, R> {
    R apply(T t, U u, V v);
}
