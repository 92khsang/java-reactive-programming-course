package com.hayes.sec06;

import java.time.Duration;

import com.hayes.common.Util;
import com.hayes.sec06.assignment.ExternalServiceClient;
import com.hayes.sec06.assignment.InventoryService;
import com.hayes.sec06.assignment.Order;
import com.hayes.sec06.assignment.OrderProcessor;
import com.hayes.sec06.assignment.RevenueService;
import reactor.core.publisher.Flux;

/*
    Ensure that the external service is up and running!
 */
public class Lec06Assignment {

	public static void main(String[] args) {
		ExternalServiceClient client = new ExternalServiceClient();
		OrderProcessor inventoryService = new InventoryService();
		OrderProcessor revenueService = new RevenueService();

		Flux<Order> flux = client.fetchOrders();

		flux
				.doOnNext(inventoryService::consume)
				.subscribe();

		flux
				.doOnNext(revenueService::consume)
				.subscribe();


		inventoryService
				.stream()
				.delayElements(Duration.ofSeconds(2))
				.subscribe(Util.subscriber("inventory"));

		revenueService
				.stream()
				.delayElements(Duration.ofSeconds(2))
				.subscribe(Util.subscriber("revenue"));

		Util.sleepSeconds(20);
	}

}