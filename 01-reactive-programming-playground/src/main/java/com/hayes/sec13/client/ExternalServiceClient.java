package com.hayes.sec13.client;

import com.hayes.common.AbstractHttpClient;
import reactor.core.publisher.Flux;

// just for demo
public class ExternalServiceClient extends AbstractHttpClient {

	public Flux<String> getBook() {
		return this.get("/demo07/book")
				.responseContent()
				.asString();
	}
}