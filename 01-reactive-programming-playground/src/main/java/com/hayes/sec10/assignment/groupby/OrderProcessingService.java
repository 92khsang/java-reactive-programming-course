package com.hayes.sec10.assignment.groupby;

import java.util.Map;
import java.util.function.UnaryOperator;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

// just for demo/assignment
// in the real life - we can have separate classes for every category
@Slf4j
public class OrderProcessingService {

	private static final Map<String, UnaryOperator<Flux<PurchaseOrder>>> TRANSFORM_MAP = Map.of(
			"Automotive", OrderProcessingService::processAutomotive,
			"Kids", OrderProcessingService::processKids
	);

	public static Flux<PurchaseOrder> process(Flux<PurchaseOrder> flux) {
		return flux
				.filter(order -> TRANSFORM_MAP.containsKey(order.getCategory()))
				.groupBy(PurchaseOrder::getCategory)
				.flatMap(groupedOrder ->
						TRANSFORM_MAP.get(groupedOrder.key()).apply(groupedOrder)
				);
	}

	public static Flux<PurchaseOrder> processKids(Flux<PurchaseOrder> flux) {
		return flux
				.concatMap(order -> {
					log.info("Kids order {}", order);
					return Flux.just(order, PurchaseOrder.builder()
							.category(order.getCategory())
							.price(0)
							.item(order.getItem())
							.build()
					);
				});
	}

	public static Flux<PurchaseOrder> processAutomotive(Flux<PurchaseOrder> flux) {
		return flux
				.map(order -> {
							log.info("Automotive order {}", order);
							return PurchaseOrder.builder()
									.category(order.getCategory())
									.price(order.getPrice() + 100)
									.item(order.getItem())
									.build();
						}
				);
	}

}