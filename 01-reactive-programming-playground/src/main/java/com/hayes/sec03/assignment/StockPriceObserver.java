package com.hayes.sec03.assignment;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@Slf4j
public class StockPriceObserver implements Subscriber<Integer> {

	private Subscription subscription;

	private final int originBalance;
	private int currentBalance;
	private int stockCount;

	public StockPriceObserver(int balance) {
		this.originBalance = balance;
		this.currentBalance = balance;
		this.stockCount = 0;
	}

	@Override
	public void onSubscribe(Subscription subscription) {
		log.info("Subscribed to stock prices.");
		this.subscription = subscription;
		subscription.request(Long.MAX_VALUE);
	}

	@Override
	public void onNext(Integer price) {
		if (price < 90) {
			buyOneStock(price);
		}
		else if (price > 110) {
			sellAllStocks(price);
		}
		else {
			log.info("Hold: Price is {}. No action taken.", price);
		}
	}

	@Override
	public void onError(Throwable throwable) {
		log.error("Stream error occurred", throwable);
	}

	@Override
	public void onComplete() {
		int profit = currentBalance - originBalance;
		log.info("Stream completed. Final profit: {}", profit);
	}

	private void buyOneStock(int price) {
		if (currentBalance >= price) {
			currentBalance -= price;
			stockCount++;
			log.info("Bought 1 stock at {}. New balance: {}", price, currentBalance);
		}
		else {
			log.info("Insufficient balance to buy at {}. Current: {}", price, currentBalance);
		}
	}

	private void sellAllStocks(int price) {
		if (stockCount > 0) {
			int revenue = price * stockCount;
			currentBalance += revenue;
			log.info("Sold {} stocks at {}. Revenue: {}, New balance: {}", stockCount, price, revenue, currentBalance);
			stockCount = 0;
		}
		else {
			log.info("No stocks to sell at {}", price);
		}

		subscription.cancel();
		log.info("Subscription cancelled after selling.");
	}
}
