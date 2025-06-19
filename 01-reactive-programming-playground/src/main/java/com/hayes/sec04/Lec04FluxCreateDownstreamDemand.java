package com.hayes.sec04;

import com.hayes.common.Util;
import com.hayes.sec01.subscriber.SubscriberImpl;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/*
    Flux create does NOT check the downstream demand by default! It is by design!
 */
@Slf4j
public class Lec04FluxCreateDownstreamDemand {

	public static void main(String[] args) {
		Flux<String> flux = Flux.create(sink -> {
			for (int i = 0; i < 10; i++) {
				String name = Util.faker().name().firstName();
				log.info("Generated: {}", name);
				sink.next(name);
			}
			sink.complete();
		});

		SubscriberImpl sub = new SubscriberImpl();
		flux.subscribe(sub);

		// Simulate delayed, partial demand
		Util.sleepSeconds(2);
		sub.getSubscription().request(2);
		Util.sleepSeconds(2);
		sub.getSubscription().request(2);
		sub.getSubscription().cancel();
		sub.getSubscription().request(2); // ❌ No effect after cancel

	}

}
