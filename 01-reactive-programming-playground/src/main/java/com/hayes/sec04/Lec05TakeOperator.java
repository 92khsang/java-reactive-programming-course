package com.hayes.sec04;

import com.hayes.common.Util;
import reactor.core.publisher.Flux;

/*
    Take is similar to java stream's limit
 */
public class Lec05TakeOperator {

	public static void main(String[] args) {
		takeN();
		takeWhile();
		takeUntil();
	}

	private static void takeN() {
		Flux.range(1, 10)
				.log("take")
				.take(3)
				.log("sub")
				.subscribe(Util.subscriber("takeN"));
	}

	private static void takeWhile() {
		Flux.range(1, 10)
				.log("take")
				.takeWhile(i -> i < 5)
				.log("sub")
				.subscribe(Util.subscriber("takeWhile"));
	}

	private static void takeUntil() {
		Flux.range(1, 10)
				.log("take")
				.takeUntil(i -> i == 5)
				.log("sub")
				.subscribe(Util.subscriber("takeUntil"));
	}

}
