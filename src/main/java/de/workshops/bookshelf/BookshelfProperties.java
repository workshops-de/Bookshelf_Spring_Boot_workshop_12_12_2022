package de.workshops.bookshelf;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("bookshelf")
@Getter
@Setter
public class BookshelfProperties {

    private String defaultAuthorName;

    private int maxBookNumber;
}
