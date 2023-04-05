package com.example.dynamicdatasource.configurations;

import com.example.dynamicdatasource.anotions.WithDatabase;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class DataSourceAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceAspect.class);

    @Around("@annotation(withDatabase)")
    public Object proceed(ProceedingJoinPoint proceedingJoinPoint, WithDatabase withDatabase) {
        try {
            DatabaseContextHolder.setContext(withDatabase.value());
            LOGGER.info("Aspect executed {}", withDatabase.value());
            return proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            LOGGER.error(" Aspect Error");
            return new Object();
        }
    }
}
