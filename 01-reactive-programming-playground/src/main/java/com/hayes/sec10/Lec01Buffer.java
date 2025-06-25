package com.hayes.sec10;

import java.time.Duration;

import com.hayes.common.Util;
import reactor.core.publisher.Flux;

/*
    Collect items based on given internal / size
 */
public class Lec01Buffer {

	public static void main(String[] args) {
//		bufferWithSize();
//		bufferWithDuration();
//		commonPitfall();
		bufferTimeout();
	}

	private static void bufferWithSize() {
		Flux.range(1, 10)
				.log("Buffering")
				.buffer(3)
				.subscribe(Util.subscriber("bufferWithSize"));

		Util.sleepSeconds(1);
	}

	private static void bufferWithDuration() {
		Flux.interval(Duration.ofMillis(200))
				.map(i -> "event-" + i)
				.buffer(Duration.ofMillis(500))
				.subscribe(Util.subscriber("bufferWithDuration"));

		Util.sleepSeconds(2);
	}

	private static void commonPitfall() {
		Flux.just("A", "B")
				.concatWith(Flux.never())   // never emits or completes
				.log("Buffering")
				.buffer(3)                  // waits for 3 items
				.subscribe(Util.subscriber("commonPitfall")); // will **not emit**

		Util.sleepSeconds(1);
	}

	private static void bufferTimeout() {
		Flux.just("A", "B")
				.concatWith(Flux.never())
				.log("Buffering")
				.bufferTimeout(3, Duration.ofSeconds(1))
				.subscribe(Util.subscriber("bufferTimeout"));

		Util.sleepSeconds(2);
	}

}