package com.hayes.sec03;

import java.util.List;

import com.hayes.common.Util;
import reactor.core.publisher.Flux;

/*
    To create flux from iterable or array
 */
public class Lec03FluxFromIterableOrArray {

	public static void main(String[] args) {
		// From List
		List<String> items = List.of("A", "B", "C");
		Flux.fromIterable(items)
				.subscribe(Util.subscriber("ListSubscriber"));

		// From Array
		Integer[] numbers = {1, 2, 3, 4, 5, 6};
		Flux.fromArray(numbers)
				.subscribe(Util.subscriber("ArraySubscriber"));

	}
}
