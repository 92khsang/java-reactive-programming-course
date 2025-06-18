package com.hayes.sec02;

import com.hayes.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/*
    Emitting empty after some method invocation
 */
public class Lec07MonoFromRunnable {

	private static final Logger log = LoggerFactory.getLogger(Lec07MonoFromRunnable.class);

	public static void main(String[] args) {
		getProductName(1)
				.subscribe(Util.subscriber("Exist Product"));

		getProductName(2)
				.subscribe(Util.subscriber("No-Exist Product"));

		getProductName(2);
	}

	private static Mono<String> getProductName(int productId) {
		if (productId == 1) {
			return Mono.fromSupplier(() -> Util.faker().commerce().productName());
		}
		return Mono.fromRunnable(() -> notifyBusiness(productId));
	}

	private static void notifyBusiness(int productId) {
		log.info("Notifying business on unavailable product: {}", productId);
	}

}
