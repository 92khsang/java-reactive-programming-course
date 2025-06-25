package com.hayes.sec09.helper;

import java.time.Duration;

import reactor.core.publisher.Flux;

public class Kayak {

	public static Flux<Flight> getFlights() {
		return Flux.merge(
						AmericanAirlines.getFlights(),
						Emirates.getFlights(),
						Qatar.getFlights()
				)
				.take(Duration.ofSeconds(2));
	}

}