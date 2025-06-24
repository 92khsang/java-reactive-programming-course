package com.hayes.sec08;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class Lec04FluxCreate {

	public static void main(String[] args) {

		System.setProperty("reactor.bufferSize.small", "16");

		Flux<Integer> producer = Flux.create(sink -> {
					for (int i = 1; i <= 500 && !sink.isCancelled(); i++) {
						log.info("Generating {}", i);
						sink.next(i);
						Util.sleepMillis(50); // simulate 20 items/sec
					}
					sink.complete();
				})
				.cast(Integer.class)
				.subscribeOn(Schedulers.parallel());

		producer
//				.limitRate(1)
				.publishOn(Schedulers.boundedElastic())
				.map(Lec04FluxCreate::timeConsumingTask)
				.subscribe(Util.subscriber());

		Util.sleepSeconds(60);
	}


	private static int timeConsumingTask(int i) {
		Util.sleepSeconds(1);
		return i;
	}

}