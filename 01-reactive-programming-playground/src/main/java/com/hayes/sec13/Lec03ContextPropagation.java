package com.hayes.sec13;

import java.time.Duration;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.context.Context;

@Slf4j
public class Lec03ContextPropagation {

	public static void main(String[] args) {
//		contextPropagation();
//		unsharedContext();
		unsharedContext_2();
	}

	private static void contextPropagation() {
		Mono<String> producer1 = sharedProducer(1, Schedulers.parallel());
		Mono<String> producer2 = sharedProducer(2, Schedulers.boundedElastic());

		Flux.merge(producer1, producer2)
				.contextWrite(Context.of("user", "Sam"))
				.blockLast(Duration.ofSeconds(2));
	}

	private static void unsharedContext() {
		Mono<String> producer1 = sharedProducer(1, Schedulers.parallel());
		Mono<String> producer2 = specificProducer(2, Schedulers.boundedElastic());

		Flux.merge(producer1, producer2)
				.contextWrite(Context.of("user", "Sam"))
				.blockLast(Duration.ofSeconds(2));
	}

	private static void unsharedContext_2() {
		Mono<String> producer1 = sharedProducer(1, Schedulers.parallel());
		Mono<String> producer2 = sharedProducer(2, Schedulers.boundedElastic())
				.contextWrite(ctx -> Context.empty());

		Flux.merge(producer1, producer2)
				.contextWrite(Context.of("user", "Sam"))
				.blockLast(Duration.ofSeconds(2));
	}

	private static Mono<String> sharedProducer(int index, Scheduler scheduler) {
		return producer(index)
				.subscribeOn(scheduler);
	}

	private static Mono<String> specificProducer(int index, Scheduler scheduler) {
		return producer(index)
				.contextWrite(ctx -> Context.empty())
				.subscribeOn(scheduler);
	}

	private static Mono<String> producer(int index) {
		return Mono.deferContextual(ctx -> {
			log.info("Producer {} sees: {}", index, ctx);
			return Mono.empty();
		});
	}
}