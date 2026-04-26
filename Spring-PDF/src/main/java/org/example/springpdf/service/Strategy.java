package org.example.springpdf.service;

public interface Strategy<T, R> {
    String type();

    R execute(T input);
}
