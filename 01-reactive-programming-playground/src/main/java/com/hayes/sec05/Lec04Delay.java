package com.hayes.sec05;

import java.time.Duration;

import com.hayes.common.Util;
import reactor.core.publisher.Flux;

public class Lec04Delay {

	public static void main(String[] args) {
		Flux.range(1, 10)
				.log()
				.delayElements(Duration.ofSeconds(1))
				.subscribe(Util.subscriber("Delay"));

		Util.sleepSeconds(12);
	}

}
