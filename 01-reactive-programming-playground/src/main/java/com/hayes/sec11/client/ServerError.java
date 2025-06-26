package com.hayes.sec11.client;

// just for demo
public class ServerError extends RuntimeException {

	public ServerError(String path) {
		super("Server error At \"" + path + "\"");
	}

}