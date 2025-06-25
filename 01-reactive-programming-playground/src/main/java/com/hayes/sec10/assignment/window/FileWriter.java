package com.hayes.sec10.assignment.window;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

// just for demo
@Slf4j
public class FileWriter {
	private final Path filePath;

	private FileWriter(Path filePath) {
		this.filePath = filePath;
	}

	public static FileWriter of(Integer index) {
		try {
			Path baseDir = Paths.get(
					System.getProperty("java.io.tmpdir"),
					"java-reactive-programming-course",
					"sec10"
			);

			if (!Files.exists(baseDir)) {
				Files.createDirectories(baseDir);
			}

			Path filePath = baseDir.resolve(String.format("log%d.txt", index));
			log.info("Creating file at {}", filePath);


			if (!Files.exists(filePath)) {
				Files.createFile(filePath);
			}
			return new FileWriter(filePath);
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new RuntimeException(ex);
		}
	}

	public void write(String content) {
		try {
			content += "\n";
			Files.writeString(this.filePath, content, StandardOpenOption.APPEND);
		}
		catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void delete() {
		try {
			Files.deleteIfExists(this.filePath);
		}
		catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public List<String> readAll() {
		try {
			return Files.readAllLines(this.filePath);
		}
		catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

}