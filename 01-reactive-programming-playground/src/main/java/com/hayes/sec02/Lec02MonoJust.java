package com.hayes.sec02;

import com.hayes.sec01.subscriber.SubscriberImpl;
import reactor.core.publisher.Mono;

/*
    Use just when the value to be emitted is already in the memory
 */
public class Lec02MonoJust {

	public static void main(String[] args) {
		Mono<String> mono = Mono.just("vins");
		var subscriber = new SubscriberImpl();
		mono.subscribe(subscriber);

		subscriber.getSubscription().request(10);

		// adding these will have no effect as producer already sent complete
		subscriber.getSubscription().request(10);
		subscriber.getSubscription().cancel();
	}
}
