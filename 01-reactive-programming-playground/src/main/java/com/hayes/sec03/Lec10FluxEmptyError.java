package com.hayes.sec03;

import com.hayes.common.Util;
import reactor.core.publisher.Flux;

/*
    To create empty/error flux
 */
public class Lec10FluxEmptyError {

	public static void main(String[] args) {
		Flux.empty()
				.subscribe(Util.subscriber("Empty"));

		Flux.error(new RuntimeException("Oops!"))
				.subscribe(Util.subscriber("Error"));
	}

}
