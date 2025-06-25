package com.hayes.sec09;

import java.util.List;

import com.hayes.common.Util;
import com.hayes.sec09.applications.Order;
import com.hayes.sec09.applications.OrderService;
import com.hayes.sec09.applications.PaymentService;
import com.hayes.sec09.applications.User;
import com.hayes.sec09.applications.UserService;
import reactor.core.publisher.Mono;

/*
    Get all users and build 1 object as shown here.
    record UserInformation(Integer userId, String username, Integer balance, List<Order> orders) {}
 */
public class Lec16Assignment {

	record UserInformation(
			int id,
			String name,
			int balance,
			List<Order> orders
	) { }

	public static void main(String[] args) {

		UserService.getAllUsers()
				.flatMap(Lec16Assignment::toUserInformation)
				.subscribe(Util.subscriber());

		Util.sleepSeconds(3);
	}

	private static Mono<UserInformation> toUserInformation(User user) {
		return Mono.zip(
						PaymentService.getUserBalance(user.id()),
						OrderService.getUserOrders(user.id()).collectList()
				)
				.map(t -> new UserInformation(
								user.id(), user.username(), t.getT1(), t.getT2()
						)
				);
	}

}