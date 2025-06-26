package com.hayes.sec13.client;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import reactor.core.publisher.Flux;
import reactor.util.context.ContextView;

// just for demo - could be a bean in real life
public class RateLimiterServer {

	private static final Map<String, Integer> categoryAttempts = Collections.synchronizedMap(new HashMap<>());

	static {
		refresh();
	}

	public static boolean canAllow(String category) {
		int attempts = categoryAttempts.getOrDefault(category, 0);
		return attempts > 0;
	}

	public static synchronized <T> Flux<T> limitCalls(Flux<T> flux, ContextView contextView) {
		Optional<String> category = contextView.getOrEmpty("category");
		if (category.isEmpty()) {
			return Flux.error(new IllegalArgumentException("No category provided"));
		}

		if (canAllow(category.get())) {
			int attempts = categoryAttempts.getOrDefault(category.get(), 0);
			categoryAttempts.put(category.get(), attempts - 1);
			return flux;
		}

		return Flux.error(new RuntimeException("Exceeded the given limit"));
	}

	private static void refresh() {
		Flux.interval(Duration.ofSeconds(5))
				.startWith(0L)
				.subscribe(__ -> {
					categoryAttempts.put("standard", 2);
					categoryAttempts.put("prime", 3);
				});
	}
}