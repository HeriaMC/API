package fr.heriamc.api.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface AnnotatedFieldRetriever {

    default <F extends Annotation> List<String> getAnnotatedFields(Class<?> clazz, Class<? extends F> annotation) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(annotation))
                .map(Field::getName)
                .collect(Collectors.toList());
    }

}
