package com.github.ka.jobrunr.spring;

import org.jobrunr.utils.reflection.ReflectionUtils;
import org.springframework.util.MultiValueMap;

import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class QueryParams {

    public static <T> T parse(MultiValueMap<String, String> queryParams, Class<T> clazz) {
        Map<String, String> fieldValues = queryParams.entrySet().stream()
                .collect(toMap(Map.Entry::getKey, e -> e.getValue().get(0)));

        return ReflectionUtils.newInstanceAndSetFieldValues(clazz, fieldValues);
    }
}
