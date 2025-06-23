package com.hayes.sec05;

import java.time.Duration;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/*
    timeout - will produce timeout error.
        - We can handle as part of onError methods
    there is also an overloaded method to accept a publisher
    we can have multiple timeouts. the closest one to the subscriber will take effect for the subscriber.
 */
@Slf4j
public class Lec09Timeout {

	public static void main(String[] args) {
//		singleTimeout();
		multiTimeout();
	}

	private static void singleTimeout() {
		getProductName(1300)
				.timeout(Duration.ofSeconds(1)) // max 1 sec wait
				.subscribe(Util.subscriber("Timeout"));
		Util.sleepSeconds(2);

		Util.printCutoffLIne();
		getProductName(3000)
				.timeout(Duration.ofSeconds(1)) // max 1 sec wait
				.onErrorReturn("fallback") // if timeout occurs, fallback
				.subscribe(Util.subscriber("Timeout"));
		Util.sleepSeconds(2);

		Util.printCutoffLIne();
		getProductName(30)
				.timeout(Duration.ofSeconds(1))
				.subscribe(Util.subscriber("In Time"));
		Util.sleepSeconds(2);

		Util.printCutoffLIne();
		getProductName(3000)
				.timeout(Duration.ofSeconds(1), getProductName(100))
				.subscribe(Util.subscriber("Timeout With fallback publisher"));
		Util.sleepSeconds(2);
	}

	private static void multiTimeout() {
		getProductName(3000)
				.log("Producer Timeout")
				.timeout(Duration.ofMillis(1000)) // Producer Timeout
				.onErrorReturn("Producer Fallback")
				.log("My Timeout")
				.timeout(Duration.ofMillis(200)) // My Timeout
				.onErrorReturn("My Fallback")
				.subscribe(Util.subscriber("MultiTimeout"));
		Util.sleepSeconds(2);

		Util.printCutoffLIne();
		getProductName(3000)
				.log("Producer Timeout")
				.timeout(Duration.ofMillis(200)) // Producer Timeout
				.onErrorReturn("Producer Fallback")
				.log("My Timeout")
				.timeout(Duration.ofMillis(1000)) // My Timeout
				.onErrorReturn("My Fallback")
				.subscribe(Util.subscriber("MultiTimeout"));
		Util.sleepSeconds(2);
	}

	private static Mono<String> getProductName(long waitMs) {
		return Mono.fromSupplier(() -> {
					log.info("waitMs: {}", waitMs);
					return Util.faker().commerce().productName();
				})
				.delayElement(Duration.ofMillis(waitMs)); // simulate slow call
	}

}