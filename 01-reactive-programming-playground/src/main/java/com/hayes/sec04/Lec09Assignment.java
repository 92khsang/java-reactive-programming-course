package com.hayes.sec04;

import java.net.URL;
import java.nio.file.Path;

import com.hayes.common.Util;
import com.hayes.sec04.assignment.FileReaderService;
import com.hayes.sec04.assignment.FileReaderServiceImpl;

public class Lec09Assignment {

	private static final URL FILE_URL = Lec09Assignment.class.getClassLoader().getResource("sec04/file.txt");

	public static void main(String[] args) {
		FileReaderService reader = new FileReaderServiceImpl();

		assert FILE_URL != null;
		Path filePath = Path.of(FILE_URL.getPath());

		// Base Test
		// reader.read(filePath).subscribe(Util.subscriber("Base Test"));

		// Take Operator
		// reader.read(filePath).take(6).subscribe(Util.subscriber("Take"));

		// Take Until Operator
		reader.read(filePath)
				.takeUntil(line -> line.equalsIgnoreCase("line17"))
				.subscribe(Util.subscriber("Take Until"));
	}

}
