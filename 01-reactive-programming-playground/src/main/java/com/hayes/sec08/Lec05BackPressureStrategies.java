package com.hayes.sec08;

import java.util.function.UnaryOperator;

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
//		bufferStrategy();
//		errorStrategy();
		fixedSizeBufferStrategy();
	}

	private static void bufferStrategy() {
		Flux<Integer> producer = createFlux();

		producer
				.onBackpressureBuffer()
				.transform(appendSuffix())
				.subscribe(Util.subscriber("BufferStrategy"));

		Util.sleepSeconds(10);
	}

	private static void errorStrategy() {
		Flux<Integer> producer = createFlux();

		producer
				.log("Tag 1")
				.onBackpressureError()
				.log("Tag 2")
				.transform(appendSuffix())
				.subscribe(Util.subscriber("ErrorStrategy"));

		Util.sleepSeconds(10);
	}

	private static void fixedSizeBufferStrategy() {
		Flux<Integer> producer = createFlux();

		producer
				.onBackpressureBuffer(10)
				.transform(appendSuffix())
				.subscribe(Util.subscriber("FixedSizeBufferStrategy"));

		Util.sleepSeconds(20);
	}

	private static Flux<Integer> createFlux() {
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

	private static UnaryOperator<Flux<Integer>> appendSuffix() {
		return flux -> flux
				.limitRate(1)
				.publishOn(Schedulers.boundedElastic())
				.map(Lec05BackPressureStrategies::timeConsumingTask);
	}


	private static int timeConsumingTask(int i) {
		Util.sleepSeconds(1);
		return i;
	}

}