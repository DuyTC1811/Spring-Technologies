//package org.example.springpdf;
//
//import jakarta.annotation.PostConstruct;
//import org.example.springpdf.registrar.StrategyExecutorRegistrar;
//import org.example.springpdf.service.StrategyExecutor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.Map;
//
//@Configuration
//public class StrategyExecutorAutoConfig {
//    @Bean
//    public static StrategyExecutorRegistrar strategyExecutorRegistrar() {
//        return new StrategyExecutorRegistrar();
//    }
//
////    @Autowired
////    private ApplicationContext context;
////
////    @PostConstruct
////    void test() {
////        Map<String, StrategyExecutor> beans = context.getBeansOfType(StrategyExecutor.class);
////
////        beans.forEach((name, bean) -> System.out.println(name + " -> " + bean));
////    }
//}
