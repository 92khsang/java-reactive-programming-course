package com.hayes.sec08;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/*
    Reactor provides backpressure handling strategies
    - buffer
    - drop
    - latest
    - error
 */
@Slf4j
public class Lec05BackPressureStrategies {

	public static void main(String[] args) {
		bufferStrategy();
	}

	private static void bufferStrategy() {
		Flux<Integer> producer = createFlux();

		producer
				.onBackpressureBuffer()
				.limitRate(1)
				.publishOn(Schedulers.boundedElastic())
				.map(Lec05BackPressureStrategies::timeConsumingTask)
				.subscribe(Util.subscriber("BufferStrategy"));

		Util.sleepSeconds(10);
	}

	public static Flux<Integer> createFlux() {
		return Flux.create(sink -> {
					for (int i = 1; i <= 100 && !sink.isCancelled(); i++) {
						log.info("Generating {}", i);
						sink.next(i);
						Util.sleepMillis(20);
					}
					sink.complete();
				})
				.cast(Integer.class)
				.subscribeOn(Schedulers.parallel());
	}


	private static int timeConsumingTask(int i) {
		Util.sleepSeconds(1);
		return i;
	}

}