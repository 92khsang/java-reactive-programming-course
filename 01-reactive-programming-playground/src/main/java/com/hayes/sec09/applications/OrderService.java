package com.hayes.sec09.applications;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import com.hayes.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/*
    Just for demo.
    Imagine order-service, as an application, has an endpoint.
    This is a client class to make a call to the endpoint (IO request).
 */
public class OrderService {

	private static final Map<Integer, List<Order>> orderTable = Map.of(
			1, List.of(
					new Order(1, Util.faker().commerce().productName(), Util.faker().random().nextInt(10, 100)),
					new Order(1, Util.faker().commerce().productName(), Util.faker().random().nextInt(10, 100))
			),
			2, List.of(
					new Order(2, Util.faker().commerce().productName(), Util.faker().random().nextInt(10, 100)),
					new Order(2, Util.faker().commerce().productName(), Util.faker().random().nextInt(10, 100)),
					new Order(2, Util.faker().commerce().productName(), Util.faker().random().nextInt(10, 100))
			),
			3, List.of()
	);

	public static Flux<Order> getUserOrders(Integer userId) {
		return Flux.fromIterable(orderTable.get(userId))
				.delayElements(Duration.ofMillis(500))
				.transform(Util.fluxLogger("order-for-user" + userId));
	}

	public static Mono<Integer> getOrderNumbersByUserId(Integer userId) {
		return Mono.fromSupplier(() -> orderTable.get(userId).size());
	}

	public static int getTotalOrderNumbers() {
		return orderTable.values().stream().mapToInt(List::size).sum();
	}
}