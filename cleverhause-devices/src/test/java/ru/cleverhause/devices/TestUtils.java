package ru.cleverhause.devices;

import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static String fromFile(String path) throws Exception {
        Path path2 = Paths.get(TestUtils.class.getClassLoader().getResource(path).toURI());
        return Files.readString(path2);
    }
}
