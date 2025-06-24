package com.hayes.sec07;

import com.hayes.common.AbstractHttpClient;
import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/*
    Ensure that the external service is up and running!
 */
@Slf4j
public class Lec06EventLoopIssueFix {

	@Slf4j
	public static class BlockedClient extends AbstractHttpClient {
		public Mono<String> getProductName(int productId) {
			return this.httpClient.get()
					.uri("/demo01/product/" + productId)
					.responseContent() // ByteBufFlux
					.asString() // Flux<String>
					.doOnNext(m -> log.info("next: {}", m))
					.next(); // Flux<String> -> Mono<String>
		}
	}

	@Slf4j
	public static class UnblockedClient extends BlockedClient {
		public Mono<String> getProductName(int productId) {
			return super.getProductName(productId)
					.publishOn(Schedulers.boundedElastic());
		}

	}

	public static void main(String[] args) {
		scenario(new BlockedClient());
		Util.printCutoffLIne();
		scenario(new UnblockedClient());
	}

	private static void scenario(BlockedClient httpClient) {
		for (int i = 1; i <= 5; i++) {
			httpClient.getProductName(i)
					.map(Lec06EventLoopIssueFix::process)
					.subscribe(Util.subscriber());
		}

		Util.sleepSeconds(10);
	}


	private static String process(String input) {
		Util.sleepSeconds(1);
		return input + "-processed";
	}
}