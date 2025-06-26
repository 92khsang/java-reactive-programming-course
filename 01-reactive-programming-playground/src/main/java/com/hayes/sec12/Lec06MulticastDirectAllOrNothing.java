package com.hayes.sec12;

import java.time.Duration;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Slf4j
public class Lec06MulticastDirectAllOrNothing {

	public static void main(String[] args) {
		System.setProperty("reactor.bufferSize.small", "16");

//		doNothing();
		doAll();
	}

	private static void doNothing() {
		Sinks.Many<String> sink = Sinks.many().multicast().directAllOrNothing();
		Flux<String> flux = sink.asFlux();

		flux.subscribe(Util.subscriber("Sam"));
		flux
				.delayElements(Duration.ofMillis(200))
				.subscribe(Util.subscriber("Mike"));

		tryEmitNext(sink);
	}

	private static void doAll() {
		Sinks.Many<String> sink = Sinks.many().multicast().directAllOrNothing();
		Flux<String> flux = sink.asFlux();

		flux.subscribe(Util.subscriber("Sam"));
		flux
				.onBackpressureBuffer()
				.delayElements(Duration.ofMillis(200))
				.subscribe(Util.subscriber("Mike"));

		tryEmitNext(sink);
	}

	private static void tryEmitNext(Sinks.Many<String> sink) {
		for (int i = 0; i < 32; i++) {
			Sinks.EmitResult result = sink.tryEmitNext(String.valueOf(i));
			if (!Sinks.EmitResult.OK.equals(result)) {
				log.info("Failed because of {} at {}", result, i);
			}
		}

		Util.sleepSeconds(8);
	}
}