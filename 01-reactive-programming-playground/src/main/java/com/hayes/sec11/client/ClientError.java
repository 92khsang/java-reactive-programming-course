package com.hayes.sec11.client;

// just for demo
public class ClientError extends RuntimeException {

	public ClientError(String path) {
		super("Client error At \"" + path + "\"");
	}

}