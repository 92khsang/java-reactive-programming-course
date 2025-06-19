package com.hayes.sec04.assignment;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

/*
    - do the work only when it is subscribed
    - do the work based on the demand
    - stop producing when subscriber cancels
    - produce only the requested items
    - file should be closed once done
 */
@Slf4j
public class FileReaderServiceImpl implements FileReaderService {

	@Override
	public Flux<String> read(Path path) {
		return Flux.generate(
				() -> createReader(path),
				this::readLine,
				this::closeReader
		);
	}

	private BufferedReader createReader(Path path) {
		try {
			return Files.newBufferedReader(path);
		}
		catch (IOException ex) {
			log.error("Failed to open file {}", path, ex);
			throw new RuntimeException(ex);
		}
	}

	private BufferedReader readLine(BufferedReader reader, SynchronousSink<String> sink) {
		try {
			String line = reader.readLine();
			if (Objects.nonNull(line)) {
				sink.next(line);
			}
			else {
				sink.complete();
			}
		}
		catch (IOException ex) {
			log.error("Failed to read from file", ex);
			sink.error(ex); // error signal is preferred over raw throw
		}
		return reader;
	}

	private void closeReader(BufferedReader reader) {
		try {
			log.info("Closing input stream");
			reader.close();
		}
		catch (IOException ex) {
			log.error("Failed to close input stream", ex);
			throw new RuntimeException(ex);
		}
	}
}