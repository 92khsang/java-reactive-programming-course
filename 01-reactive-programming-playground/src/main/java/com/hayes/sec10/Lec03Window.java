package com.hayes.sec10;

import java.time.Duration;

import com.hayes.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Lec03Window {

	public static void main(String[] args) {
//		windowWithSize();
		windowWithDuration();
	}

	private static void windowWithSize() {
		eventStream()
				.window(5) // return Flux<Flux<String>>
				.flatMap(Lec03Window::processEvent)
				.subscribe(Util.subscriber("windowWithSize"));

		Util.sleepSeconds(10);
	}

	private static void windowWithDuration() {
		eventStream()
				.window(Duration.ofMillis(800)) // return Flux<Flux<String>>
				.flatMap(Lec03Window::processEvent)
				.subscribe(Util.subscriber("windowWithSize"));

		Util.sleepSeconds(10);
	}

	private static Flux<String> eventStream() {
		return Flux.interval(Duration.ofMillis(200))
				.map(i -> "event-" + i);
	}

	private static Mono<Void> processEvent(Flux<String> flux) {
		return flux.doOnNext(e -> System.out.print("*"))
				.doOnComplete(System.out::println)
				.then();
	}
}