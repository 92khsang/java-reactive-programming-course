package com.hayes.sec09.applications;

import java.util.Map;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/*
    Just for demo.
    Imagine user-service, as an application, has 2 endpoints.
    This is a client class to make a call to those 2 endpoints (IO requests).
 */
public class UserService {

	private static final Map<String, Integer> userTable = Map.of(
			"sam", 1,
			"mike", 2,
			"jake", 3
	);

	public static Flux<User> getAllUsers() {
		return Flux.fromIterable(userTable.entrySet())
				.map(e -> new User(e.getValue(), e.getKey()));
	}

	public static Mono<Integer> getUserId(String name) {
		return Mono.fromSupplier(() -> userTable.get(name));
	}

}