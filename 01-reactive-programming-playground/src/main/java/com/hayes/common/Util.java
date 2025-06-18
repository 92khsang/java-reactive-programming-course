package com.hayes.common;

import com.github.javafaker.Faker;
import org.reactivestreams.Subscriber;

public class Util {

	private static final Faker faker = Faker.instance();

	public static <T> Subscriber<T> subscriber() {
		return Util.subscriber("");
	}

	public static <T> Subscriber<T> subscriber(String name) {
		return new DefaultSubscriber<>(name);
	}

	public static Faker faker() {
		return faker;
	}

}
