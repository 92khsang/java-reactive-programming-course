package com.hayes.sec03;

import com.hayes.common.Util;
import com.hayes.sec03.assignment.StockPriceObserver;
import com.hayes.sec03.client.ExternalServiceClient;

/*
    Ensure that the external service is up and running!
 */
public class Lec12Assignment {

	public static void main(String[] args) {

		ExternalServiceClient client = new ExternalServiceClient();
		client.getStocks()
				.map(Integer::valueOf)
				.subscribe(new StockPriceObserver(1000));

		Util.sleepSeconds(21);
	}
}
