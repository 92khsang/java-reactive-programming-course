package com.hayes.sec07;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class Lec02SubscribeOn {

	public static void main(String[] args) {
//		runInMainThread();
//		runInOtherThread();
		runInMultiThead();
	}

	private static void runInMainThread() {
		createFlux()
				.subscribe(data -> log.info("Subscriber received: {}", data));

		Util.sleepSeconds(1);
	}

	private static void runInOtherThread() {
		Flux<Integer> flux = createFlux();

		Runnable runnable = () -> flux.subscribe(Util.subscriber("RunInOtherThread"));
		Thread.ofPlatform().start(runnable);

		Util.sleepSeconds(1);
	}

	private static void runInMultiThead() {
		Flux<Integer> flux = createFlux();

		Runnable r1 = () -> flux.subscribe(Util.subscriber("sub1"));
		Runnable r2 = () -> flux.subscribe(Util.subscriber("sub2"));

		Thread.ofPlatform().start(r1);
		Thread.ofPlatform().start(r2);

		Util.sleepSeconds(1);
	}

	private static Flux<Integer> createFlux() {
		return Flux.<Integer>create(sink -> {
					log.info("Generating value: {}", 1);
					sink.next(1);
					sink.complete();
				})
				.doFirst(() -> log.info("First 1"))
				.log("Upward subscribeOn")
				.subscribeOn(Schedulers.boundedElastic()) // Switches thread *before* execution starts
				.doFirst(() -> log.info("First 2"))
				.log("Downward subscribeOn");
	}

}