package com.hayes.sec05;

import com.hayes.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/*
    Similar to error handling.
    Handling empty!
 */
public class Lec07DefaultIfEmpty {

	public static void main(String[] args) {
		Mono.empty()
				.defaultIfEmpty(50)
				.subscribe(Util.subscriber());

		Util.printCutoffLIne();

		Mono.just(100)
				.defaultIfEmpty(50)
				.subscribe(Util.subscriber());

		Util.printCutoffLIne();

		Flux.range(1, 10)
				.filter(i -> i > 11)
				.defaultIfEmpty(999)
				.subscribe(Util.subscriber("Filtered All"));

		Flux.range(1, 10)
				.filter(i -> i > 9) // only 10 passes
				.defaultIfEmpty(999)
				.subscribe(Util.subscriber("Filtered Left"));

	}

}