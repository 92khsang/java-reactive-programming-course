package com.hayes.sec02;

import com.hayes.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/*
    Creating publisher is a lightweight operation.
    Executing time-consuming business logic can be delayed
 */

public class Lec09PublisherCreateVsExecution {

	private static final Logger log = LoggerFactory.getLogger(Lec09PublisherCreateVsExecution.class);

	public static void main(String[] args) {
		Util.measureExecutionTime(log, "Without subscribe", () -> {
			getName();
		});

		System.out.println("==========================================");

		Util.measureExecutionTime(log, "With subscribe", () -> {
			getName().subscribe(Util.subscriber());
		});
	}

	private static Mono<String> getName() {
		log.info("Entered getName()");
		return Mono.fromSupplier(() -> {
			Util.sleepSeconds(3);
			log.info("Generating name");
			return Util.faker().name().firstName();
		});
	}
}
