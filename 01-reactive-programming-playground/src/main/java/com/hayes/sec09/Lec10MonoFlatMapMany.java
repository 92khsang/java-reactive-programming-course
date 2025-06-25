package com.hayes.sec09;

import com.hayes.common.Util;
import com.hayes.sec09.applications.Order;
import com.hayes.sec09.applications.OrderService;
import com.hayes.sec09.applications.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/*
    Sequential non-blocking IO calls!
    flatMap is used to flatten the inner publisher / to subscribe to the inner publisher
    Mono is supposed to be 1 item - what if the flatMap returns multiple items!?
 */
public class Lec10MonoFlatMapMany {

	public static void main(String[] args) {

		for (String name : UserService.getAllUserNames()) {
			Mono<Integer> userId = UserService.getUserId(name);

			Flux<Order> orders = userId
					.transform(Util.monoLogger("FlatMapMany - Mono"))
					.flatMapMany(OrderService::getUserOrders)
					.transform(Util.fluxIntervalLogger("FlatMapMany - Flux"));

			orders.subscribe();

			userId
					.flatMap(OrderService::getOrderNumbersByUserId)
					.doOnNext(number -> {
						Util.sleepMillis(500 * (number + 1));
						Util.printCutoffLIne();
					})
					.subscribe();

		}

	}

}