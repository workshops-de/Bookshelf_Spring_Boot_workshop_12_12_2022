package de.workshops.bookshelf;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> getAllBooks() {
        return repository.findAll();
    }

    public Optional<Book> getBookByIsbn(String isbn) {
        return repository.findAll().stream().filter(book -> hasIsbn(book, isbn)).findFirst();
    }

    public List<Book> searchBooksByAuthor(String author) {
        return repository.findAll().stream().filter(book -> hasAuthor(book, author)).toList();
    }

    public List<Book> searchBooks(BookSearchRequest searchRequest) {
        return repository.findAll().stream().filter(book ->
                hasAuthor(book, searchRequest.getAuthor())
                        && hasIsbn(book, searchRequest.getIsbn())
        ).toList();
    }

    private boolean hasIsbn(Book book, String isbn) {
        return !StringUtils.hasText(isbn) || book.getIsbn().equals(isbn);
    }

    private boolean hasAuthor(Book book, String author) {
        return !StringUtils.hasText(author) || book.getAuthor().contains(author);
    }
}
