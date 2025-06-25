package com.hayes.common;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import reactor.netty.ByteBufFlux;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.LoopResources;

public abstract class AbstractHttpClient {

	private static final String BASE_URL = "http://localhost:8080";

	protected final HttpClient httpClient;

	public AbstractHttpClient() {
		var loopResources = LoopResources.create("custom-thread", 1, true);
		this.httpClient = HttpClient.create()
				.runOn(loopResources)
				.baseUrl(BASE_URL);
	}

	protected ByteBufFlux get(String pathTemplate, Object pathId, Map<String, Object> params) {
		String fullUri = String.format(pathTemplate, pathId);

		if (Objects.nonNull(params) && !params.isEmpty()) {
			fullUri += buildQueryString(params);
		}

		return this.httpClient
				.get()
				.uri(fullUri)
				.responseContent();
	}

	protected ByteBufFlux get(String fullUri, Map<String, Object> params) {
		return get(fullUri, null, params);
	}

	protected ByteBufFlux get(String pathTemplate, Object pathId) {
		return get(pathTemplate, pathId, null);
	}

	protected ByteBufFlux get(String fullUri) {
		return get(fullUri, null, null);
	}

	private String buildQueryString(Map<String, Object> params) {
		String query = params.entrySet().stream()
				.map(entry -> encode(entry.getKey()) + "=" + encode(String.valueOf(entry.getValue())))
				.collect(Collectors.joining("&"));

		return "?" + query;
	}

	private String encode(String value) {
		return URLEncoder.encode(value, StandardCharsets.UTF_8);
	}

}
