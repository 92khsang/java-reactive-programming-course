package com.hayes.common;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class Util {

	private static final Faker faker = Faker.instance();

	public static <T> Subscriber<T> subscriber() {
		return Util.subscriber("");
	}

	public static <T> Subscriber<T> subscriber(String name) {
		return new DefaultSubscriber<>(name);
	}

	public static Faker faker() {
		return faker;
	}

	public static void sleepSeconds(int seconds) {
		try {
			Thread.sleep(Duration.ofSeconds(seconds));
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static void sleepMillis(int ms) {
		try {
			Thread.sleep(Duration.ofMillis(ms));
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static void measureExecutionTime(Logger log, String label, Runnable runnable) {
		Instant start = Instant.now();
		log.info("[{}] started at {}", label, start);

		try {
			runnable.run();
		}
		finally {
			Instant end = Instant.now();
			log.info("[{}] finished at {}, duration: {} ms",
					label, end, Duration.between(start, end).toMillis());
		}
	}

	public static <T> T measureExecutionTime(Logger log, String label, Supplier<T> supplier) {
		Instant start = Instant.now();
		log.info("[{}] started at {}", label, start);

		try {
			return supplier.get();
		}
		finally {
			Instant end = Instant.now();
			log.info("[{}] finished at {}, duration: {} ms",
					label, end, Duration.between(start, end).toMillis());
		}
	}

	public static void printCutoffLIne() {
		System.out.println("============================================");
	}

	public static <T> UnaryOperator<Flux<T>> fluxLogger(String name) {
		return flux -> flux
				.doOnSubscribe(__ -> log.info("[{}] subscribing", name))
				.doOnCancel(() -> log.info("[{}] cancelling", name))
				.doOnComplete(() -> log.info("[{}] completed", name));
	}

	public static <T> UnaryOperator<Mono<T>> monoLogger(String name) {
		return mono -> mono
				.doOnSubscribe(__ -> log.info("[{}] subscribing", name))
				.doOnCancel(() -> log.info("[{}] cancelling", name))
				.doOnSuccess(value -> log.info("[{}] completed with value: {}", name, value))
				.doOnError(e -> log.error("[{}] error: {}", name, e.toString()));
	}

	public static <T> UnaryOperator<Flux<T>> fluxIntervalLogger(String name) {
		return flux -> {
			final long[] lastTimestamp = {System.currentTimeMillis()};

			return flux
					.doOnNext(item -> {
						long now = System.currentTimeMillis();
						long interval = now - lastTimestamp[0];
						log.info("[{}] Emitted: {} (interval since last: {} ms)", name, item, interval);
						lastTimestamp[0] = now;
					})
					.transform(Util.fluxLogger(name));
		};
	}
}
