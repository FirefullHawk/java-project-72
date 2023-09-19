package hexlet.code.utils;

import hexlet.code.App;

import java.io.File;
import java.nio.file.Files;
import java.util.stream.Collectors;
import java.io.IOException;

public class Helper {
    public static String getSql(String sourceFileName) throws IOException {
        var url = App.class.getClassLoader().getResource(sourceFileName);
        var file = new File(url.getFile());
        return Files.lines(file.toPath())
                .collect(Collectors.joining("\n"));
    }
}
