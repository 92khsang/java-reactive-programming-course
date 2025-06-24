package com.hayes.sec07;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/*
    publish on for downstream!
 */
@Slf4j
public class Lec05PublishOn {
	public static void main(String[] args) {
		publishOn();
		Util.printCutoffLIne();
		chainedPublishOn();
	}


	private static void publishOn() {
		Flux<Integer> flux = createFlux();

		flux
				.doFirst(() -> log.info("Before publishOn")) // still current thread
				.doOnNext((value) -> log.info("Publish Thread {}", value)) // still current thread
				.publishOn(Schedulers.boundedElastic()) // switch happens here
				.doFirst(() -> log.info("After publishOn"))
				.subscribe(Util.subscriber("publishOn"));

		Util.sleepSeconds(1);
	}

	private static void chainedPublishOn() {
		Flux<Integer> flux = createFlux();

		flux
				.doFirst(() -> log.info("Before Parallel"))
				.doOnNext((value) -> log.info("Publish Thread {}", value))
				.publishOn(Schedulers.parallel()) // switch happens here
				.doFirst(() -> log.info("Before Bounded"))
				.doOnNext((value) -> log.info("Parallel Thread {}", value))
				.publishOn(Schedulers.boundedElastic()) // switch happens here
				.doFirst(() -> log.info("After All"))
				.doOnNext((value) -> log.info("Bounded Thread {}", value))
				.subscribe(Util.subscriber("publishOn"));

		Util.sleepSeconds(1);
	}

	private static Flux<Integer> createFlux() {
		return Flux.<Integer>create(sink -> {
			log.info("Generating value: {}", 1);
			sink.next(1);
			sink.complete();
		});
	}
}