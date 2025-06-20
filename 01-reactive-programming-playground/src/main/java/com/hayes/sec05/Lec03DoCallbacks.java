package com.hayes.sec05;

import java.util.function.Function;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/*
    do hooks/callbacks
 */
@Slf4j
public class Lec03DoCallbacks {

	public static void main(String[] args) {
		normalDemo();
		discardDemo();
		errorDemo();
	}

	private static void normalDemo() {
		Flux<String> flux = createFlux(0);

		flux.transform(doHooks(1))
				.transform(doHooks(2))
				.subscribe(Util.subscriber("ForNormal"));

		System.out.println("======================================");
	}

	private static void discardDemo() {
		Flux<String> flux = createFlux(0);

		flux
				.transform(doHooks(1))
				.take(2)
				.transform(doHooks(2))
				.take(4)
				.subscribe(Util.subscriber("ForDiscard"));

		System.out.println("======================================");
	}

	private static void errorDemo() {
		Flux<String> flux = createFlux(1);

		flux.transform(doHooks(1))
				.subscribe(Util.subscriber("ForError"));
	}

	private static Flux<String> createFlux(int select) {
		// doOnError is triggered only for errors that occur within the Publisher itself,
		// so unlike in the lecture, the test was modified to use a random number implementation
		return Flux.create(sink -> {
			log.info("Producer begins");
			switch (select) {
			case 0 -> {
				log.info("Encountered 0, emit 0-4");
				for (int j = 0; j < 5; j++) {
					log.info("Generated {}", j);
					sink.next(String.valueOf(j));
				}
			}
			case 1 -> sink.error(new RuntimeException("Encountered 1, throwing error"));
			}

			sink.complete();
			log.info("Producer ends");
		});
	}

	public static Function<Flux<String>, Flux<String>> doHooks(int index) {
		return flux -> flux
				.doFirst(() -> log.info("doFirst-{}", index))
				.doOnSubscribe(sub -> log.info("doOnSubscribe-{} {}", index, sub))
				.doOnRequest(requested -> log.info("doOnRequest-{} {}", index, requested))
				.doOnNext(next -> log.info("doOnNext-{} {}", index, next))
				.doOnComplete(() -> log.info("doOnComplete-{}", index))
				.doOnError(e -> log.error("doOnError-{}", index, e))
				.doOnTerminate(() -> log.info("doOnTerminate-{}", index))
				.doOnCancel(() -> log.info("doOnCancel-{}", index))
				.doOnDiscard(Object.class, o -> log.info("doOnDiscard-{} {}", index, o))
				.doFinally(signalType -> log.info("doFinally-{} {}", index, signalType));
	}

}
