package com.hayes.sec11;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import com.hayes.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/*
    repeat operator simply resubscribes when it sees complete signal.
    it does not like error signal.
 */
public class Lec01Repeat {

	public static void main(String[] args) {
//		repeatCountry(0);
//		repeatCountry(5);
//		conditionalRepeat();
//		conditionAsBooleanSupplier();
//		delayedRepeat();
//		delayedRepeatWithTake();
		repeatWithFlux();
	}

	private static void repeatCountry(Integer repeatCount) {
		Mono<String> mono = getCountry()
				.transform(Util.monoLogger("repeatCountry"));

		var repeatedMono = !repeatCount.equals(0) ? mono.repeat(repeatCount)
				: mono.repeat().take(10); // block Inf loop
		repeatedMono.subscribe();
	}

	private static void conditionalRepeat() {
		getCountry()
				.repeat()
				.takeUntil("canada"::equalsIgnoreCase)
				.subscribe(Util.subscriber("conditionalRepeat"));
	}

	private static void conditionAsBooleanSupplier() {
		AtomicInteger counter =  new AtomicInteger(0);

		getCountry()
				.repeat(() -> counter.getAndIncrement() < 3)
				.subscribe(Util.subscriber("conditionAsBooleanSupplier"));
	}

	private static void delayedRepeat() {
		getCountry()
				.repeatWhen(flux -> flux.delayElements(Duration.ofSeconds(2)))
				.subscribe(Util.subscriber("delayedRepeat"));

		Util.sleepSeconds(6);
	}

	private static void delayedRepeatWithTake() {
		getCountry()
				.repeatWhen(flux -> flux.delayElements(Duration.ofSeconds(2)).take(2))
				.subscribe(Util.subscriber("delayedRepeatWithTake"));

		Util.sleepSeconds(6);
	}

	private static void repeatWithFlux() {
		Flux.just(1, 2, 3)
				.repeat(3)
				.subscribe(Util.subscriber("repeatWithFlux"));
	}

	private static Mono<String> getCountry() {
		return Mono.fromSupplier(() -> Util.faker().country().name());
	}
}