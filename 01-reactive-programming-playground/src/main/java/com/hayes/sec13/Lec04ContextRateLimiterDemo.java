package com.hayes.sec13;

import com.hayes.common.Util;
import com.hayes.sec13.client.ExternalServiceClient;
import com.hayes.sec13.client.UserService;

/*
    Ensure that the external service is up and running!
 */
public class Lec04ContextRateLimiterDemo {

	public static void main(String[] args) {
		ExternalServiceClient client = new ExternalServiceClient();

		for (int i = 0; i < 10; i++) {
			client.getBook()
					.contextWrite(UserService.userCategoryContext())
					.subscribe(Util.subscriber("Book"));

			Util.sleepMillis(100);
		}
	}

}