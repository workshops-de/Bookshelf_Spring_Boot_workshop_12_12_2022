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
        return repository.findBookByIsbn(isbn);
    }

    public List<Book> searchBooksByAuthor(String author) {
        return repository.findByAuthorContains(author);
    }

    public List<Book> searchBooks(BookSearchRequest searchRequest) {
        boolean findByIsbn = StringUtils.hasText(searchRequest.getIsbn());
        boolean findByAuthor = StringUtils.hasText(searchRequest.getAuthor());

        if (findByIsbn && findByAuthor) {
            return repository.findByIsbnAndAuthorContains(searchRequest.getIsbn(), searchRequest.getAuthor());
        } else if (findByIsbn) {
            Optional<Book> book = repository.findBookByIsbn(searchRequest.getIsbn());
            return book.stream().toList();
        } else if (findByAuthor) {
            return repository.findByAuthorContains(searchRequest.getAuthor());
        } else {
            return repository.findAll();
        }
    }

    public Book createBook(Book book) {
        return repository.save(book);
    }
}
