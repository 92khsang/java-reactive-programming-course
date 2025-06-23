package com.hayes.sec06;

import java.time.Duration;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/*
    - publish().autoConnect(0) will provide new values to the subscribers.
    - replay allows us to cache
 */
@Slf4j
public class Lec04HotPublisherCache {

	public static void main(String[] args) {
//		problem();
		solution();
	}


	private static void problem() {
		Flux<Integer> stock = stockStream()
				.publish()
				.autoConnect(0);

		runFlux(stock);
	}

	private static void solution() {
		Flux<Integer> stock = stockStream()
				.replay(1)
				.autoConnect(0);

		runFlux(stock);
	}

	private static void runFlux(Flux<Integer> flux) {

		Util.sleepSeconds(4);

		log.info("Sam part");
		flux
				.doFirst(() -> log.info("Sam joining"))
				.subscribe(Util.subscriber("Sam"));

		Util.sleepSeconds(4);

		log.info("Mike part");
		flux
				.doFirst(() -> log.info("Mike joining"))
				.subscribe(Util.subscriber("Mike"));

		Util.sleepSeconds(5);
	}

	private static Flux<Integer> stockStream() {
		Flux<Integer> flux = Flux.generate(
				sink -> {
					int randomValue = Util.faker().random().nextInt(10, 100);
					sink.next(randomValue);
				}
		);

		return flux
				.delayElements(Duration.ofSeconds(3))
				.doOnNext(price -> log.info("emitting price: {}", price));
	}
}