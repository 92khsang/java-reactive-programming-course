package com.hayes.sec06;

import java.time.Duration;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/*
    Hot - 1 data producer for all the subscribers.
    share => publish().refCount(1)
    It needs 1 min subscriber to emit data.
    It stops when there is 0 subscriber.
    Re-subscription - It starts again where there is a new subscriber.
    To have min 2 subscribers, use publish().refCount(2);
 */
@Slf4j
public class Lec02HotPublisher {

	public static void main(String[] args) {
//		coldPublisher();
//		Util.printCutoffLIne();
//		hotPublisher();
//		Util.printCutoffLIne();
//		hotPublisherWithCancel();

		refCountTwo();
		Util.printCutoffLIne();
		refCount();
	}

	private static void coldPublisher() {
		Flux<String> movieFlux = movieStream().take(3);

		movieFlux.subscribe(Util.subscriber("Sam"));
		Util.sleepSeconds(2); // Mike joins late
		movieFlux.subscribe(Util.subscriber("Mike"));
		Util.sleepSeconds(4);
	}

	private static void hotPublisher() {
		Flux<String> movieFlux = movieStream().take(3).share();

		movieFlux.subscribe(Util.subscriber("Sam"));
		Util.sleepSeconds(2); // Mike joins late
		movieFlux.subscribe(Util.subscriber("Mike"));
		Util.sleepSeconds(3);
	}

	private static void hotPublisherWithCancel() {
		Flux<String> movieFlux = movieStream().share();

		movieFlux
				.log("Sam")
				.take(4)
				.subscribe(Util.subscriber("Sam"));

		movieFlux
				.log("Mike")
				.take(3)
				.subscribe(Util.subscriber("Mike"));

		Util.sleepSeconds(5);
	}

	private static void refCountTwo() {
		Flux<String> movieFlux = movieStream().take(2).publish().refCount(2);

		movieFlux
				.doOnSubscribe(__ -> log.info("Sam subscribed"))
				.subscribe(Util.subscriber("Sam"));
		Util.sleepSeconds(1); // Sam does nothing

		movieFlux
				.doOnSubscribe(__ -> log.info("Mike subscribed"))
				.subscribe(Util.subscriber("Mike"));
		Util.sleepSeconds(3);
	}

	private static void refCount() {
		Flux<String> movieFlux = movieStream().take(3).publish().refCount(1);

		movieFlux
				.take(1)
				.doOnSubscribe(__ -> log.info("Sam is entering"))
				.doOnTerminate(() -> log.info("Sam is leaving"))
				.subscribe(Util.subscriber("Sam")); // watches 1 scene, then exits

		Util.sleepSeconds(3);

		movieFlux
				.doOnSubscribe(__ -> log.info("Mike is entering"))
				.doOnTerminate(() -> log.info("Mike is leaving"))
				.subscribe(Util.subscriber("Mike")); // triggers re-subscription

		Util.sleepSeconds(4);
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