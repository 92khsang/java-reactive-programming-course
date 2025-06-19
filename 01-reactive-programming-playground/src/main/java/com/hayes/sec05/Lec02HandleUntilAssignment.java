package com.hayes.sec05;

import com.hayes.common.Util;
import reactor.core.publisher.Flux;

public class Lec02HandleUntilAssignment {

	public static void main(String[] args) {
		generateUtilCanada();
	}

	private static void generateUtilCanada() {
		Flux<String> flux = Flux
				.generate(sink -> {
					sink.next(Util.faker().country().name());
				})
				.cast(String.class)
				.handle((country, sink) -> {
					sink.next(country);

					if ("canada".equalsIgnoreCase(country)) {
						sink.complete();
					}
				});

		flux.subscribe(Util.subscriber("Country loop"));
	}

}
