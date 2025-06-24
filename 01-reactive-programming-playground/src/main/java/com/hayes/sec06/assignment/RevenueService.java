package com.hayes.sec06.assignment;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class RevenueService implements OrderProcessor {

	Map<String, Integer> revenueByCategory = new HashMap<>();

	@Override
	public void consume(Order order) {
		Integer revenue = revenueByCategory.getOrDefault(order.category(), 0);
		revenueByCategory.put(order.category(), revenue + (order.price() * order.quantity()));
	}

	@Override
	public Flux<String> stream() {
		return Flux.generate(sink -> {
			sink.next(this.revenueByCategory.toString());
		});
	}
}