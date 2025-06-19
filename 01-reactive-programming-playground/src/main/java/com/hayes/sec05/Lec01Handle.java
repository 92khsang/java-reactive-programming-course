package com.hayes.sec05;

import com.hayes.common.Util;
import reactor.core.publisher.Flux;

/*
    Handle behaves like filter + map

    1 => -2
    4 => do not send
    7 => error
    everything else => send as it is
*/
public class Lec01Handle {

	public static void main(String[] args) {
		handle();
	}

	private static void handle() {
		Flux<Integer> originFlux = Flux.range(1, 10);

		Flux<Integer> handledFlux = originFlux
				.handle((item, sink) -> {
					switch (item) {
					case 1 -> sink.next(-2);
					case 4 -> {
					}
					case 7 -> sink.error(new RuntimeException("Oops!"));
					default -> sink.next(item);
					}
				})
				.cast(Integer.class);


		originFlux.subscribe(Util.subscriber("Origin Flux"));
		handledFlux.subscribe(Util.subscriber("Handled Flux"));
	}

}
