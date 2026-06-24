package com.autodrive.backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
@ConfigurationPropertiesScan
public class BackendApplication {

	public static void main(String[] args) {
		loadDotenv();
		SpringApplication.run(BackendApplication.class, args);
	}

	private static void loadDotenv() {
		Dotenv dotenv;
		if (Files.exists(Path.of(".env"))) {
			dotenv = Dotenv.configure()
					.ignoreIfMissing()
					.load();
		} else if (Files.exists(Path.of("autostart", ".env"))) {
			dotenv = Dotenv.configure()
					.directory("autostart")
					.ignoreIfMissing()
					.load();
		} else {
			dotenv = Dotenv.configure()
					.ignoreIfMissing()
					.load();
		}

		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
	}
}
