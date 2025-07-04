package com.hayes.sec03;

import com.hayes.common.Util;
import reactor.core.publisher.Flux;

/*
    To demo filter / map operators
 */
public class Lec02MultipleSubscribers {

	public static void main(String[] args) {
		var flux = Flux.just(1, 2, 3, 4, 5, 6);

		flux.subscribe(Util.subscriber("Sub-1"));
		flux
				.filter(i -> i > 7)
				.subscribe(Util.subscriber("Sub-2"));
		flux
				.filter(i -> i % 2 == 0)
				.map(i -> i + "a")
				.subscribe(Util.subscriber("Sub-3"));
	}

}
