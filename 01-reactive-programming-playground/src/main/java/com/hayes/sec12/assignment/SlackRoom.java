package com.hayes.sec12.assignment;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Sinks;

@Slf4j
public class SlackRoom {
	private final String name;
	private final List<SlackMember> members;
	private final Sinks.Many<SlackMessage> sink;

	public SlackRoom(String name) {
		this.name = name;
		this.members = new ArrayList<>();

		this.sink = Sinks.many().replay().all();
	}

	public void addMember(SlackMember member) {
		members.add(member);
		member.connect(this.sink, () -> {
			log.info("Removing {} from room '{}'", member.getName(), name);
			members.remove(member);
		});
	}

	String getName() {
		return name;
	}
}