package com.hayes.sec09;

import com.hayes.common.Util;
import com.hayes.sec09.assignment.ExternalServiceClient;
import reactor.core.publisher.Flux;

/*
    Ensure that the external service is up and running!
 */
public class Lec13ConcatMap {

	public static void main(String[] args) {
		ExternalServiceClient client = new ExternalServiceClient();

		Flux.range(1, 10)
				.concatMap(client::getProduct)
				.transform(Util.fluxIntervalLogger("product"))
				.subscribe();

		Util.sleepSeconds(12);
	}

}