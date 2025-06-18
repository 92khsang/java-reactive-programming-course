package com.hayes.sec02;

import java.util.List;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/*
    To delay the publisher creation
 */
@Slf4j
public class Lec10MonoDefer {

	public static void main(String[] args) {
		Util.measureExecutionTime(log, "No-defer", () -> {
			createPublisher()
					//.subscribe(Util.subscriber())
			;
		});

		Util.measureExecutionTime(log, "Defer Without Sub", () -> {
			Mono.defer(Lec10MonoDefer::createPublisher);
		});

		Util.measureExecutionTime(log, "Defer With Sub", () -> {
			Mono.defer(Lec10MonoDefer::createPublisher)
					.subscribe(Util.subscriber());
		});

	}

	private static Mono<Integer> createPublisher() {
		log.info("Creating publisher");
		var numbers = List.of(1, 2, 3);
		Util.sleepSeconds(1);
		return Mono.fromSupplier(() -> sum(numbers));
	}

	// time-consuming business login
	private static int sum(List<Integer> numbers) {
		log.info("Computing sum of: {}", numbers);
		Util.sleepSeconds(3);
		return numbers.stream().mapToInt(a -> a).sum();
	}
}
