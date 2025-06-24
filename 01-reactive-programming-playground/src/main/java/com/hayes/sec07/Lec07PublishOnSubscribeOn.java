package com.hayes.sec07;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/*
    publish on for downstream!
    subscribeOn for upstream
 */
@Slf4j
public class Lec07PublishOnSubscribeOn {

	public static void main(String[] args) {
		final Flux<Integer> flux = Flux.<Integer>create(sink -> {
					log.info("Generating value: {}", 1);
					sink.next(1);
					sink.complete();
				})
				.doFirst(() -> log.info("Bounded Elastic Thread"))
				.subscribeOn(Schedulers.boundedElastic())
				.publishOn(Schedulers.parallel())
				.doOnNext(__ -> log.info("Parallel Thread"))
				.doFirst(() -> log.info("Subscriber's Thread"));

		flux.subscribe(Util.subscriber());

		Util.sleepSeconds(1);
	}

}