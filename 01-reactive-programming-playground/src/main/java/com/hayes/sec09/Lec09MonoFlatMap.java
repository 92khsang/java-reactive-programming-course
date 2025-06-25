package com.hayes.sec09;

import com.hayes.common.Util;
import com.hayes.sec09.applications.PaymentService;
import com.hayes.sec09.applications.UserService;
import reactor.core.publisher.Mono;

/*
    Sequential non-blocking IO calls!
    flatMap is used to flatten the inner publisher / to subscribe to the inner publisher
 */
public class Lec09MonoFlatMap {

	public static void main(String[] args) {
//		map();
		flatMap();
	}

	private static void map() {
		Mono<Integer> userId = UserService.getUserId("sam");
		Mono<Mono<Integer>> userBalance = userId.map(PaymentService::getUserBalance);
		userBalance.subscribe(Util.subscriber());
	}

	private static void flatMap() {
		Mono<Integer> userId = UserService.getUserId("sam");

		userId
				.flatMap(PaymentService::getUserBalance)
				.transform(Util.monoLogger("flatMap"))
				.subscribe(Util.subscriber("Sub"));
	}

}