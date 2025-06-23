package com.hayes.sec05;

import com.hayes.common.Util;
import reactor.core.publisher.Flux;

/*
    Similar to error handling.
    Handling empty!
 */
public class Lec08SwitchIfEmpty {

	public static void main(String[] args) {
		Flux<Integer> fallback = Flux.range(100, 3);

		Flux.range(1, 10)
				.filter(i -> i > 10) // filters out all values → empty
				.switchIfEmpty(fallback)
				.subscribe(Util.subscriber());
	}
}