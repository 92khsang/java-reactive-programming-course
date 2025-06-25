package com.hayes.sec10;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javafaker.Book;
import com.hayes.common.Util;
import com.hayes.sec10.assignment.buffer.BookOrder;
import com.hayes.sec10.assignment.buffer.RevenueReport;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Lec02BufferAssignment {

	public static void main(String[] args) {
		List<String> filteringGenre = List.of(
				"Science fiction",
				"Fantasy",
				"Suspense/Thriller"
		);

		orderStream()
				.filter(order -> filteringGenre.contains(order.genre()))
				.buffer(Duration.ofSeconds(5))
				.flatMap(Lec02BufferAssignment::toReport)
				.subscribe(Util.subscriber("Report"));

		Util.sleepSeconds(20);
	}

	private static Flux<BookOrder> orderStream() {
		return Flux.interval(Duration.ofMillis(200))
				.map(__ -> {
					Book book = Util.faker().book();

					return new BookOrder(
							book.genre(),
							book.title(),
							Util.faker().random().nextInt(10, 100)
					);
				});
	}

	private static Mono<RevenueReport> toReport(List<BookOrder> orders) {
		return Mono.fromSupplier(() -> new RevenueReport(
				LocalTime.now(),
				orders.stream()
						.collect(Collectors.groupingBy(
								BookOrder::genre,
								Collectors.summingInt(BookOrder::price)
						))
		));
	}

}