package com.adaptive.mockserver.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Json {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T mapJsonToObject(String text, Class<T> target) {
        try {
            return objectMapper.readValue(text, target);
        } catch (JsonProcessingException e) {
            log.error(e.toString());
            return null;
        }
    }

    public static String mapObjectToJson(Object objectToMap) {
        try {
            return objectMapper.writeValueAsString(objectToMap);
        } catch (JsonProcessingException e) {
            log.error(e.toString());
            return "{}";
        }
    }
}
