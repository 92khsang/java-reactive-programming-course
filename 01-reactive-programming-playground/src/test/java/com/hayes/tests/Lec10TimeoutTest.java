package com.hayes.tests;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class Lec10TimeoutTest {

	private Flux<Integer> getItems() {
		return Flux.range(1, 5)
				.delayElements(Duration.ofMillis(200));
	}

	@Test
	public void timeoutTest() {
		StepVerifier.create(getItems())
				.expectNext(1, 2, 3, 4, 5)
				.expectComplete()
				.verify(Duration.ofMillis(1500)); // ✅ Passes (duration > total emission time)
	}

	// @Test
	public void timeoutTest_fail() {
		StepVerifier.create(getItems())
				.expectNext(1, 2, 3, 4, 5)
				.expectComplete()
				.verify(Duration.ofMillis(500)); // ❌ Fails – producer needs ~1000ms
	}

}