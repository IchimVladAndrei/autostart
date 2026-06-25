package com.autodrive.common.config;

import io.github.cdimascio.dotenv.Dotenv;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class DotenvLoader {

    private DotenvLoader() {
    }

    public static void load(String serviceDirectory) {
        Path directory = resolveDotenvDirectory(serviceDirectory);
        Dotenv dotenv = Dotenv.configure()
                .directory(directory.toString())
                .ignoreIfMissing()
                .load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
    }

    private static Path resolveDotenvDirectory(String serviceDirectory) {
        return List
                .of(
                        Path.of(serviceDirectory),
                        Path.of("."),
                        Path.of(".."),
                        Path.of("autostart"),
                        Path.of("..", "autostart")
                )
                .stream()
                .filter(path -> Files.exists(path.resolve(".env")))
                .findFirst()
                .orElse(Path.of("."));
    }
}
