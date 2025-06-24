package com.hayes.sec07;

import com.hayes.common.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/*
    Often times you really do not need this!
    - prefer non-blocking IO for network calls
 */
@Slf4j
public class Lec08Parallel {

	public static void main(String[] args) {
//		noParallel();
//		parallel();
//		parallelWithParallelism(3);
		parallelAndMerge();
	}

	private static void noParallel() {
		Flux.range(1, 10)
				.map(Lec08Parallel::process)
				.subscribe(Util.subscriber("noParallel"));
	}

	private static void parallel() {
		Flux.range(1, 5)
				.parallel()
				.runOn(Schedulers.parallel())
				.map(Lec08Parallel::process)
				.subscribe(Util.subscriber("Parallel"));

		Util.sleepSeconds(2);
	}

	private static void parallelWithParallelism(int parallelism) {
		Flux.range(1, 5)
				.parallel(parallelism)
				.runOn(Schedulers.parallel())
				.map(Lec08Parallel::process)
				.subscribe(Util.subscriber("Parallel with Parallelism"));

		Util.sleepSeconds((5 / parallelism) + 2);
	}

	private static void parallelAndMerge() {
		Flux.range(1, 5)
				.parallel()
				.runOn(Schedulers.parallel())
				.map(Lec08Parallel::process)
				.sequential()
				.subscribe(Util.subscriber("Parallel"));

		Util.sleepSeconds(2);
	}


	private static int process(int i) {
		log.info("time consuming task {}", i);
		Util.sleepSeconds(1); ;
		return i * 2;
	}

}