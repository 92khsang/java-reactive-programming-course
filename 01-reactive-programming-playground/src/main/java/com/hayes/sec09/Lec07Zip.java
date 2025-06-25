package com.hayes.sec09;

import java.time.Duration;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/*
    Zip
    - we will subscribe to all the producers at the same time
    - all or nothing
    - all producers will have to emit an item
 */
@Slf4j
public class Lec07Zip {

	record Car(String frame, String engine, String tire) { }

	public static void main(String[] args) {
		Flux.zip(parts("Frame"), parts("Engine"), parts("Tire"))
				.map(tuple -> new Car(tuple.getT1(), tuple.getT2(), tuple.getT3()))
				.subscribe(Util.subscriber("Car"));

		Util.sleepSeconds(10);
	}

	private static Flux<String> parts(String partName) {
		int numbers = Util.faker().random().nextInt(1, 10);
		int durationMs = Util.faker().random().nextInt(20, 1000);
		log.info("Number of {}s: {} (Duration: {} ms)", partName, numbers, durationMs);

		return Flux.range(1, numbers)
				.delayElements(Duration.ofMillis(durationMs))
				.map(i -> partName + i)
				.transform(Util.fluxIntervalLogger(partName));
	}

}