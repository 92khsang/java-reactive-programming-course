package com.hayes.sec12.assignment;

import java.util.Objects;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Sinks;


@Slf4j
public class SlackMember {

	@Getter
	private final String name;
	private Disposable subscription;
	private Sinks.Many<SlackMessage> sink;

	public SlackMember(String name) {
		this.name = name;
	}

	public void says(String message) {
		SlackMessage slackMessage = new SlackMessage(this.name, message);
		this.sink.emitNext(slackMessage, ((signalType, emitResult) -> {
			if (emitResult.isFailure()) {
				log.error("Sending member '{}' to '{}' failed", name, message);
				return true;
			}
			return false;
		}));
	}

	public void leave() {
		if (Objects.nonNull(this.subscription) && !subscription.isDisposed()) {
			this.sink.tryEmitNext(new SlackMessage(this.name, "Leaving member '" + this.name + "'"));
			this.subscription.dispose();
			this.subscription = null;
		}
	}

	void connect(Sinks.Many<SlackMessage> sink, Runnable onLeaveCallback) {
		this.sink = sink;
		this.subscription = sink.asFlux()
				.doOnNext(message -> {
					if (!message.sender().equals(this.name)) {
						log.info("[{}] {}: {}", this.name, message.sender(), message.message());
					}
				})
				.doOnCancel(() -> {
					log.info("{} disconnected", name);
					onLeaveCallback.run();
				})
				.subscribe();
	}

}