package ru.cleverhause.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.InputStream;

public class JsonUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public JsonUtil() {
    }

    public static <T> T fromJsonFile(String path, Class<T> cls) throws Exception {
        return MAPPER.readValue(new File(JsonUtil.class.getResource(path).toURI()), cls);
    }

    public static <T> T fromInputStream(InputStream src, Class<T> cls) throws Exception {
        return MAPPER.readValue(src, cls);
    }

    public static <T> T fromString(String src, Class<T> cls) throws Exception {
        return MAPPER.readValue(src, cls);
    }

    public static String toJson(Object obj) throws Exception {
        return MAPPER.writeValueAsString(obj);
    }
}
