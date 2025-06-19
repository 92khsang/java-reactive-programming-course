package com.hayes.sec04;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

/*
    Flux generate
    - invokes the given lambda expression again and again based on downstream demand.
    - We can emit only one value at a time
    - will stop when complete method is invoked
    - will stop when error method is invoked
    - will stop downstream cancels
 */
@Slf4j
public class Lec06FluxGenerate {

	public static void main(String[] args) {
		simpleFluxGenerate();
		illegalFluxGenerate();
		fluxGenerateWithTake();
	}

	private static void simpleFluxGenerate() {
		Flux<Integer> flux = Flux.generate((SynchronousSink<Integer> sink) -> {
			sink.next(1);        // emit ONE item
			sink.complete();        // complete the stream
		});

		flux.subscribe(Util.subscriber());
	}

	private static void illegalFluxGenerate() {
		Flux<Integer> flux = Flux.generate((SynchronousSink<Integer> sink) -> {
			sink.next(1);
			sink.next(2); // ❌ IllegalStateException: More than one call to onNext
			sink.complete();
		});

		flux.subscribe(Util.subscriber());
	}

	private static void fluxGenerateWithTake() {
		Flux<Integer> flux = Flux.generate((SynchronousSink<Integer> sink) -> {
			log.info("Generated");
			sink.next(1);
		});

		flux.take(4)
				.subscribe(Util.subscriber());
	}

}
