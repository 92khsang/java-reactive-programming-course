package com.hayes.sec10;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.hayes.common.Util;
import com.hayes.sec10.assignment.window.FileWriter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Lec04WindowAssignment {

	public static void main(String[] args) {
		AtomicInteger index = new AtomicInteger(0);
		List<FileWriter> writerPool = new ArrayList<>();

		eventStream()
				.window(Duration.ofMillis(800))
				.flatMap(flux -> {
					FileWriter writer = FileWriter.of(index.getAndIncrement());
					writerPool.add(writer);
					return processEvent(flux, writer);
				})
				.subscribe(Util.subscriber());

		Util.sleepSeconds(10);

		for (FileWriter writer : writerPool) {
			System.out.println(writer.readAll());
			writer.delete();
			Util.printCutoffLIne();
		}

		writerPool.clear();
	}


	private static Flux<String> eventStream() {
		return Flux.interval(Duration.ofMillis(200))
				.map(i -> "event-" + i);
	}

	private static Mono<Void> processEvent(Flux<String> flux, FileWriter writer) {
		return flux
				.doOnNext(writer::write)
				.then();
	}
}