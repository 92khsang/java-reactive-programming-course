package com.hayes.sec04;

import java.util.concurrent.atomic.AtomicInteger;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

/*
	Requirement: Emit at most 10 countries, or stop early if Canada is seen.
 */
@Slf4j
public class Lec08GenerateWithState {

	public static void main(String[] args) {
		// problem_stateless(); // Infinite loop
		// problem_thead_unsafe();
		solution();
	}

	// Each call to sink -> {} is stateless.
	private static void problem_stateless() {

		Flux.generate(sink -> {
			int counter = 0; // Always initialized
			String country = Util.faker().country().name();
			sink.next(country);
			counter++;  // 🚫 WRONG: Always 0 in each invocation
			if ("Canada".equalsIgnoreCase(country) || counter == 10) {
				sink.complete();
			}
		});
	}

	// Can be mutated externally, making it thread-unsafe and prone to bugs in shared environments
	private static void problem_thead_unsafe() {
		AtomicInteger counter = new AtomicInteger(0);

		Flux<String> flux = Flux.generate(sink -> {
			String country = Util.faker().country().name();
			sink.next(country);
			if ("Canada".equalsIgnoreCase(country) || counter.incrementAndGet() == 10) {
				sink.complete();
			}
		});

		flux.subscribe(Util.subscriber("Country loop"));
	}

	private static void solution() {
		Flux<String> flux = Flux.generate(
				() -> 0, // initial state: counter = 0
				(Integer counter, SynchronousSink<String> sink) -> {
					String country = Util.faker().country().name();
					sink.next(country);
					if ("Canada".equalsIgnoreCase(country) || counter == 9) {
						sink.complete();
					}
					return counter + 1; // pass updated state
				},
				(counter) -> log.info("Ended at {}", counter) // Optional
		);

		flux.subscribe(Util.subscriber("Country loop"));
	}

}
