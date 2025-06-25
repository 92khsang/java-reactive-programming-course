package com.hayes.sec09;

import java.util.List;

import com.hayes.common.Util;
import com.hayes.sec09.helper.ProducerGenerator;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class Lec03ConcatWith {
	public static void main(String[] args) {
//		concatWithValues();
//		concatWithPublisher();
//		fluxConcat();
		chaining();
	}

	private static void concatWithValues() {
		producer1()
				.concatWithValues(-1, 0)
				.subscribe(Util.subscriber("concatWithValues"));

		Util.sleepSeconds(1);
	}

	private static void concatWithPublisher() {
		producer1()
				.log("To Producer 1")
				.concatWith(producer2())
				.log("To Producer 2")
				.take(5)
				.subscribe(Util.subscriber("concatWithPublisher"));

		Util.sleepSeconds(1);
	}

	private static void fluxConcat() {
		Flux.concat(producer1(), producer2())
				.subscribe(Util.subscriber("fluxConcat"));

		Util.sleepSeconds(1);
	}

	private static void chaining() {
		producer1()
				.concatWith(producer2())
				.concatWithValues(1000)
				.subscribe(Util.subscriber("chaining"));

		Util.sleepSeconds(1);
	}

	private static Flux<Integer> producer1() {
		return ProducerGenerator.generate(1, List.of(1, 2, 3));
	}


	private static Flux<Integer> producer2() {
		return ProducerGenerator.generate(3, List.of(51, 52, 53));
	}
}