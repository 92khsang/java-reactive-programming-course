package com.hayes.sec09.assignment;

import com.hayes.common.AbstractHttpClient;
import reactor.core.publisher.Mono;

// just for demo
public class ExternalServiceClient extends AbstractHttpClient {

	private static final String BASE_URI = "/demo05";

	public Mono<Product> getProduct(int id) {
		return Mono.zip(
						name(id), review(id), price(id)
				)
				.map(t -> new Product(t.getT1(), t.getT2(), t.getT3()));
	}


	private Mono<String> name(Integer id) {
		return this.get(BASE_URI.concat("/product/%d"), id)
				.responseContent()
				.asString()
				.next();
	}

	private Mono<String> price(Integer id) {
		return this.get(BASE_URI.concat("/price/%d"), id)
				.responseContent()
				.asString()
				.next();
	}

	private Mono<String> review(Integer id) {
		return this.get(BASE_URI.concat("/review/%d"), id)
				.responseContent()
				.asString()
				.next();
	}

}