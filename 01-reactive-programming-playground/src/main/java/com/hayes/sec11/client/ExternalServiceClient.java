package com.hayes.sec11.client;

import com.hayes.common.AbstractHttpClient;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;

// just for demo
public class ExternalServiceClient extends AbstractHttpClient {

	public Flux<String> getCountry() {
		return wrapResponse(this.get("/demo06/country"));
	}

	public Flux<String> getProduct(int id) {
		return wrapResponse(this.get("/demo06/product/%d", id));
	}

	private static Flux<String> wrapResponse(HttpClient.ResponseReceiver<?> receiver) {
		return receiver.response((res, content) -> {
			int code = res.status().code();
			return switch (code) {
				case 200 -> content.asString();
				case 400 -> Flux.error(new ClientError(res.path()));
				default -> Flux.error(new ServerError(res.path()));
			};
		});
	}

}