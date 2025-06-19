package com.hayes.sec03;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

@Slf4j
public class Lec06Log {

	public static void main(String[] args) {
		// Basic Usage
		Flux.range(1, 3)
				.log()
				.subscribe(Util.subscriber("Basic log"));

		// Partial request demo
		Flux.range(1, 5)
				.log()
				.subscribe(new BaseSubscriber<Integer>() {
					@Override
					protected void hookOnNext(Integer value) {
						log.info("received: {}", value);
					}

					@Override
					protected void hookOnSubscribe(Subscription subscription) {
						subscription.request(3);
					}
				});

		// Changing log and map
		Flux.range(1, 3)
				.log("before-map")
				.map(i -> Util.faker().name().firstName())
				.log("after-map")
				.subscribe(Util.subscriber("WithName"));
	}

}
