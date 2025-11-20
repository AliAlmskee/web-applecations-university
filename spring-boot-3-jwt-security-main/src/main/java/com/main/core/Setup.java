package com.main.core;

import com.main.core.annotation.Label;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Setup {
    private static final Map<Class<?>, Field> labelFieldsMap = new ConcurrentHashMap<>();


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
