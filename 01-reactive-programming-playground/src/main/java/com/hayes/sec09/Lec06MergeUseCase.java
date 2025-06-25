package com.hayes.sec09;

import com.hayes.common.Util;
import com.hayes.sec09.helper.Flight;
import com.hayes.sec09.helper.Kayak;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class Lec06MergeUseCase {

	public static void main(String[] args) {
		Flux<Flight> flux = Kayak.getFlights();

		flux.subscribe(Util.subscriber("MergeUseCas"));

		Util.sleepSeconds(3);
	}

}