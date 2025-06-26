package com.hayes.sec12;

import com.hayes.common.Util;
import com.hayes.sec12.assignment.SlackMember;
import com.hayes.sec12.assignment.SlackRoom;

public class Lec08SlackAssignment {

	public static void main(String[] args) {
		SlackRoom room = new SlackRoom("refractor");

		SlackMember sam = new SlackMember("Sam");
		SlackMember jake = new SlackMember("Jake");
		SlackMember mike = new SlackMember("Mike");

		room.addMember(sam);
		room.addMember(jake);

		sam.says("Hi all...");

		Util.sleepSeconds(2);

		jake.says("Hey!");
		sam.says("I simply wanted to say hi..");

		Util.sleepSeconds(2);

		room.addMember(mike);

		mike.says("Hey guys.. glad to be here...");

		Util.sleepSeconds(2);

		sam.leave();
		jake.leave();
		mike.leave();
	}

}