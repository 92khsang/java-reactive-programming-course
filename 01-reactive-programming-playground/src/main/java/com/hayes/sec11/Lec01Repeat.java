package com.hayes.sec11;

import com.hayes.common.Util;
import reactor.core.publisher.Mono;

/*
    repeat operator simply resubscribes when it sees complete signal.
    it does not like error signal.
 */
public class Lec01Repeat {

	public static void main(String[] args) {
		repeatCountry(0);
		Util.printCutoffLIne();
		repeatCountry(5);
	}

	private static void repeatCountry(Integer repeatCount) {
		Mono<String> mono = Mono.fromSupplier(() -> Util.faker().country().name())
				.transform(Util.monoLogger("repeatCountry"));

		var repeatedMono = !repeatCount.equals(0) ? mono.repeat(repeatCount)
				: mono.repeat().take(10); // block Inf loop
		repeatedMono.subscribe();

	}
}