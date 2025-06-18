package com.hayes.sec02;

import java.util.List;

import com.hayes.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/*
    To delay the execution using supplier / callable
 */
public class Lec06MonoFromCallable {

	private static final Logger log = LoggerFactory.getLogger(Lec06MonoFromCallable.class);

	public static void main(String[] args) {
		var numbers = List.of(1, 2, 3);

		Mono.fromCallable(() -> sum(numbers))
				.subscribe(Util.subscriber())
		;

		Mono.fromSupplier(() -> {
			try {
				return sum(numbers);
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}).subscribe(Util.subscriber());
	}

	private static int sum(List<Integer> numbers) throws Exception {
		log.info("Computing sum of: {}", numbers);
		return numbers.stream().mapToInt(a -> a).sum();
	}
}
