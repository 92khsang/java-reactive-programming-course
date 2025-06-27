package com.hayes.tests;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;

public class Lec07ScenarioNameTest {

	private Flux<Integer> getItems() {
		return Flux.range(1, 3);
	}

	// @Test
	public void stepVerifierOptionsTest_fail() {
		StepVerifierOptions options = StepVerifierOptions.create()
				.scenarioName("123 item test");

		StepVerifier.create(getItems(), options)
				.expectNext(1)
				.expectNext(2)
				.verifyComplete();
	}

	// @Test
	public void detailStepLevelDescriptionTest_fail() {
		StepVerifierOptions options = StepVerifierOptions.create()
				.scenarioName("Detailed Step Test");

		StepVerifier.create(getItems(), options)
				.expectNext(11)
				.as("First item should be 11")
				.expectNext(2, 3)
				.as("Next two items should be 2, 3")
				.verifyComplete();
	}

	// @Test
	public void realisticUseCaseTest_fail() {
		StepVerifierOptions options = StepVerifierOptions.create()
				.scenarioName("Mismatched Final Items");

		StepVerifier.create(getItems(), options)
				.expectNext(1)
				.expectNext(2)
				.expectNext(4)
				.as("Expected last item to be 4")
				.verifyComplete();
	}
}