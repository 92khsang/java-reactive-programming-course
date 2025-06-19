package com.hayes.sec03;

import java.util.stream.Stream;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/*
    To create flux from stream
 */
@Slf4j
public class Lec04FluxFromStream {

	public static void main(String[] args) {
		// BAD: Reusing a single stream — will throw on second subscription
		Stream<Integer> stream = Stream.of(1, 2, 3);
		Flux<Integer> badFlux = Flux.fromStream(stream);
		badFlux.subscribe(Util.subscriber("Sub-1"));
		try {
			badFlux.subscribe(Util.subscriber("Sub-2")); // throw exception
		}
		catch (IllegalStateException ex) {
			log.error(ex.getMessage(), ex);
		}


		// GOOD: Using Supplier<Stream> to regenerate stream per subscription
		Flux<Integer> goodFlux = Flux.fromStream(() -> Stream.of(1, 2, 3));
		goodFlux.subscribe(Util.subscriber("Sub-3"));
		goodFlux.subscribe(Util.subscriber("Sub-4"));

	}
}
