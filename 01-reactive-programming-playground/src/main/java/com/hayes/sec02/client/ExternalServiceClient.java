package com.hayes.sec02.client;

import com.hayes.common.AbstractHttpClient;
import reactor.core.publisher.Mono;

public class ExternalServiceClient extends AbstractHttpClient {

	public Mono<String> getProductName(int productId) {
		return this.httpClient.get()
				.uri("/demo01/product/" + productId)
				.responseContent() // ByteBufFlux
				.asString() // Flux<String>
				.next(); // Flux<String> -> Mono<String>
	}

}
