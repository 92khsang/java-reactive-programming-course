package com.hayes.sec09;

import com.hayes.common.Util;
import com.hayes.sec09.assignment.ExternalServiceClient;

/*
    Ensure that the external service is up and running!
 */
public class Lec08ZipAssignment {

	public static void main(String[] args) {
		ExternalServiceClient client = new ExternalServiceClient();

		for (int i = 1; i <= 10; i++) {
			client.getProduct(i)
					.subscribe(Util.subscriber("product-" + i));
		}

		Util.sleepSeconds(2);
	}

}