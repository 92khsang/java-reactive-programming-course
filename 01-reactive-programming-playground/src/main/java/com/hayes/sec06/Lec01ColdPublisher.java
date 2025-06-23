package com.hayes.sec06;

import java.util.concurrent.atomic.AtomicInteger;

import com.hayes.common.Util;
import com.hayes.sec04.helper.NameGenerator;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class Lec01ColdPublisher {

	public static void main(String[] args) {
//		demo01();
		demo02();
	}

	private static void demo01() {
		AtomicInteger counter = new AtomicInteger(0);

		Flux<Integer> flux = Flux.create(sink -> {
			log.info("FluxSink invoked");
			for (int i = 0; i < 3; i++) {
				sink.next(counter.incrementAndGet());
			}
			sink.complete();
		});

		flux.subscribe(Util.subscriber("Sub1"));
		flux.subscribe(Util.subscriber("Sub2"));
	}


	private static void demo02() {
		NameGenerator nameGenerator = new NameGenerator();
		var flux = Flux.create(nameGenerator);

		flux
				.log("Sub1")
				.doFirst(() -> log.info("doFirst: Sub1"))
				.subscribe(Util.subscriber("Sub1"));

		flux
				.log("Sub2")
				.doFirst(() -> log.info("doFirst: Sub2"))
				.subscribe(Util.subscriber("Sub2"));

		for (int i = 0; i < 2; i++) {
			nameGenerator.generate();
		}
	}

}