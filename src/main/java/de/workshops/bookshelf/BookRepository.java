package de.workshops.bookshelf;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findBookByIsbn(String isbn);

    List<Book> findByAuthorContains(String author);

    List<Book> findByIsbnAndAuthorContains(String isbn, String author);

}
