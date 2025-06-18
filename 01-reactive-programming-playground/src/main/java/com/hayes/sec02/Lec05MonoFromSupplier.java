package com.hayes.sec02;

import java.util.List;

import com.hayes.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/*
    To delay the execution using supplier / callable
 */
public class Lec05MonoFromSupplier {

	private static final Logger log = LoggerFactory.getLogger(Lec05MonoFromSupplier.class);

	public static void main(String[] args) {
		var numbers = List.of(1, 2, 3);

		Mono.fromSupplier(() -> sum(numbers))
				.subscribe(Util.subscriber())
		;
	}

	private static int sum(List<Integer> numbers) {
		log.info("Computing sum of: {}", numbers);
		return numbers.stream().mapToInt(a -> a).sum();
	}

}
