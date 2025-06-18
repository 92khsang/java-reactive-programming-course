package com.hayes.sec01.publisher;

import com.github.javafaker.Faker;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubscriptionImpl implements Subscription {

	private static final Logger log = LoggerFactory.getLogger(SubscriptionImpl.class);
	private static final int MAX_ITEMS = 10;
	private final Faker faker;
	private final Subscriber<? super String> subscriber;
	private boolean cancelled;
	private int count;

	public SubscriptionImpl(Subscriber<? super String> subscriber) {
		this.subscriber = subscriber;
		this.faker = Faker.instance();
		this.cancelled = false;
		this.count = 0;
	}

	@Override
	public void request(long requested) {
		if (cancelled) {
			return;
		}
		log.info("subscriber has requested {} items", requested);
		for (int i = 0; i < requested && count < MAX_ITEMS; i++) {
			count++;
			this.subscriber.onNext(this.faker.internet().emailAddress());
		}

		if (count >= MAX_ITEMS) {
			log.info("no more data to produced");
			this.subscriber.onComplete();
			this.cancelled = true;
		}
	}

	@Override
	public void cancel() {
		log.info("subscriber has cancelled");
		this.cancelled = true;
	}
}
