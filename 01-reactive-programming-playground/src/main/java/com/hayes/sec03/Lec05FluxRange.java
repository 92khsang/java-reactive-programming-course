package com.hayes.sec03;

import com.hayes.common.Util;
import reactor.core.publisher.Flux;

/*
    To create a range of items based on the given start and count
 */
public class Lec05FluxRange {

	public static void main(String[] args) {
		// Emit numbers from 1 to 10
		Flux.range(1, 10)
				.subscribe(Util.subscriber("Normal Number Sub"));

		// Generate 10 random names using Faker
		Flux.range(1, 10)
				.map(__ -> Util.faker().name().firstName())
				.subscribe(Util.subscriber("Random Name Sub"));
	}

}
