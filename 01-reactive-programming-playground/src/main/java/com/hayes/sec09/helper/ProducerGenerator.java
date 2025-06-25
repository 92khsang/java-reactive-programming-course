package com.hayes.sec09.helper;

import java.time.Duration;
import java.util.Collection;
import java.util.function.UnaryOperator;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class ProducerGenerator {

	public static Flux<Integer> generate(int idx, Collection<Integer> values) {
		return Flux.fromIterable(values)
				.transform(appendCommon(idx));
	}

	public static Flux<Integer> generate(int idx, Throwable error) {
		return Flux.<Integer>error(error)
				.transform(appendCommon(idx));
	}

	private static UnaryOperator<Flux<Integer>> appendCommon(int idx) {
		return flux -> flux
				.delayElements(Duration.ofMillis(10))
				.transform(Util.fluxLogger(String.format("producer %d", idx)));
	}
}
