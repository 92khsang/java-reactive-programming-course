package com.hayes.sec05.assignment;

import java.time.Duration;

import com.hayes.common.AbstractHttpClient;
import reactor.core.publisher.Mono;

public class ExternalServiceClient extends AbstractHttpClient {

	public Mono<String> getProductName(int productId) {
		return callPrimary(productId)
				.timeout(Duration.ofSeconds(2))
				.onErrorResume(ex -> callTimeoutFallback(productId))
				.switchIfEmpty(callEmptyFallback(productId));
	}

	private Mono<String> callPrimary(int id) {
		return this.httpClient.get()
				.uri(String.format("/demo03/product/%d", id))
				.responseContent()
				.asString()
				.next();
	}

	private Mono<String> callTimeoutFallback(int id) {
		return this.httpClient.get()
				.uri(String.format("/demo03/timeout-fallback/product/%d", id))
				.responseContent()
				.asString()
				.next();
	}

	private Mono<String> callEmptyFallback(int id) {
		return this.httpClient.get()
				.uri(String.format("/demo03/empty-fallback/product/%d", id))
				.responseContent()
				.asString()
				.next();
	}

}
