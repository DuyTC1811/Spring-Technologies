package com.example.dynamicdatasource.anotions;

import com.example.dynamicdatasource.configurations.DataSourceType;

import javax.transaction.Transactional;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Transactional
public @interface WithDatabase {
    DataSourceType value() default DataSourceType.MASTER;
}
