package com.hayes.sec04;

import java.util.ArrayList;
import java.util.List;

import com.hayes.common.Util;
import com.hayes.sec04.helper.NameGenerator;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class Lec03FluxSinkThreadSafety {

	public static void main(String[] args) {
		listWithoutThreadSafety();
		fluxSinkWithThreadSafety();
	}

	private static void listWithoutThreadSafety() {
		List<String> list = new ArrayList<>();

		Runnable runnable = () -> {
			for (int i = 0; i < 1000; i++) {
				list.add(Util.faker().name().firstName());
			}
		};

		for (int i = 0; i < 10 ; i++) {
			Thread.ofPlatform().start(runnable); // 10 threads adding 1000 items
		}

		Util.sleepSeconds(2);
		log.info("Normal List size: {}", list.size()); // Expected: 10000, but often <10000
	}

	private static void fluxSinkWithThreadSafety() {
		List<String> names =  new ArrayList<>();
		NameGenerator generator = new NameGenerator();

		Flux<String> flux = Flux.create(generator);
		flux.subscribe(names::add); // subscriber receives values

		Runnable runnable = () -> {
			for (int i = 0; i < 1000; i++) {
				generator.generate(); // emit names
			}
		};

		for (int i = 0; i < 10 ; i++) {
			Thread.ofPlatform().start(runnable); // 10 threads × 1000 = 10,000
		}

		Util.sleepSeconds(2);
		log.info("Flux List size: {}", names.size()); // ✅ Always 10,000
	}

}
