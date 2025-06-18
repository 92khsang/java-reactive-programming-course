package com.hayes.sec02;

import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
    If we do not have the terminal operator, then stream operators will not execute
 */
public class Lec01LazyStream {

	private static Logger logger = LoggerFactory.getLogger(Lec01LazyStream.class);

	public static void main(String[] args) {
		Stream.of(1)
				.peek(i -> logger.info("received: {}", i))
				.toList();
	}
}
