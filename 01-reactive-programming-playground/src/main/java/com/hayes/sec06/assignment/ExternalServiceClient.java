package com.hayes.sec06.assignment;

import com.hayes.common.AbstractHttpClient;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class ExternalServiceClient extends AbstractHttpClient {

	public Flux<Order> fetchOrders() {
		return this.httpClient.get()
				.uri("/demo04/orders/stream")
				.responseContent()
				.asString()
				.map(this::parse)
				.doOnNext(o -> log.info("Received order: {}", o))
				.publish()
				.refCount(2);
	}

	private Order parse(String order) {
		String[] parts = order.split(":");
		return new Order(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
	}
}