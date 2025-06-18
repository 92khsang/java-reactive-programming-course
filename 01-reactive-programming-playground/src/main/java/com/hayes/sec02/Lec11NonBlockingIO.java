package com.hayes.sec02;

import com.hayes.common.Util;
import com.hayes.sec02.client.ExternalServiceClient;
import lombok.extern.slf4j.Slf4j;

/*
    To demo non-blocking IO
    Ensure that the external service is up and running!
 */
@Slf4j
public class Lec11NonBlockingIO {

	public static void main(String[] args) {
		log.info("Starting");

		var client = new ExternalServiceClient();

		Util.measureExecutionTime(log, "Non-blocking Request", () -> {
			for (int i = 1; i <= 100; i++) {
				client.getProductName(i) // Each response is delayed by 1 second (artificial delay in server)
						.subscribe(Util.subscriber());
			}
		});

		Util.sleepSeconds(2); // Wait for responses to arrive

	}

}
