package com.hayes.sec02.assignment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CompletableFuture;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class FileServiceImpl implements FileService {

	private static final Path BASE_DIR = Paths.get("src", "main", "resources", "sec02");

	@Override
	public Mono<String> read(String fileName) {
		return Mono.fromFuture(() -> CompletableFuture.supplyAsync(() -> {
			try {
				log.info("Reading file {}", fileName);
				Path fileFullPath = buildFilePath(fileName);
				return Files.readString(fileFullPath);
			}
			catch (IOException e) {
				throw new RuntimeException(e);
			}
		}));
	}

	@Override
	public Mono<Void> write(String fileName, String content) {
		return Mono.fromFuture(() -> CompletableFuture.supplyAsync(() -> {
			try {
				log.info("Writing file {}", fileName);
				Path fileFullPath = buildFilePath(fileName);
				Files.createDirectories(fileFullPath.getParent());
				Files.writeString(fileFullPath, content, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
				return null;
			}
			catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}));
	}

	@Override
	public Mono<Void> delete(String fileName) {
		return Mono.fromFuture(() -> CompletableFuture.supplyAsync(() -> {
			try {
				log.info("Deleting file {}", fileName);
				Path fileFullPath = buildFilePath(fileName);
				Files.deleteIfExists(fileFullPath);
				return null;
			}
			catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}));
	}


	private Path buildFilePath(String fileName) {
		return BASE_DIR.resolve(fileName);
	}
}
