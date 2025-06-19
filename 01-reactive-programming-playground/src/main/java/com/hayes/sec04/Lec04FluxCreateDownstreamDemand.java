package com.hayes.sec04;

import java.util.concurrent.atomic.AtomicInteger;

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
//		produceEarly();
		produceLazy();
	}

	private static void produceEarly() {
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

	private static void produceLazy() {
		Flux<String> flux = Flux.create(sink -> {
			AtomicInteger counter = new AtomicInteger(0);

			sink.onRequest(n -> {
				for (int i = 0; i < n && !sink.isCancelled(); i++) {
					String name = Util.faker().name().firstName();
					log.info("Generated: {}", name);
					sink.next(name);

					if (counter.incrementAndGet() >= 100) {
						sink.complete(); // optional: auto-complete after 100 items
						break;
					}
				}
			});
		});

		SubscriberImpl sub = new SubscriberImpl();
		flux.subscribe(sub);

		sub.getSubscription().request(2); // receive 2
		sub.getSubscription().request(2); // receive 2 more
		sub.getSubscription().cancel();  // no further data, even if you request again
		sub.getSubscription().request(2); // ❌ ignored (post-cancel)

	}

}
