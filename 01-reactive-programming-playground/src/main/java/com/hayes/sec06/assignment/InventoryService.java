package com.hayes.sec06.assignment;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class InventoryService implements OrderProcessor {

	Map<String, Integer> inventoryByCategory = new HashMap<>();

	@Override
	public void consume(Order order) {
		Integer stock = inventoryByCategory.getOrDefault(order.category(), 500);
		inventoryByCategory.put(order.category(), stock - order.quantity());
	}

	@Override
	public Flux<String> stream() {
		return Flux.generate(sink -> {
			sink.next(this.inventoryByCategory.toString());
		});
	}
}