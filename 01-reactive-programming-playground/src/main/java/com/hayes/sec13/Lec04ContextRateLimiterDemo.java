package com.hayes.sec13;

import com.hayes.common.Util;
import com.hayes.sec13.client.ExternalServiceClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.context.Context;

/*
    Ensure that the external service is up and running!
 */
public class Lec04ContextRateLimiterDemo {

	public static void main(String[] args) {
		Flux.merge(
				Mono.fromRunnable(() -> scenario("Sam")).subscribeOn(Schedulers.boundedElastic()),
				Mono.fromRunnable(() -> scenario("Mike")).subscribeOn(Schedulers.boundedElastic())
		).blockLast();
	}

	private static void scenario(String user) {
		ExternalServiceClient client = new ExternalServiceClient();

		for (int i = 0; i < 10; i++) {
			client.getBook()
					.contextWrite(Context.of("user", user.toLowerCase()))
					.subscribe(Util.subscriber(user));

			Util.sleepSeconds(1);
		}
	}

}