package com.hayes.sec05;

import com.hayes.common.Util;
import com.hayes.sec05.assignment.ExternalServiceClient;

/*
    Ensure that the external service is up and running!
 */
public class Lec11Assignment {

	public static void main(String[] args) {
		ExternalServiceClient client = new ExternalServiceClient();

		for (int i = 1; i <= 4; i++) {
			client.getProductName(i)
					.subscribe(Util.subscriber("Product-" + i));
		}

		Util.sleepSeconds(3);
	}

}