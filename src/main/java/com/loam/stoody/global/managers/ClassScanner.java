package com.loam.stoody.global.managers;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.web.context.support.StandardServletEnvironment;

public class ClassScanner {

    private static final Logger logger = LoggerFactory.getLogger(ClassScanner.class);

    public static Class<?>[] findAllAnnotatedClassesInPackage(String packageName,
                                                              Class<? extends Annotation> clazz) {
        final List<Class<?>> result = new LinkedList<Class<?>>();
        final ClassPathScanningCandidateComponentProvider provider =
                new ClassPathScanningCandidateComponentProvider(true, new StandardServletEnvironment());
        provider.addIncludeFilter(new AnnotationTypeFilter(clazz));
        for (BeanDefinition beanDefinition : provider.findCandidateComponents(packageName)) {
            try {
                result.add(Class.forName(beanDefinition.getBeanClassName()));
            } catch (ClassNotFoundException e) {
                logger.warn("Could not resolve class object for bean definition", e);
            }
        }
        return result.toArray(new Class<?>[result.size()]);
    }

}