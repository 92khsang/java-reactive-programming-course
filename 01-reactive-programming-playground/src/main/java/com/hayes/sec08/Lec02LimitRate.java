package com.hayes.sec08;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/*
    Reactor automatically handles the backpressure.
    We can also adjust the limit
 */
@Slf4j
public class Lec02LimitRate {
	public static void main(String[] args) {

		Flux<Integer> producer = Flux.generate(
						() -> 1,
						(state, sink) -> {
							log.info("Generating {}", state);
							sink.next(state);
							return ++state;
						}
				)
				.cast(Integer.class)
				.subscribeOn(Schedulers.parallel());

		producer
				.limitRate(5)
				.publishOn(Schedulers.boundedElastic())
				.map(Lec02LimitRate::timeConsumingTask)
				.subscribe(Util.subscriber());

		Util.sleepSeconds(60);
	}


	private static int timeConsumingTask(int i) {
		Util.sleepSeconds(1);
		return i;

	}
}