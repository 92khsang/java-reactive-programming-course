package com.hayes.sec09;

import com.hayes.common.Util;
import com.hayes.sec09.applications.Order;
import com.hayes.sec09.applications.OrderService;
import com.hayes.sec09.applications.User;
import com.hayes.sec09.applications.UserService;
import reactor.core.publisher.Flux;

/*
    Sequential non-blocking IO calls!
    flatMap is used to flatten the inner publisher / to subscribe to the inner publisher
 */
public class Lec11FluxFlatMap {

	public static void main(String[] args) {
//		withoutConcurrency();
		withConcurrency(1);
	}

	private static void withoutConcurrency() {
		Flux<User> users = UserService.getAllUsers();
		Flux<Order> orders = users
				.map(User::id)
				.flatMap(OrderService::getUserOrders);

		orders
				.subscribe(Util.subscriber("Flux2Flux"));

		Util.sleepSeconds(2);
	}

	private static void withConcurrency(int concurrency) {
		Flux<User> users = UserService.getAllUsers();
		Flux<Order> orders = users
				.map(User::id)
				.flatMap(OrderService::getUserOrders, concurrency);

		orders
				.subscribe(Util.subscriber("Flux2Flux"));

		Util.sleepSeconds(3);
	}

}