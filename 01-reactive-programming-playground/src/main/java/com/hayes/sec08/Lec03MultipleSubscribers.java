package com.hayes.sec08;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class Lec03MultipleSubscribers {

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
				.map(Lec03MultipleSubscribers::timeConsumingTask)
				.subscribe(Util.subscriber("Slow"));


		producer
				.take(100)
				.publishOn(Schedulers.boundedElastic())
				.subscribe(Util.subscriber("Fast"));

		Util.sleepSeconds(1);
	}


	private static int timeConsumingTask(int i) {
		Util.sleepMillis(3);
		return i;

	}

}