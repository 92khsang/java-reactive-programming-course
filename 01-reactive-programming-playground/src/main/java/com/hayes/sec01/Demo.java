package com.hayes.sec01;

/*
   1. publisher does not produce data unless subscriber requests for it.
   2. publisher will produce only <= subscriber-requested items. publisher can also produce 0 items!
   3. subscriber can cancel the subscription. producer should stop at that moment as subscriber is no longer interested in consuming the data
   4. producer can send the error signal
 */

import java.time.Duration;

import com.hayes.sec01.publisher.PublisherImpl;
import com.hayes.sec01.subscriber.SubscriberImpl;

public class Demo {

	public static void main(String[] args) throws InterruptedException {
//		demo01();
//		demo02();
//		demo03();
		demo04();
	}

	private static void demo01() {
		var publisher = new PublisherImpl();
		var subscriber = new SubscriberImpl();
		publisher.subscribe(subscriber);
	}

	private static void demo02() throws InterruptedException {
		var publisher = new PublisherImpl();
		var subscriber = new SubscriberImpl();
		publisher.subscribe(subscriber);
		subscriber.getSubscription().request(3);
		Thread.sleep(Duration.ofSeconds(2));
		subscriber.getSubscription().request(3);
		Thread.sleep(Duration.ofSeconds(2));
		subscriber.getSubscription().request(3);
		Thread.sleep(Duration.ofSeconds(2));
		subscriber.getSubscription().request(3);
		Thread.sleep(Duration.ofSeconds(2));
		subscriber.getSubscription().request(3);
	}

	public static void demo03() throws InterruptedException {
		var publisher = new PublisherImpl();
		var subscriber = new SubscriberImpl();
		publisher.subscribe(subscriber);
		subscriber.getSubscription().request(3);
		Thread.sleep(Duration.ofSeconds(2));
		subscriber.getSubscription().cancel();
		subscriber.getSubscription().request(3);
		Thread.sleep(Duration.ofSeconds(2));
	}

	public static void demo04() throws InterruptedException {
		var publisher = new PublisherImpl();
		var subscriber = new SubscriberImpl();
		publisher.subscribe(subscriber);
		subscriber.getSubscription().request(3);
		Thread.sleep(Duration.ofSeconds(2));
		subscriber.getSubscription().request(11);
		Thread.sleep(Duration.ofSeconds(2));
		subscriber.getSubscription().request(3);
		Thread.sleep(Duration.ofSeconds(2));
	}
}
