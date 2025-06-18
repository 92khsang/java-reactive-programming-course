package com.hayes.sec02;

import java.util.concurrent.CompletableFuture;

import com.hayes.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/*
    If you have a CompletableFuture already, then we can convert that into a Mono
 */
public class Lec08MonoFromFuture {

	private static final Logger log = LoggerFactory.getLogger(Lec08MonoFromFuture.class);

	public static void main(String[] args) {
		Mono.fromFuture(getName("Future with subscriber"))
				.subscribe(Util.subscriber());
		Util.sleepSeconds(1);

		Mono.fromFuture(getName("Future without subscriber"));
		Util.sleepSeconds(1);

		Mono.fromFuture(() -> getName("Lazy Future with subscriber"))
				.subscribe(Util.subscriber());
		Util.sleepSeconds(1);

		Mono.fromFuture(() -> getName("Lazy Future without subscriber"));
		Util.sleepSeconds(1);
	}

	private static CompletableFuture<String> getName(String mark) {
		return CompletableFuture.supplyAsync(() -> {
			log.info("[{}] Generating name", mark);
			return Util.faker().name().firstName();
		});
	}
}
