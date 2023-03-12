package com.example.dynamicdatasource.configurations;

public class DatabaseContextHolder {
    private static final ThreadLocal<DataSourceType> CONTEXT = new ThreadLocal<>();

    public static void setContext(DataSourceType dataSourceType) {
        CONTEXT.set(dataSourceType);
    }

    public static DataSourceType getContext() {
        return CONTEXT.get();
    }

    public static void resetContext() {
        CONTEXT.set(DataSourceType.MASTER);
    }
}
