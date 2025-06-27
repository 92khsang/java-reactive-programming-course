package com.hayes.tests;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class Lec03FluxTest {

	private Flux<Integer> getItems() {
		return Flux.just(1, 2, 3)
				.log(); // observe stream lifecycle
	}

	@Test
	public void limitedRequestTest() {
		StepVerifier.create(getItems(), 1)
				.expectNext(1)
				.thenCancel()
				.verify();
	}

	@Test
	public void fullSequenceAndCompleteTest() {
		StepVerifier.create(getItems())
				.expectNext(1)
				.expectNext(2)
				.expectNext(3)
				.verifyComplete();

		StepVerifier.create(getItems())
				.expectNext(1, 2, 3)
				.verifyComplete();
	}

}