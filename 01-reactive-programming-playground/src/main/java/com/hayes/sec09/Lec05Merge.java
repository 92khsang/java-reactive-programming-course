package com.hayes.sec09;

import java.util.List;

import com.hayes.common.Util;
import com.hayes.sec09.helper.ProducerGenerator;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/*
    Subscriber subscribes to all the producers at the same time.
 */
@Slf4j
public class Lec05Merge {

	public static void main(String[] args) {
		mergeWith();
		Util.printCutoffLIne();
		merge();
	}

	private static void mergeWith() {
		producer1()
				.mergeWith(producer2())
				.mergeWith(producer3())
				.log("mergeWith")
				.subscribe(Util.subscriber("mergeWith"));

		Util.sleepSeconds(1);
	}

	private static void merge() {
		Flux.merge(producer1(), producer2(), producer3())
				.log("merge")
				.take(2)
				.subscribe(Util.subscriber("merge"));

		Util.sleepSeconds(1);
	}

	private static Flux<Integer> producer1() {
		return ProducerGenerator.generate(1, List.of(1, 2, 3));
	}

	private static Flux<Integer> producer2() {
		return ProducerGenerator.generate(2, List.of(4, 5, 6));
	}

	private static Flux<Integer> producer3() {
		return ProducerGenerator.generate(3, List.of(7, 8, 9));
	}
}