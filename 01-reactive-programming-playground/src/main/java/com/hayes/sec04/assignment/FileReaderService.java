package com.hayes.sec04.assignment;

import java.nio.file.Path;

import reactor.core.publisher.Flux;

/*
    - do the work only when it is subscribed
    - do the work based on the demand
    - stop producing when subscriber cancels
    - produce only the requested items
    - file should be closed once done
 */
public interface FileReaderService {

	Flux<String> read(Path path);

}
