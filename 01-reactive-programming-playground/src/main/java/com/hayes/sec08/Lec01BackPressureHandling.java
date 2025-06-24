package com.hayes.sec08;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/*
    Reactor automatically handles the backpressure.
    System.setProperty("reactor.bufferSize.small", "16");
 */
@Slf4j
public class Lec01BackPressureHandling {

	public static void main(String[] args) {

		System.setProperty("reactor.bufferSize.small", "16");

		Flux<Integer> producer = Flux.generate(
				() -> 1,
				(state, sink) -> {
					log.info("Generating {}", state);
					sink.next(state);
					return ++state;
				}
		);

		producer
				.publishOn(Schedulers.boundedElastic())
				.map(Lec01BackPressureHandling::timeConsumingTask)
				.subscribe(Util.subscriber());

		Util.sleepSeconds(60);
	}


	private static int timeConsumingTask(int i) {
		Util.sleepSeconds(1);
		return i;
	}

}