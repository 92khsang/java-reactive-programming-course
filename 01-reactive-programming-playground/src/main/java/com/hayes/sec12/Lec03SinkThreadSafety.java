package com.hayes.sec12;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Sinks;

@Slf4j
public class Lec03SinkThreadSafety {

	public static void main(String[] args) {
		threadUnsafe();
		threadSafe();
	}

	private static void threadUnsafe() {
		Sinks.Many<Integer> sink = Sinks.many().unicast().onBackpressureBuffer();
		List<Integer> list = new ArrayList<>();
		sink.asFlux().subscribe(list::add);

		for (int i = 0; i < 1000; i++) {
			int j = i;
			CompletableFuture.runAsync(() -> sink.tryEmitNext(j));
		}

		Util.sleepSeconds(2);
		log.info("Unsafe list size {}", list.size());

	}

	private static void threadSafe() {
		Sinks.Many<Integer> sink = Sinks.many().unicast().onBackpressureBuffer();
		List<Integer> list = new ArrayList<>();
		sink.asFlux().subscribe(list::add);

		for (int i = 0; i < 1000; i++) {
			int j = i;
			CompletableFuture.runAsync(() -> sink.emitNext(j, (signalType, emitResult) -> emitResult == Sinks.EmitResult.FAIL_NON_SERIALIZED));
		}

		Util.sleepSeconds(2);
		log.info("Safe list size {}", list.size());

	}

}