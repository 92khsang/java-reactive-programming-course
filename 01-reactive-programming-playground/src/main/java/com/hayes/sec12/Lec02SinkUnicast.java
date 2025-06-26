package com.hayes.sec12;

import java.util.function.Consumer;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/*
    We can emit multiple messages. but there will be only one subscriber.
 */
@Slf4j
public class Lec02SinkUnicast {

	public static void main(String[] args) {
		doDemo(sink -> {
		}, "doNothing");

		Util.printCutoffLIne();
		doDemo(sink -> {
			// Emitting values before subscriber joins
			sink.tryEmitNext("hi");
			sink.tryEmitNext("how are you?");
			sink.tryEmitNext("someone?");
		}, "tryEmitNext");

		Util.printCutoffLIne();
		doDemo(Sinks.Many::tryEmitComplete, "tryEmitComplete");

		Util.printCutoffLIne();
		doDemo(sink -> {
			sink.tryEmitError(new RuntimeException("Oops 1"));
			sink.tryEmitError(new RuntimeException("Oops 2"));
			sink.tryEmitError(new RuntimeException("Oops 3"));
		}, "tryEmitError");

		Util.printCutoffLIne();
		multiSub();
	}

	private static void doDemo(Consumer<Sinks.Many<String>> doSomething, String subName) {
		Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();

		doSomething.accept(sink);

		Flux<String> flux = sink.asFlux();
		flux.subscribe(Util.subscriber(subName));
	}

	private static void multiSub() {
		Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();

		for (int i = 1; i <= 3; i++) {
			Sinks.EmitResult result = sink.tryEmitNext("Multi Before " + i);
			log.info("[First] EmitResult-{} {}", i, result);
		}

		Flux<String> flux = sink.asFlux();

		flux.subscribe(Util.subscriber("Multi-1"));
		flux.subscribe(Util.subscriber("Multi-2"));

		for (int i = 1; i <= 3; i++) {
			Sinks.EmitResult result = sink.tryEmitNext("Multi After " + i);
			log.info("[Second] EmitResult-{} {}", i, result);
		}
	}
}