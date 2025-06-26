package com.hayes.tests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Slf4j
public class Lec02EmptyErrorTest {

	Mono<String> getUsername(int userId) {
		return switch (userId) {
			case 1 -> Mono.just("Sam");
			case 2 -> Mono.empty(); // null
			default -> Mono.error(new IllegalArgumentException("invalid input"));
		};
	}


	@Test
	public void normalTest() {
		StepVerifier.create(getUsername(1))
				.expectNext("Sam")
				.expectComplete()
				.verify();
	}

	@Test
	public void emptyTest() {
		StepVerifier.create(getUsername(2))
				.expectComplete()
				.verify();
	}

	@Test
	public void errorTest() {
		StepVerifier.create(getUsername(3))
				.expectError(IllegalArgumentException.class)
				.verify();
	}

	@Test
	public void errorMessageTest() {
		StepVerifier.create(getUsername(3))
				// .expectError() ❌ Not allowed combining
				.expectErrorMessage("invalid input")
				.verify();
	}

	@Test
	public void consumeErrorWithTest() {
		StepVerifier.create(getUsername(3))
				.consumeErrorWith(ex -> {
					Assertions.assertInstanceOf(IllegalArgumentException.class, ex);
					Assertions.assertEquals("invalid input", ex.getMessage());
				})
				.verify();
	}

	@Test
	public void consumeNextWithTest() {
		StepVerifier.create(getUsername(1))
				.consumeNextWith(value -> Assertions.assertEquals("Sam", value))
				.expectComplete()
				.verify();
	}

}