package com.hayes.tests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/*
    To write a simple test using StepVerifier.
    StepVerifier acts like a subscriber.
 */
@Slf4j
public class Lec01MonoTest {
	public Mono<String> getProduct(int id) {
		return Mono.fromSupplier(() -> "Product " + id)
				.doFirst(() -> log.info("invoked"));
	}

	@Test
	public void productTest() {
		StepVerifier.create(getProduct(1))  // Provide the Mono publisher
				.expectNext("Product 1")     // Assert expected value
				.expectComplete()              // Assert Mono emits completion
				.verify();                     // Trigger the subscription and verification
	}

}