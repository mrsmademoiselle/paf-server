package com.memorio.memorio.services.helper;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * In Klassen die nicht von Spring gemanaged werden, können wir kein Autowired benutzen.
 * Um dort dennoch Zugriff auf unsere Beans zu haben, wurde diese Klasse erstellt.
 * Sie holt sich über unseren ApplicationContext die gewünschte Bean und gibt sie zurück.
 */
@Service
public class BeanUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

}