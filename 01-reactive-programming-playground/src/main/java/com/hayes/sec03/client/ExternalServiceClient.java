package com.hayes.sec03.client;

import com.hayes.common.AbstractHttpClient;
import reactor.core.publisher.Flux;

public class ExternalServiceClient extends AbstractHttpClient {

	public Flux<String> getNames() {
		return this.httpClient.get()
				.uri("/demo02/name/stream")
				.responseContent() // ByteBufFlux
				.asString(); // Flux<String>
	}

	public Flux<String> getStocks() {
		return this.httpClient.get()
				.uri("/demo02/stock/stream")
				.responseContent() // ByteBufFlux
				.asString(); // Flux<String>
	}
}
