package com.hayes.sec04.helper;


import java.util.Objects;
import java.util.function.Consumer;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.FluxSink;

@Slf4j
public class NameGenerator implements Consumer<FluxSink<String>> {

	private FluxSink<String> fluxSink;

	@Override
	public void accept(FluxSink<String> fluxSink) {
		log.info("Got a flux sink");
		this.fluxSink = fluxSink; // store reference for later use
	}


	public void generate() {
		if (Objects.nonNull(this.fluxSink)) {
			fluxSink.next(Util.faker().name().firstName());
		}
	}
}
