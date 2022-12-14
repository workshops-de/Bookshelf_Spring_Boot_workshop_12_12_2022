package de.workshops.bookshelf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class BookRestControllerIntegrationTest {

    @Autowired
    private TestRestTemplate anonymousRestTemplate;

    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = anonymousRestTemplate.withBasicAuth("dbUser", "password");
    }

    @Test
    void getAllBooks() {
        Book[] books = restTemplate.getForObject("/book", Book[].class);
        assertThat(books).hasSize(3);
    }

    @Test
    void searchBookByAuthor_should_fail() {
        ResponseEntity<String> response = restTemplate.getForEntity("/book?author=Er", String.class);
        HttpStatus statusCode = response.getStatusCode();
        assertThat(statusCode).isEqualTo(BAD_REQUEST);
    }

    @Test
    void searchBooks_with_isbn_and_author_should_work() {
        BookSearchRequest bookSearchRequest = new BookSearchRequest();
        bookSearchRequest.setIsbn("978-3826655487");
        bookSearchRequest.setAuthor("Robert C. Martin");

        Book[] books = restTemplate.postForObject("/book/search", bookSearchRequest, Book[].class);
        assertThat(books).extracting(Book::getTitle)
                .containsExactly("Clean Code");
    }

    @Test
    void searchBooks_with_author_should_work() {
        BookSearchRequest bookSearchRequest = new BookSearchRequest();
        bookSearchRequest.setAuthor("Robert");

        Book[] books = restTemplate.postForObject("/book/search", bookSearchRequest, Book[].class);
        assertThat(books).extracting(Book::getTitle)
                .containsExactly("Clean Code");
    }

    @Test
    void searchBooks_without_author_should_fail() {
        BookSearchRequest bookSearchRequest = new BookSearchRequest();
        bookSearchRequest.setIsbn("978-3826655487");

        ResponseEntity<String> response = restTemplate.postForEntity("/book/search", bookSearchRequest, String.class);
        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Error handling books: author must not be null");
    }

    @Test
    @DirtiesContext
    void createBook_should_save_book_in_repository() {
        Book book = new Book("Spring Boot", "Introduction to Spring Boot", "Max Mustermann", "12345");
        Book savedBook = restTemplate.withBasicAuth("dbAdmin", "admin").postForObject("/book", book, Book.class);
        assertThat(savedBook).isNotNull();

        Book[] books = restTemplate.getForObject("/book", Book[].class);
        assertThat(books).extracting(Book::getTitle)
                .contains("Spring Boot");
    }
}
