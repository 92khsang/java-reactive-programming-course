package com.hayes.common;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

import com.github.javafaker.Faker;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;

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
}
