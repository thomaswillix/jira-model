package com.example.functions;

import java.util.Objects;

public class Validation {
    public static void requireNonNull(Object object, String field){
        Objects.requireNonNull(object, field + " can't be null.");
    }

    public static void requireNotBlank(String string, String field){
        requireNonNull(string, field);
        if (string.isBlank()) throw new IllegalArgumentException( field + " can't be blank.");
    }

    public static String requireValidString(String string, String field){
        requireNotBlank(string, field);
        return string;
    }
}
