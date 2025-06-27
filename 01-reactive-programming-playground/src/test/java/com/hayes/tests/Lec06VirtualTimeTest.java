package com.hayes.tests;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class Lec06VirtualTimeTest {

	public Flux<Integer> getItems() {
		return Flux.range(1, 5)
				.delayElements(Duration.ofSeconds(10));
	}

	// Native test (slow)
	public void nativeTest_sample() {
		StepVerifier.create(getItems())
				.expectNext(1, 2, 3, 4, 5)
				.expectComplete()
				.verify();  // ⏳ Takes 50s in real time
	}

	@Test
	public void virtualTimeTest() {
		StepVerifier.withVirtualTime(this::getItems)
				.thenAwait(Duration.ofSeconds(51)) // Simulate 51s of time
				.expectNext(1, 2, 3, 4, 5)
				.verifyComplete();
	}

	// Failed
	public void virtualTimeTest_fail() {
		StepVerifier.withVirtualTime(this::getItems)
				.thenAwait(Duration.ofSeconds(30)) // Only simulates 30s
				.expectNext(1, 2, 3, 4, 5)
				.verifyComplete();
	}

	@Test
	public void virtualTimeTest_advance() {
		StepVerifier.withVirtualTime(this::getItems)
				.expectSubscription()                    // Must include explicitly for expectNoEvent
				.expectNoEvent(Duration.ofSeconds(9))    // No events before 10s
				.thenAwait(Duration.ofSeconds(1))        // Now total 10s
				.expectNext(1)                        // Item 1 arrives
				.thenAwait(Duration.ofSeconds(41))        // Wait for the rest
				.expectNext(2, 3, 4, 5)
				.verifyComplete();
	}

}