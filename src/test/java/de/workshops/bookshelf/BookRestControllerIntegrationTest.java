package de.workshops.bookshelf;

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

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class BookRestControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

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
    @DirtiesContext
    void createBook_should_save_book_in_repository() {
        Book book = new Book("Spring Boot", "Introduction to Spring Boot", "Max Mustermann", "12345");
        Book savedBook = restTemplate.postForObject("/book", book, Book.class);
        assertThat(savedBook).isNotNull();

        Book[] books = restTemplate.getForObject("/book", Book[].class);
        assertThat(books).extracting(Book::getTitle)
                .contains("Spring Boot");
    }
}
