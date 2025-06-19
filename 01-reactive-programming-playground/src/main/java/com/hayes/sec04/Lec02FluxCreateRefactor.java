package com.hayes.sec04;

import com.hayes.common.Util;
import com.hayes.sec04.helper.NameGenerator;
import reactor.core.publisher.Flux;

public class Lec02FluxCreateRefactor {

	public static void main(String[] args) {
		NameGenerator nameGenerator = new NameGenerator();

		Flux<String> flux = Flux.create(nameGenerator);
		flux.subscribe(Util.subscriber("GenerateNameSub"));

		for (int i = 0; i < 10; i++) {
			nameGenerator.generate();
		}

	}

}
