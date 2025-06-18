package com.hayes.sec02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/*
    To discuss some of the subscribe overloaded methods
 */
public class Lec03MonoSubscribe {

	private static final Logger log = LoggerFactory.getLogger(Lec03MonoSubscribe.class);

	public static void main(String[] args) {
		var mono = Mono.just(1)
				.map(i -> i / 0);

		mono.subscribe(
				i -> log.info("received: {}", i),
				error -> log.error("error", error),
				() -> log.info("completed"),
				subscription -> subscription.request(1)
		);
	}
}
