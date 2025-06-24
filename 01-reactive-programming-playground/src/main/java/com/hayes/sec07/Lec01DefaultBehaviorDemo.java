package com.hayes.sec07;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/*
    By default, the current thread is doing all the work
 */
@Slf4j
public class Lec01DefaultBehaviorDemo {

	public static void main(String[] args) {
//		runInMainThread();
		runInMultiThread();
	}

	private static void runInMainThread() {
		var flux = simpleFlux();

		flux.subscribe(Util.subscriber("sub1"));
		flux.subscribe(Util.subscriber("sub2"));
	}

	private static void runInMultiThread() {
		var flux = simpleFlux();

		Runnable runnable = () -> flux.subscribe(Util.subscriber("sub1"));
		Thread.ofPlatform().start(runnable);
		flux.subscribe(Util.subscriber("sub2"));
	}

	private static Flux<Integer> simpleFlux() {
		Flux<Integer> flux = Flux.create(sink -> {
			for (int i = 1; i < 3; i++) {
				log.info("Generated: {}", i);
				sink.next(i);
			}
			sink.complete();
		});

		return flux.doOnNext(v -> log.info("value: {}", v));
	}

}