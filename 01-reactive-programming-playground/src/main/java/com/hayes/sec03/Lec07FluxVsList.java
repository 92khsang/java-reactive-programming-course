package com.hayes.sec03;

import com.hayes.common.Util;
import com.hayes.sec01.subscriber.SubscriberImpl;
import com.hayes.sec03.helper.NameGenerator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Lec07FluxVsList {

	public static void main(String[] args) {

		// Traditional: blocks until all names are generated
		Util.measureExecutionTime(log, "List", () -> {
			var list = NameGenerator.getNameList(10);
			log.info("list: {}", list);
		});

		// Reactive: responsive streaming of names
		Util.measureExecutionTime(log, "Flux", () -> {
			NameGenerator.getNamesFlux(10)
					.subscribe(Util.subscriber());
		});

		Util.measureExecutionTime(log, "Flux With Cancel", () -> {
			var subscriber = new SubscriberImpl();
			NameGenerator.getNamesFlux(10)
					.subscribe(subscriber);

			subscriber.getSubscription().request(3);
			subscriber.getSubscription().cancel();
		});

	}

}
