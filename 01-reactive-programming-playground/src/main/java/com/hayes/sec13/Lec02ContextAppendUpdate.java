package com.hayes.sec13;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

/*
    Context is an immutable map. We can append additional info!
 */
@Slf4j
public class Lec02ContextAppendUpdate {

	public static void main(String[] args) {
		observeMono()
				.contextWrite(ctx -> ctx.put("user", ctx.get("user").toString().toUpperCase()))
//				.contextWrite(ctx -> ctx.delete("c"))
//				.contextWrite(ctx -> Context.of("user", "Mike"))
				.contextWrite(ctx -> ctx.putAllMap(Map.of("e", "f", "g", "h")))
				.contextWrite(ctx -> ctx.put("c", "d"))
				.contextWrite(ctx -> ctx.put("a", "b"))
				.contextWrite(Context.of("user", "sam"))
				.subscribe();

	}


	private static Mono<Void> observeMono() {
		return Mono.deferContextual(ctx -> {
			log.info("Context {}", ctx);
			return Mono.empty();
		});
	}
}