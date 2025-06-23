package com.hayes.sec06;

import java.time.Duration;
import java.util.function.UnaryOperator;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/*
    almost same as publish().refCount(1).
    - does NOT stop when subscribers cancel. So it will start producing even for 0 subscribers once it started.
    - make it real hot - publish().autoConnect(0)
 */
@Slf4j
public class Lec03HotPublisherAutoConnect {

	public static void main(String[] args) {
		runAutoConnectScenario(1);
		Util.printCutoffLIne();
		runAutoConnectScenario(0);
	}

	private static void runAutoConnectScenario(int minSubscribers) {
		Flux<String> movieFlux = movieStream()
				.take(8)
				.publish()
				.autoConnect(minSubscribers); // auto-start immediately, no subscribers needed

		Util.sleepSeconds(2);

		movieFlux
				.transform(addSamScenario())
				.subscribe(Util.subscriber("Sam"));


		Util.sleepSeconds(5);

		movieFlux
				.transform(addMikeScenario())
				.subscribe(Util.subscriber("Mike")); // triggers re-subscription

		Util.sleepSeconds(4);
	}

	private static <T> UnaryOperator<Flux<T>> addSamScenario() {
		return flux -> flux
				.take(3)
				.doOnSubscribe(__ -> log.info("Sam is entering"))
				.doOnTerminate(() -> log.info("Sam is leaving"));
	}

	private static <T> UnaryOperator<Flux<T>> addMikeScenario() {
		return flux -> flux
				.take(3)
				.doOnSubscribe(__ -> log.info("Mike is entering"))
				.doOnTerminate(() -> log.info("Mike is leaving"));
	}

	private static Flux<String> movieStream() {
		Flux<String> stream = Flux.generate(
				() -> {
					log.info("Received the request");
					return 1;
				},
				(state, sink) -> {
					String scene = "🎬 Scene " + state;
					log.info("Playing {}", scene);
					sink.next(scene);
					return state + 1;
				}
		);

		return stream
				.delayElements(Duration.ofSeconds(1));
	}
}