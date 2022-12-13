package de.workshops.bookshelf;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookRestControllerUnitTest {

    private final BookService bookService = mock(BookService.class);

    private final BookRestController bookRestController = new BookRestController(bookService);

    @Test
    void getAllBooks() {
        List<Book> listWithOneBook = List.of(new Book("Testing Done Right", "A book about unit tests", "Nobody Ever", "123"));
        when(bookService.getAllBooks()).thenReturn(listWithOneBook);

        List<Book> allBooks = bookRestController.getAllBooks();

        assertThat(allBooks).hasSize(1);
    }
}
