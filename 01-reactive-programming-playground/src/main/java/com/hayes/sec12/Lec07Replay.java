package com.hayes.sec12;

import java.time.Duration;

import com.hayes.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class Lec07Replay {

	public static void main(String[] args) {
		doDemo(Sinks.many().replay().all());
		Util.printCutoffLIne();
		doDemo(Sinks.many().replay().limit(1));
		Util.printCutoffLIne();
		doDemo(Sinks.many().replay().limit(Duration.ofMillis(1)));
	}

	private static void doDemo(Sinks.Many<String> sink) {
		sink.tryEmitNext("hi");
		sink.tryEmitNext("hello");
		Util.sleepMillis(1);
		sink.tryEmitNext("world");
		sink.tryEmitNext("?");

		Flux<String> flux = sink.asFlux();
		flux.subscribe(Util.subscriber("Sam"));
		flux.subscribe(Util.subscriber("Mike"));
		flux.subscribe(Util.subscriber("Jake"));

		sink.tryEmitNext("bye");
		sink.tryEmitNext("!");

	}

}