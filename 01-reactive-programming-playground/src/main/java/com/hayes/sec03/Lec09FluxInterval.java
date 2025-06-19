package com.hayes.sec03;

import java.time.Duration;

import com.hayes.common.Util;
import reactor.core.publisher.Flux;

/*
    To emit a message based on the given interval
 */
public class Lec09FluxInterval {

	public static void main(String[] args) {
		// Emit a long value every 500ms
		Flux.interval(Duration.ofMillis(500))
				.subscribe(Util.subscriber("Interval"));

		// Emit random first names every 500ms
		Flux.interval(Duration.ofMillis(500))
				.map(__ -> Util.faker().name().firstName())
				.subscribe(Util.subscriber("Names"));

		// Prevent immediate exit by blocking main thread
		Util.sleepSeconds(5);
	}

}
