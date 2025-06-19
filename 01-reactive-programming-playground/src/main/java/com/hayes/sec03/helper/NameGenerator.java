package com.hayes.sec03.helper;

import java.util.List;
import java.util.stream.IntStream;

import com.hayes.common.Util;
import reactor.core.publisher.Flux;

public class NameGenerator {

	public static List<String> getNameList(int count) {
		return IntStream.rangeClosed(1, count)
				.mapToObj(__ -> generateName())
				.toList();
	}

	public static Flux<String> getNamesFlux(int count) {
		return Flux.range(1, count)
				.map(__ -> generateName());
	}

	private static String generateName() {
		Util.sleepSeconds(1); // Delay
		return Util.faker().name().fullName();
	}

}
