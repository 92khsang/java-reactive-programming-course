package com.hayes.sec04;

import com.hayes.common.Util;
import reactor.core.publisher.Flux;

/*
    To create a flux & emit items programmatically
 */
public class Lec01FluxCreate {

	public static void main(String[] args) {
		Flux.create(fluxSink -> {
			fluxSink.next(1);
			fluxSink.next(2);
			fluxSink.complete();
		}).subscribe(Util.subscriber("Simple Flux Create"));

		Flux.create(fluxSink -> {
			for (int i = 0; i < 10; i++) {
				fluxSink.next(Util.faker().country().name());
			}
			fluxSink.complete();
		}).subscribe(Util.subscriber("Country loop"));

		Flux.create(fluxSink -> {
			String country;
			do {
				country = Util.faker().country().name();
				fluxSink.next(country);
			}
			while (!"Canada".equalsIgnoreCase(country));
		}).subscribe(Util.subscriber("Util Canada"));

	}

}
