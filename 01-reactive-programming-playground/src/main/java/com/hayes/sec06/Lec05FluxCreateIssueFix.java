package com.hayes.sec06;

import com.hayes.common.Util;
import com.hayes.sec04.helper.NameGenerator;
import reactor.core.publisher.Flux;

/*
    To fix the issue we faced in sec04/Lec02FluxCreateRefactor
 */
public class Lec05FluxCreateIssueFix {

	public static void main(String[] args) {
		NameGenerator nameGenerator = new NameGenerator();

		Flux<String> flux = Flux.create(nameGenerator).share();
		flux.subscribe(Util.subscriber("Sub1"));
		flux.subscribe(Util.subscriber("Sub2"));

		for (int i = 0; i < 5; i++) {
			nameGenerator.generate();
		}

	}
}