package ru.cleverhause.common.test.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TestJsonUtil {
    public static ObjectMapper TEST_OBJECT_MAPPER = new ObjectMapper();

    public static Map<String, Object> jsonToMap(String body) {
        try {
            return (Map) TEST_OBJECT_MAPPER.readValue(body, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException var2) {
            throw new AssertionError(var2);
        }
    }

    public static List<Map<String, Object>> jsonToList(String body) {
        try {
            return (List) TEST_OBJECT_MAPPER.readValue(body, new TypeReference<List<Map<String, Object>>>() {
            });
        } catch (IOException var2) {
            throw new AssertionError(var2);
        }
    }

    public static List<String> jsonToSimpleList(String body) {
        try {
            return (List) TEST_OBJECT_MAPPER.readValue(body, new TypeReference<List<String>>() {
            });
        } catch (IOException var2) {
            throw new AssertionError(var2);
        }
    }
}
