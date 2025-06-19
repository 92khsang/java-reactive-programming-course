package com.hayes.sec02;

import com.hayes.common.Util;
import com.hayes.sec02.assignment.FileService;
import com.hayes.sec02.assignment.FileServiceImpl;

public class Lec12Assignment {

	public static void main(String[] args) {
		FileService fileService = new FileServiceImpl();

		fileService.write("file.txt", "assignment")
				.subscribe(Util.subscriber("Write"))
		;
		Util.sleepSeconds(1);

		fileService.read("file.txt")
				.subscribe(Util.subscriber("Read"))
		;
		Util.sleepSeconds(1);

		fileService.delete("file.txt")
				.subscribe(Util.subscriber("Delete"))
		;
		Util.sleepSeconds(1);
	}
}
