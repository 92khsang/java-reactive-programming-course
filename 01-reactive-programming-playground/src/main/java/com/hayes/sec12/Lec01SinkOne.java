package com.hayes.sec12;

import java.util.function.Consumer;

import com.hayes.common.Util;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

public class Lec01SinkOne {

	public static void main(String[] args) {
		doDemo(sink -> {
		}, "doNothing");
		Util.printCutoffLIne();
		doDemo(sink -> sink.tryEmitValue("Hello"), "tryEmitValue");
		doDemo(Sinks.Empty::tryEmitEmpty, "tryEmitEmpty");
		doDemo(sink -> sink.tryEmitError(new RuntimeException("Oops")), "tryEmitError");
		multiSub();
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

		sink.tryEmitValue("Multi");

		// Turn the Sink into a Mono
		Mono<String> mono = sink.asMono();
		// Subscribe to it
		mono.subscribe(Util.subscriber("Multi-1"));
		mono.subscribe(Util.subscriber("Multi-2"));
	}

}