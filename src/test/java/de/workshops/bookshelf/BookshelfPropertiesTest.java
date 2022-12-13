package de.workshops.bookshelf;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "bookshelf.default-author-name=Max",
        "bookshelf.max-book-number=3"
})
class BookshelfPropertiesTest {

    @Autowired
    private BookshelfProperties properties;

    @Test
    void properties_should_be_mapped_to_object() {
        assertThat(properties.getDefaultAuthorName()).isEqualTo("Max");
        assertThat(properties.getMaxBookNumber()).isEqualTo(3);
    }
}