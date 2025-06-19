package com.hayes.sec03;

import com.hayes.common.Util;
import com.hayes.sec03.helper.NameGenerator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/*
    To covert a Flux <--> Mono
 */
public class Lec11FluxMono {

	public static void main(String[] args) {
		mono2flux();
		flux2mono();
	}

	private static void mono2flux() {
		Mono<String> nameMono = NameGenerator.getNameMono();
		Flux<String> nameFlux = Flux.from(nameMono);
		nameFlux.subscribe(Util.subscriber("nameMono2Flux"));

		Mono<String> emptyMono = Mono.empty();
		Flux<String> emptyFlux = Flux.from(emptyMono);
		emptyFlux.subscribe(Util.subscriber("emptyMono2Flux"));

		Mono<String> errorMono = Mono.error(new RuntimeException("error"));
		Flux<String> errorFlux = Flux.from(errorMono);
		errorFlux.subscribe(Util.subscriber("errorMono2Flux"));
	}

	private static void flux2mono() {
		Flux<Integer> numFlux = Flux.range(1, 10);
		Mono<Integer> numMono = numFlux.next();
		numMono.subscribe(Util.subscriber("numFlux2Mono_1"));

		Mono<Integer> numMono2 = Mono.from(numFlux);
		numMono2.subscribe(Util.subscriber("numFlux2Mono_2"));
	}

}
