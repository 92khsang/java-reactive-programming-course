package com.hayes.sec11;

import java.time.Duration;

import com.hayes.common.Util;
import com.hayes.sec11.client.ClientError;
import com.hayes.sec11.client.ExternalServiceClient;
import com.hayes.sec11.client.ServerError;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

/*
    Ensure that the external service is up and running!
 */
@Slf4j
public class Lec03ExternalServiceDemo {

	public static void main(String[] args) {
		ExternalServiceClient client = new ExternalServiceClient();

//		repeatCountry(client);
		retryProduct(client);
	}

	private static void repeatCountry(ExternalServiceClient client) {
		Flux<String> countryStream = client.getCountry();

		countryStream.repeatWhen(flux
						-> flux.delayElements(Duration.ofMillis(200))
				)
				.takeUntil("canada"::equalsIgnoreCase)
				.subscribe(Util.subscriber());

		Util.sleepSeconds(3);
	}

	private static void retryProduct(ExternalServiceClient client) {

		for (int i = 1; i <= 2; i++) {
			final String tag = String.format("p-%d", i);

			client.getProduct(i)
					.doOnNext(item -> log.info("[{}] Emitted: {}", tag, item))
					.retryWhen(
							Retry.backoff(20, Duration.ofSeconds(1))
									.filter(ex -> ex.getClass().equals(ServerError.class))
									.doBeforeRetry(Lec03ExternalServiceDemo::doBeforeRetryLog)
					)
					.retryWhen(
							Retry.fixedDelay(3, Duration.ofSeconds(1))
									.filter(ex -> ex.getClass().equals(ClientError.class))
									.doBeforeRetry(Lec03ExternalServiceDemo::doBeforeRetryLog)
					)
					.subscribe(Util.subscriber("RetryProduct Sub"));
		}
		Util.sleepSeconds(30);
	}

	private static void doBeforeRetryLog(Retry.RetrySignal signal) {
		log.info("Retrying {}: {}...", signal.totalRetries(), signal.failure().getMessage());
	}

}