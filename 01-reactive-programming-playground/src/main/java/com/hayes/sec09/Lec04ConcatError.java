package com.hayes.sec09;

import java.util.List;

import com.hayes.common.Util;
import com.hayes.sec09.helper.ProducerGenerator;
import reactor.core.publisher.Flux;

public class Lec04ConcatError {

	public static void main(String[] args) {
//		errorImmediately();
		errorDelayed();
	}

	private static void errorImmediately() {
		Flux.concat(producer1(), producer3(), producer2())
				.subscribe(Util.subscriber("errorImmediately"));

		Util.sleepSeconds(1);
	}

	private static void errorDelayed() {
		Flux.concatDelayError(producer1(), producer3(), producer2())
				.subscribe(Util.subscriber("errorDelayed"));

		Util.sleepSeconds(1);
	}


	private static Flux<Integer> producer1() {
		return ProducerGenerator.generate(1, List.of(1, 2, 3));
	}

	private static Flux<Integer> producer2() {
		return ProducerGenerator.generate(2, List.of(2, 3, 4));
	}

	private static Flux<Integer> producer3() {
		return ProducerGenerator.generate(3, new RuntimeException("Oops"));
	}

}