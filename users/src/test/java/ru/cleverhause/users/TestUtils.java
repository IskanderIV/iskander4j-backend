package ru.cleverhause.users;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestUtils {

    public static String fromFile(String path) throws Exception {
        Path path2File = Paths.get(TestUtils.class.getClassLoader().getResource(path).toURI());
        return Files.readString(path2File);
    }
}
