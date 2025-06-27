package com.hayes.tests;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;
import reactor.util.context.Context;

public class Lec08ContextTest {

	public Mono<String> getWelcomeMessage() {
		return Mono.deferContextual(ctx -> {
			if (ctx.hasKey("user")) {
				return Mono.just("Welcome " + ctx.get("user"));
			}
			else {
				return Mono.error(new RuntimeException("unauthenticated"));
			}
		});
	}

	@Test
	public void contextInjectionTest() {
		StepVerifierOptions options = StepVerifierOptions.create()
				.withInitialContext(Context.of("user", "Sam"));

		StepVerifier.create(getWelcomeMessage(), options)
				.expectNext("Welcome Sam")
				.verifyComplete();
	}

	@Test
	public void contextInjectionTest_unauthenticated() {
		StepVerifier.create(getWelcomeMessage())
				.expectErrorMessage("unauthenticated")
				.verify();
	}
}