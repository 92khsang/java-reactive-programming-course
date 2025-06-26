package com.hayes.sec13.client;

import java.util.Map;
import java.util.function.Function;

import reactor.util.context.Context;


// just for demo - could be a bean in real life
public class UserService {

	private static final Map<String, String> USER_CATEGORY_MAP = Map.of(
			"sam", "standard",
			"mike", "prime"
	);

	public static Function<Context, Context> userCategoryContext() {
		return context -> context.<String>getOrEmpty("user")
				.filter(USER_CATEGORY_MAP::containsKey)
				.map(USER_CATEGORY_MAP::get)
				.map(category -> context.put("category", category))
				.orElse(Context.empty());
	}

}