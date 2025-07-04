package com.hayes.sec13.client;

import com.hayes.common.AbstractHttpClient;
import reactor.core.publisher.Mono;

// just for demo
public class ExternalServiceClient extends AbstractHttpClient {

	public Mono<String> getBook() {
		return this.get("/demo07/book")
				.responseContent()
				.asString()
				.transformDeferredContextual(RateLimiterServer::limitCalls)
				.contextWrite(UserService.userCategoryContext())
				.next();
	}
}