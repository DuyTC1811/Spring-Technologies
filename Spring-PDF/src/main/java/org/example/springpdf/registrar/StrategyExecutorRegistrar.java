package org.example.springpdf.registrar;

import org.example.springpdf.service.Strategy;
import org.example.springpdf.service.StrategyExecutor;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.ResolvableType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@AutoConfiguration
public class StrategyExecutorRegistrar implements BeanDefinitionRegistryPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(StrategyExecutorRegistrar.class);

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) {
        // noop - phải đợi postProcessBeanFactory để có đủ thông tin
    }

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) {
        if (!(beanFactory instanceof BeanDefinitionRegistry registry)) {
            throw new IllegalStateException("BeanFactory is not a BeanDefinitionRegistry: " + beanFactory.getClass());
        }

        // B1. Quét tất cả Strategy bean, resolve generic
        Map<GenericPair, List<String>> pairToBeans = new LinkedHashMap<>();
        String[] beanNamesForType = beanFactory.getBeanNamesForType(Strategy.class, true, false);
        for (String beanName : beanNamesForType) {
            Class<?> beanClass = resolveType(beanFactory, beanName);
            if (beanClass == null) {
                throw new IllegalStateException("Cannot resolve type of bean '" + beanName + "'");
            }
            Class<?>[] args = GenericTypeResolver.resolveTypeArguments(beanClass, Strategy.class);
            if (args == null || args.length != 2 || args[0] == null || args[1] == null) {
                throw new IllegalStateException(
                        "Cannot resolve generics for " + beanClass.getName() + ". Declare generics explicitly: 'implements Strategy<MyRequest, MyResult>'.");
            }
            Class<?> input = args[0];
            Class<?> result = args[1];
            GenericPair genericPair = new GenericPair(input, result);

            if (!pairToBeans.containsKey(genericPair)) {
                pairToBeans.put(genericPair, new ArrayList<>());
            }
            pairToBeans.get(genericPair).add(beanName);
        }

        if (pairToBeans.isEmpty()) {
            log.warn("StrategyExecutorRegistrar: no Strategy beans found");
            return;
        }
        // B2. Check bean name collision trước khi register
        Map<String, GenericPair> usedNames = new HashMap<>();
        for (GenericPair pair : pairToBeans.keySet()) {
            String beanName = buildBeanName(pair);

            GenericPair clash = usedNames.putIfAbsent(beanName, pair);
            if (clash != null) {
                throw new IllegalStateException(
                        "Bean name collision for StrategyExecutor: '" + beanName +
                                "' would be used for both " + clash + " and " + pair);
            }
            if (registry.containsBeanDefinition(beanName)) {
                throw new IllegalStateException(
                        "Bean '" + beanName + "' already exists. " +
                                "Remove the manual @Bean or disable StrategyExecutorRegistrar.");
            }
        }

        // B3. Register bean với generic đầy đủ
        for (Map.Entry<GenericPair, List<String>> e : pairToBeans.entrySet()) {
            GenericPair pair = e.getKey();
            List<String> beanNames = List.copyOf(e.getValue());
            String executorBeanName = buildBeanName(pair);

            ResolvableType executorType = ResolvableType.forClassWithGenerics(StrategyExecutor.class, pair.input(), pair.result());

            RootBeanDefinition bd = new RootBeanDefinition(StrategyExecutor.class,
                    () -> buildExecutor(beanFactory, executorBeanName, pair, beanNames)
            );
            bd.setTargetType(executorType);
            bd.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
            registry.registerBeanDefinition(executorBeanName, bd);
            log.info("Registered {} [{}] with {} strategies: {}", executorBeanName, pair, beanNames.size(), beanNames);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static StrategyExecutor<?, ?> buildExecutor(
            ConfigurableListableBeanFactory beanFactory,
            String executorName,
            GenericPair pair,
            List<String> beanNames
    ) {
        List<Strategy> strategies = new ArrayList<>(beanNames.size());

        for (String beanName : beanNames) {
            Object bean = beanFactory.getBean(beanName);

            if (!(bean instanceof Strategy<?, ?> strategy)) {
                throw new IllegalStateException("Bean '%s' is not a Strategy".formatted(beanName));
            }
            // Re-validate sau proxy (ví dụ @Transactional wrap bean)
            Class<?> actualClass = AopProxyUtils.ultimateTargetClass(strategy);
            Class<?>[] args = GenericTypeResolver.resolveTypeArguments(actualClass, Strategy.class);

            boolean invalidGenericType = args == null
                    || args.length != 2
                    || !pair.input().equals(args[0])
                    || !pair.result().equals(args[1]);

            if (invalidGenericType) {
                throw new IllegalStateException(
                        "Bean '%s' generic mismatch after proxying. Expected %s, actual %s"
                                .formatted(beanName, pair, Arrays.toString(args))
                );
            }

            strategies.add(strategy);
        }

        return new StrategyExecutor<>(executorName, (List) strategies);
    }

    private static Class<?> resolveType(ConfigurableListableBeanFactory bf, String beanName) {
        try {
            return bf.getType(beanName, false);
        } catch (Exception ex) {
            return null;
        }
    }

    private static String buildBeanName(GenericPair pair) {
        String s = pair.input().getSimpleName();
        return Character.toLowerCase(s.charAt(0)) + s.substring(1) + "Executor";
    }

    private record GenericPair(Class<?> input, Class<?> result) {
    }
}
