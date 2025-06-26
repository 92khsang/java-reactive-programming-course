package com.hayes.sec11;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

/*
    retry operator simply resubscribes when it sees error signal.
 */
@Slf4j
public class Lec02Retry {

	public static void main(String[] args) {
//		retry();

//		retryWhenWithBackoff();
//		retryWhenWithFixedDelay();
//		Util.sleepSeconds(1);

//		retryWhenWithLogging();

//		retryWhenWithFilter();

		onRetryExhaustedThrow();
	}

	private static void retry() {
		createMono()
				.transform(Util.monoLogger("retry"))
				.retry(2)
				.subscribe();
	}

	private static void retryWhenWithBackoff() {
		createMono()
				.transform(Util.monoLogger("retryWhen With Backoff"))
				.retryWhen(Retry.backoff(2, Duration.ofMillis(200)))
				.subscribe();
	}

	private static void retryWhenWithFixedDelay() {
		createMono()
				.transform(Util.monoLogger("retryWhen With FixedDelay"))
				.retryWhen(Retry.fixedDelay(2, Duration.ofMillis(200)))
				.subscribe();
	}

	private static void retryWhenWithLogging() {
		createMono()
				.transform(Util.monoLogger("retryWhen With Logging"))
				.retryWhen(
						Retry.fixedDelay(2, Duration.ofMillis(200))
								.doBeforeRetry(signal ->
										log.info("Retrying... totalRetries={}, failure={}, totalRetriesInARow={}",
												signal.totalRetries(),
												signal.failure().getMessage(),
												signal.totalRetriesInARow()
										)
								)
				)
				.subscribe();

		Util.sleepSeconds(1);
	}

	private static void retryWhenWithFilter() {
		createMono()
				.transform(Util.monoLogger("retryWhen with Filter"))
				.retryWhen(
						Retry.fixedDelay(2, Duration.ofMillis(200))
								.filter(ex -> ex instanceof IllegalArgumentException)
				)
				.subscribe();

		Util.sleepSeconds(1);
	}

	private static void onRetryExhaustedThrow() {
		createMono()
				.transform(Util.monoLogger("onRetryExhaustedThrow"))
				.retryWhen(
						Retry.fixedDelay(1, Duration.ofMillis(100))
								.onRetryExhaustedThrow((spec, signal) -> {
									log.error("onRetryExhaustedThrow is occurred", signal.failure());
									return new RuntimeException("Catch");
								})
				)
				.subscribe();

		Util.sleepSeconds(1);
	}

	private static Mono<String> createMono() {
		AtomicInteger count = new AtomicInteger(0);

		return Mono.fromSupplier(() -> {
			var currentCount = count.getAndIncrement();
			if (currentCount == 0) {
				throw new IllegalArgumentException("Oops " + currentCount);
			}
			else if (currentCount < 3) {
				throw new RuntimeException("Oops " + currentCount);
			}
			return "Success";
		});
	}
}