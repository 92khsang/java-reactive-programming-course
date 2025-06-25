package com.hayes.sec09;

import com.hayes.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/*
    To collect the items received via Flux. Assuming we will have finite items!
 */
public class Lec14CollectList {

	public static void main(String[] args) {
		normalFlow();
//		errorFlow();
	}

	private static void normalFlow() {
		Flux.range(1, 10)
				.log("Mark 1")
				.collectList()
				.log("Mark 2")
				.transform(Util.monoLogger("collectList"))
				.subscribe();
	}

	private static void errorFlow() {
		Flux.range(1, 10)
				.concatWith(Mono.error(new RuntimeException("boom")))
				.collectList()
				.transform(Util.monoLogger("collectList"))
				.subscribe();
	}
}