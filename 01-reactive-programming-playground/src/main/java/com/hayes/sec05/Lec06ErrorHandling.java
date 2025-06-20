package com.hayes.sec05;

import com.hayes.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/*
    Demonstrates error handling using onErrorReturn in a reactive pipeline.
 */
public class Lec06ErrorHandling {

	public static void main(String[] args) {
		onErrorReturnExample();
	}

	private static void onErrorReturnExample() {
		Flux.range(1, 10)
				.map(Lec06ErrorHandling::processItem)
				.map(String::valueOf)
				// Order matters: match from top to bottom
				.onErrorReturn(RuntimeException.class, "RuntimeException")
				.onErrorReturn(ArithmeticException.class, "ArithmeticException")
				.subscribe(Util.subscriber("Demo01"));


		Flux.range(1, 10)
				.map(Lec06ErrorHandling::processItem)
				.map(String::valueOf)
				// Order matters: match from top to bottom
				.onErrorReturn(ArithmeticException.class, "ArithmeticException")
				.onErrorReturn(RuntimeException.class, "RuntimeException")
				.subscribe(Util.subscriber("Demo01"));

		Mono.just(1)
				.map(__ -> {
					throw new ArithmeticException("ArithmeticException");
				})
				.onErrorReturn(IllegalArgumentException.class, -1) // Pass this exception
				.onErrorReturn(-3) // Any other exception
				.subscribe(Util.subscriber("Demo01"));
	}

	private static int processItem(int i) {
		if (i == 5) {
			return i / 0; // Triggers ArithmeticException
		}
		return i;
	}
}
