package com.hayes.sec13;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.context.Context;

/*
    Context is for providing metadata about the request (similar to HTTP headers)
 */
@Slf4j
public class Lec01Context {

	public static void main(String[] args) {
//		observeContext();
		injectContext();
	}

	private static void observeContext() {
		Mono.deferContextual(ctx -> {
					log.info("Context {}", ctx);
					return Mono.just("Welcome");
				})
				.subscribe(Util.subscriber("observeContext"));
	}

	private static void injectContext() {
		login()
				.contextWrite(Context.of("user", "Sam"))
				.subscribe(Util.subscriber("loginSuccess"));
		Util.sleepMillis(100);

		Util.printCutoffLIne();
		login()
				.subscribe(Util.subscriber("loginFail"));
		Util.sleepMillis(100);
	}

	private static Mono<String> login() {
		return Mono.deferContextual(ctx -> {
			log.info("Context: {}", ctx);

			if (ctx.hasKey("user")) {
				String user = ctx.get("user");
				return Mono.just("Welcome %s" .formatted(user)); // or String.format("Welcome %s", user)
			}
			return Mono.error(new RuntimeException("Unauthenticated"));
		}).subscribeOn(Schedulers.boundedElastic()); // Only if async boundary is really needed
	}

}