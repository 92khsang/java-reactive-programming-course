package com.hayes.sec04;

import com.hayes.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

public class Lec07FluxGenerateUntil {

	public static void main(String[] args) {
		untilCanadaWithoutTakeUntil();
		utilCanadaWithTakeUntil();
	}

	private static void untilCanadaWithoutTakeUntil() {
		Flux<String> flux = Flux.generate((SynchronousSink<String> sink) -> {
			String country = Util.faker().country().name();
			sink.next(country);

			if ("Canada".equalsIgnoreCase(country)) {
				sink.complete();
			}
		});

		flux.subscribe(Util.subscriber("Country loop"));

	}

	private static void utilCanadaWithTakeUntil() {
		Flux<String> flux = Flux.generate((SynchronousSink<String> sink) -> {
			sink.next(Util.faker().country().name());
		});

		flux.takeUntil(country -> country.equalsIgnoreCase("Canada"))
				.subscribe(Util.subscriber());

	}

}
