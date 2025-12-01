package com.main.core;

import com.main.aspect.NoLogging;
import com.main.core.annotation.Label;
import com.main.core.entity.BaseEntityParent;
import com.main.core.repository.BaseRepositoryParent;
import com.main.entity.User;
import com.main.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Setup {

    private static ApplicationContext applicationContext;


    private static final Map<Class<?>, Field> labelFieldsMap = new ConcurrentHashMap<>();
    private static final Map<Class<?>, BaseRepositoryParent> repositoriesMap = new ConcurrentHashMap<>();
    private static boolean finish = true;

    void run() {
        finish = true;
    }

    public static HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (sra == null) {
            return null;
        }
        return sra.getRequest();
    }

    public static <Y extends BaseEntityParent, T extends BaseRepositoryParent<Y>> T getRepository(
            Class<? extends T> repositoryClass) {
        if (!repositoriesMap.containsKey(repositoryClass)) {
            repositoriesMap.put(repositoryClass,
                    applicationContext.getBean(repositoryClass));
        }
        return (T) repositoriesMap.get(repositoryClass);
    }

    public static User getUser(String phone) {
        return getRepository(UserRepository.class)
                .findByPhone(phone).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @NoLogging
    public static boolean isFinish() {
        return finish;
    }


    public static Field getLabelField(Class<?> targetClass) {

        Field cached = labelFieldsMap.get(targetClass);
        if (cached != null) {
            return cached;
        }
        Class<?> currentClass = targetClass;
        while (currentClass != null && currentClass != Object.class) {
            for (Field field : currentClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Label.class)) {
                    labelFieldsMap.put(targetClass, field);
                    return field;
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        return null;
    }
}
