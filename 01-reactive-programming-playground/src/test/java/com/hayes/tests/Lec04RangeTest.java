package com.hayes.tests;

import java.util.ArrayList;

import com.hayes.common.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class Lec04RangeTest {
	private Flux<Integer> getItems() {
		return Flux.range(1, 50);
	}

	private Flux<Integer> getRandomItems() {
		return Flux.range(1, 50)
				.map(i -> Util.faker().random().nextInt(1, 100));
	}

	@Test
	public void expectNextCountTest() {
		StepVerifier.create(getItems())
				.expectNext(1, 2, 3)
				.expectNextCount(47)
				.verifyComplete();
	}

	@Test
	public void expectNextCountTest_complex() {
		StepVerifier.create(getItems())
				.expectNext(1, 2, 3)
				.expectNextCount(22)
				.expectNext(26, 27, 28)
				.expectNextCount(22)
				.verifyComplete();
	}

	@Test
	public void expectNextMatchesTest() {
		StepVerifier.create(getRandomItems())
				.expectNextMatches(i -> i > 0 && i <= 100) // checks that first item satisfies a condition
				.expectNextCount(49)
				.verifyComplete();
	}

	// Not in the lecture
	@Test
	public void expectNextMatchesTest_all() {
		StepVerifier.create(getRandomItems())
				.expectNextCount(0) // recording starts immediately
				.recordWith(ArrayList::new)
				.expectNextCount(50)
				.consumeRecordedWith(items -> {
					boolean allMatch = items.stream()
							.allMatch(i -> i > 0 && i <= 100);
					Assertions.assertTrue(allMatch, "All items should be between 0 (inclusive) and 100 (exclusive)");
				})
				.verifyComplete();
	}

	@Test
	public void thenConsumeWhileTest() {
		StepVerifier.create(getRandomItems())
				.thenConsumeWhile(i -> i > 0 && i <= 100)
				.verifyComplete();
	}
}