package com.hayes.sec09;

import com.hayes.common.Util;
import com.hayes.sec09.helper.NameGenerator;

public class Lec02StartWithUseCase {

	public static void main(String[] args) {
		NameGenerator generator = new NameGenerator();

		generator.generateNames()
				.take(2)
				.subscribe(Util.subscriber("Sam"));

		generator.generateNames()
				.take(2)
				.subscribe(Util.subscriber("Mike"));

		generator.generateNames()
				.take(3)
				.subscribe(Util.subscriber("Jake"));
	}

}