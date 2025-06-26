package com.hayes.sec10;

import java.time.Duration;

import com.hayes.common.Util;
import com.hayes.sec10.assignment.groupby.OrderProcessingService;
import com.hayes.sec10.assignment.groupby.PurchaseOrder;
import reactor.core.publisher.Flux;

public class Lec06GroupByAssignment {

	public static void main(String[] args) {
		orderStream()
				.transform(OrderProcessingService::process)
				.subscribe(Util.subscriber("OrderStream"));

		Util.sleepSeconds(10);
	}

	private static Flux<PurchaseOrder> orderStream() {
		var commerce = Util.faker().commerce();

		return Flux.interval(Duration.ofMillis(100))
				.map(i -> PurchaseOrder.builder()
						.price(Util.faker().random().nextInt(10, 100))
						.category(commerce.department())
						.item(commerce.productName())
						.build()
				);
	}

}