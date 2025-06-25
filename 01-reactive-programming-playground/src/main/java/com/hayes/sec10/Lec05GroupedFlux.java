package com.hayes.sec10;

import java.time.Duration;

import com.hayes.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;

public class Lec05GroupedFlux {

	public static void main(String[] args) {
//		flow(Flux.range(1, 10)); // default
//		flow(Flux.range(1, 10).map(i -> i * 2)); // even only
		flow(Flux.range(1, 10).map(i -> i * 2).startWith(1)); // only one odd
	}

	private static void flow(Flux<Integer> flux) {
		flux
				.delayElements(Duration.ofMillis(200))
				.groupBy(i -> i % 2) // OK: groupBy(Function<T, K>)
				.flatMap(Lec05GroupedFlux::processEvent)
				.subscribe();

		Util.sleepSeconds(3);
	}


	private static Mono<Void> processEvent(GroupedFlux<Integer, Integer> flux) {
		String subName = "Group " + (flux.key() == 0 ? "EVEN" : "ODD");

		return flux
				.transform(Util.fluxIntervalLogger(subName))
				.then();
	}

}