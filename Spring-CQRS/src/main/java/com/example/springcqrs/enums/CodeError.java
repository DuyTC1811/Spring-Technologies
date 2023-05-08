package com.example.springcqrs.enums;

public enum CodeError {
    SUCCESS(200, "Success"),
    NO_DATA(404, "No Data"),
    PAGE_003(405, "No");

    private final int code;
    private final String name;

    CodeError(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
