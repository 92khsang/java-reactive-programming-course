package com.hayes.sec03;

import com.hayes.common.Util;
import com.hayes.sec03.client.ExternalServiceClient;

/*
    To demo non-blocking IO with streaming messages
    Ensure that the external service is up and running!
 */
public class Lec08NonBlockingStreamingMessages {

	public static void main(String[] args) {
		var client = new ExternalServiceClient();
		client.getNames().subscribe(Util.subscriber("Sub-1"));
		client.getNames().subscribe(Util.subscriber("Sub-2"));
		Util.sleepSeconds(6);
	}
}
