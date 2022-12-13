package de.workshops.bookshelf;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("prod")
class ApplicationPropertiesTest {

    @Value("${server.port}")
    private int serverPort;

    @Test
    void serverPort_should_load_prod_value() {
        assertThat(serverPort).isEqualTo(8090);
    }
}
