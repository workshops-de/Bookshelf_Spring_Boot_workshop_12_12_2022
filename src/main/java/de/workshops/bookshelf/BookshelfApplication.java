package de.workshops.bookshelf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookshelfApplication {

	private static final Logger log = LoggerFactory.getLogger(BookshelfApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BookshelfApplication.class, args);

		log.info("BookshelfApplication started and ready to use");
	}

}
