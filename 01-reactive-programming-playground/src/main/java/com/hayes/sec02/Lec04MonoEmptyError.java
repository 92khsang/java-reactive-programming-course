package com.hayes.sec02;

import com.hayes.common.Util;
import reactor.core.publisher.Mono;

/*
    Emitting empty / error
 */
public class Lec04MonoEmptyError {

	public static void main(String[] args) {
		getUsername(1).subscribe(Util.subscriber("User-1"));
		getUsername(2).subscribe(Util.subscriber("User-2"));
		getUsername(3).subscribe(Util.subscriber("User-3"));
	}

	private static Mono<String> getUsername(int userId) {
		return switch (userId) {
			case 1 -> Mono.just("sam");
			case 2 -> Mono.empty(); // null
			default -> Mono.error(new RuntimeException("Invalid input"));
		};
	}
}
