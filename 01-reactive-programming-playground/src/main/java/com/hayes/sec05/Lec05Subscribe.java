package com.hayes.sec05;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class Lec05Subscribe {

	public static void main(String[] args) {
		Flux.range(1, 10)
				.doOnNext(i -> log.info("received: {}", i))
				.doOnComplete(() -> log.info("completed"))
				.doOnError(err -> log.error("error", err))
				.subscribe();

		Flux.range(1, 5)
				.subscribe(
						data -> log.info("Received: {}", data),      // onNext
						err -> log.error("Error: ", err),           // onError
						() -> log.info("Completed!")                // onComplete
				);

	}

}
