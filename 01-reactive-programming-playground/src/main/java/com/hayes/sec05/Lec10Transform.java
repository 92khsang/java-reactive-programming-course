package com.hayes.sec05;

import java.util.function.UnaryOperator;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class Lec10Transform {
	public static void main(String[] args) {
		getCustomers(true)
				.subscribe(Util.subscriber("Customers With Debug"));

		Util.printCutoffLIne();
		getCustomers(false)
				.subscribe(Util.subscriber("Customers Without Debug"));

		Util.printCutoffLIne();
		getPurchaseOrders(true)
				.subscribe(Util.subscriber("PurchaseOrders With Debug"));

		Util.printCutoffLIne();
		getPurchaseOrders(false)
				.subscribe(Util.subscriber("PurchaseOrders Without Debug"));
	}


	record Customer(int id, String name) { }

	record PurchaseOrder(String productName, int quantity) { }

	private static Flux<Customer> getCustomers(boolean debugEnabled) {
		return reactor.core.publisher.Flux.range(1, 3)
				.map(i -> new Customer(i, Util.faker().name().firstName()))
				.transform(debugEnabled ? addDebugger() : UnaryOperator.identity());
	}

	private static Flux<PurchaseOrder> getPurchaseOrders(boolean debugEnabled) {
		return reactor.core.publisher.Flux.range(1, 5)
				.map(i -> new PurchaseOrder(Util.faker().commerce().productName(), i * 10))
				.transform(debugEnabled ? addDebugger() : UnaryOperator.identity());
	}

	private static <T> UnaryOperator<Flux<T>> addDebugger() {
		return flux -> flux
				.doOnNext(data -> log.info("data: {}", data))
				.doOnComplete(() -> log.info("completed"));
	}

}