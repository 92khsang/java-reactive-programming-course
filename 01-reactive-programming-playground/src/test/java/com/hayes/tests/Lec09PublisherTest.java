package com.hayes.tests;

import java.util.function.UnaryOperator;

import com.hayes.common.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.publisher.TestPublisher;

public class Lec09PublisherTest {

	private UnaryOperator<Flux<String>> processor() {
		return flux -> flux
				.filter(s -> s.length() > 1)
				.map(String::toUpperCase)
				.map(s -> s + ":" + s.length());
	}

	private static Flux<String> createTestFlux() {
		TestPublisher<String> publisher = TestPublisher.create();
		return publisher.flux();
	}

//	@Test
	public void incorrectApproach() {
		TestPublisher<String> publisher = TestPublisher.create();
		Flux<String> flux = publisher.flux();

//		flux.subscribe(Util.subscriber("Publisher Test")); // This is work...
//		publisher.next("hi", "hello");
//		publisher.complete();

		publisher.emit("hi", "hello"); // emit == next + complete

		StepVerifier.create(flux.transform(processor()))
				.expectNext("HI:2", "HELLO:5")
				.verifyComplete(); // This is failed... Flux is not a data structure
	}

	@Test
	public void publisherTest() {
		TestPublisher<String> publisher = TestPublisher.create();
		Flux<String> flux = publisher.flux();

		StepVerifier.create(flux.transform(processor()))
				.then(() -> publisher.emit("hi", "hello"))  // Emit after subscription
				.expectNext("HI:2", "HELLO:5")
				.verifyComplete();
	}

	@Test
	public void publisherTest_negative() {
		TestPublisher<String> publisher = TestPublisher.create();
		Flux<String> flux = publisher.flux();

		StepVerifier.create(flux.transform(processor()))
				.then(() -> publisher.emit("a", "b", "c")) // All fail filter (length = 1)
				.verifyComplete();
	}
}