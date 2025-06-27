package com.hayes.tests;

import com.hayes.common.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/*
    "assertNext" is a method in StepVerifier
    assertNext = consumeNextWith
    We can also collect all the items and test.
 */
public class Lec05AssertNextTest {

	record Book(int id, String author, String title) { }

	private Flux<Book> getBooks() {
		return Flux.range(1, 3)
				.map(i -> new Book(
						i,
						Util.faker().book().author(),
						Util.faker().book().title())
				);
	}

	@Test
	public void assertNextTest() {
		StepVerifier.create(getBooks())
				.assertNext(book -> Assertions.assertEquals(1, book.id()))
				.assertNext(book -> Assertions.assertEquals(2, book.id()))
				.assertNext(book -> Assertions.assertEquals(3, book.id()))
				.verifyComplete();
	}

	@Test
	public void consumeNextWithTest() {
		StepVerifier.create(getBooks())
				.consumeNextWith(book -> Assertions.assertEquals(1, book.id()))
				.consumeNextWith(book -> Assertions.assertEquals(2, book.id()))
				.consumeNextWith(book -> Assertions.assertEquals(3, book.id()))
				.verifyComplete();
	}

	@Test
	public void thenConsumeWhileTest_mixed() {
		StepVerifier.create(getBooks())
				.assertNext(book -> Assertions.assertEquals(1, book.id()))
//				.assertNext(book -> Assertions.assertEquals(2, book.id()))
//				.assertNext(book -> Assertions.assertEquals(3, book.id()))
				.thenConsumeWhile(book -> book.title() != null && !book.title().isEmpty())
				.verifyComplete();
	}

	@Test
	public void collectListTest() {
		StepVerifier.create(getBooks().collectList()) // Mono<List<Book>>
				.assertNext(list -> {
					Assertions.assertEquals(3, list.size());
					for (int i = 0; i < 3; i++) {
						Assertions.assertEquals(i + 1, list.get(i).id());
					}
				})
				.verifyComplete();
	}

}