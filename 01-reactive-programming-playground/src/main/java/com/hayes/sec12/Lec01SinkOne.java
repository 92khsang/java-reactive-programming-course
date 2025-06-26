package com.hayes.sec12;

import java.util.function.Consumer;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Slf4j
public class Lec01SinkOne {

	public static void main(String[] args) {
//		doDemo(sink -> {
//		}, "doNothing");
//		Util.printCutoffLIne();
//		doDemo(sink -> sink.tryEmitValue("Hello"), "tryEmitValue");
//		doDemo(Sinks.Empty::tryEmitEmpty, "tryEmitEmpty");
//		doDemo(sink -> sink.tryEmitError(new RuntimeException("Oops")), "tryEmitError");
//		multiSub();

		emitValue();

	}

	private static void doDemo(Consumer<Sinks.One<String>> doSomething, String subName) {
		// Create a Sink that can emit one value
		Sinks.One<String> sink = Sinks.one();

		doSomething.accept(sink);

		// Turn the Sink into a Mono
		Mono<String> mono = sink.asMono();
		// Subscribe to it
		mono.subscribe(Util.subscriber(subName));
	}

	private static void multiSub() {
		// Create a Sink that can emit one value
		Sinks.One<String> sink = Sinks.one();

		Sinks.EmitResult result = sink.tryEmitValue("Multi Before");
		log.info("[First] EmitResult {}", result);

		// Turn the Sink into a Mono
		Mono<String> mono = sink.asMono();

		// Subscribe to it
		mono.subscribe(Util.subscriber("Multi-1"));
		mono.subscribe(Util.subscriber("Multi-2"));

		result = sink.tryEmitValue("Multi After");
		log.info("[Second] EmitResult {}", result);
	}

	private static void emitValue() {
		Sinks.One<String> sink = Sinks.one();

		sink.asMono().subscribe(Util.subscriber("emitValue "));

		sink.emitValue("hi", ((signalType, emitResult) -> {
			log.info("Hi, Signal: {}, Result: {}", signalType, emitResult);
			return false;
		}));

		sink.emitValue("hello", ((signalType, emitResult) -> {
			log.info("Hello, Signal: {}, Result: {}", signalType, emitResult);
			return false; // Retry or Not
		}));
	}

}