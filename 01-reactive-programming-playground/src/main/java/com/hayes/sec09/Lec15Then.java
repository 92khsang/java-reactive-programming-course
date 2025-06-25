package com.hayes.sec09;

import java.time.Duration;
import java.util.List;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/*
    "then" could be helpful when we are not interested in the result of a publisher /
    we need to have sequential execution of asynchronous tasks.
 */
@Slf4j
public class Lec15Then {

	public static void main(String[] args) {
//		basicFlow();
//		withNotification();
		withNotificationFail();
	}

	private static void basicFlow() {
		saveRecords(List.of("A", "B", "C"))
				.then()
				.subscribe(Util.subscriber("basicFlow"));

		Util.sleepSeconds(1);
	}

	private static void withNotification() {
		saveRecords(List.of("A", "B", "C"))
				.then(sendNotification())
				.subscribe(Util.subscriber("withNotification"));

		Util.sleepSeconds(1);
	}

	private static void withNotificationFail() {
		saveRecords(List.of("A", "B", "C"))
				.concatWith(Mono.error(new RuntimeException("Oops")))
				.then()
				.subscribe(Util.subscriber("withNotificationFail"));

		Util.sleepSeconds(1);
	}

	private static Flux<String> saveRecords(List<String> records) {
		return Flux.fromIterable(records)
				.delayElements(Duration.ofMillis(100))
				.doOnNext(record -> log.info("Saved: {}", record));
	}

	private static Mono<Void> sendNotification() {
		return Mono.fromRunnable(() -> log.info("✅ All records saved successfully"));
	}


}