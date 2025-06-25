package com.hayes.sec09;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/*
    Calls multiple publishers in a specific order
 */
@Slf4j
public class Lec01StartWith {

	public static void main(String[] args) {
//		startWithT();
//		startWithIterableT();
//		startWithPublisher();
//		chaining();
		assignment();
	}

	private static void startWithT() {
		producer1()
				.startWith(-1, 0)
				.subscribe(Util.subscriber("startWithT"));

		Util.sleepSeconds(1);
	}

	private static void startWithIterableT() {
		producer1()
				.startWith(Arrays.asList(-2, -1, 0))
				.subscribe(Util.subscriber("startWithIterableT"));

		Util.sleepSeconds(1);
	}

	private static void startWithPublisher() {
		producer1()
				.log("To Producer 1")
				.startWith(producer2())
				.log("To Producer 2")
				.take(5)
				.subscribe(Util.subscriber("startWithPublisher"));

		Util.sleepSeconds(1);
	}

	private static void chaining() {
		producer1()
				.startWith(producer2())
				.startWith(1000)
				.subscribe(Util.subscriber("chaining"));

		Util.sleepSeconds(1);
	}

	// 49, 50, 51, 52, 53, 0, 1, 2, 3
	private static void assignment() {
		producer1()
				.startWith(0)
				.startWith(producer3())
				.startWith(List.of(49, 50))
				.subscribe(Util.subscriber("assignment"));

		Util.sleepSeconds(1);
	}

	private static Flux<Integer> producer1() {
		return Flux.just(1, 2, 3)
				.doOnSubscribe(__ -> log.info("subscribing to producer1"))
				.delayElements(Duration.ofMillis(10));
	}

	private static Flux<Integer> producer2() {
		return Flux.just(4, 5, 6)
				.doOnSubscribe(__ -> log.info("subscribing to producer2"))
				.delayElements(Duration.ofMillis(10));
	}

	private static Flux<Integer> producer3() {
		return Flux.just(51, 52, 53)
				.doOnSubscribe(__ -> log.info("subscribing to producer3"))
				.delayElements(Duration.ofMillis(10));
	}

}