package com.hayes.sec09.helper;

import java.util.ArrayList;
import java.util.List;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class NameGenerator {

	private final List<String> redis = new ArrayList<>(); // for demo

	public Flux<String> generateNames() {
		return Flux.generate(sink -> {
					log.info("generating name");
					Util.sleepSeconds(1);
					String name = Util.faker().name().firstName();
					redis.add(name);
					sink.next(name);
				})
				.startWith(redis)
				.cast(String.class);
	}

}