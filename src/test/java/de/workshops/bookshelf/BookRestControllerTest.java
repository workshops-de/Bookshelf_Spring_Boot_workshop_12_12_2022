package de.workshops.bookshelf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class BookRestControllerTest {

    @MockBean
    private BookService bookService;

    @Autowired
    private BookRestController bookRestController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllBooks_should_return_list_of_books() {
        // Arrange
        List<Book> listWithOneBook = List.of(new Book("Testing Done Right", "A book about unit tests", "Nobody Ever", "123"));
        when(bookService.getAllBooks()).thenReturn(listWithOneBook);

        // Act
        List<Book> books = bookRestController.getAllBooks();

        // Assert
        assertThat(books).hasSize(1);
    }

    @Test
    void getAllBooks_should_have_expected_mapping() throws Exception {
        List<Book> listWithOneBook = List.of(new Book("Testing Done Right", "A book about unit tests", "Nobody Ever", "123"));
        when(bookService.getAllBooks()).thenReturn(listWithOneBook);

        mockMvc.perform(get("/book"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", CoreMatchers.is("Testing Done Right")));
    }

    @Test
    void getAllBooks_should_return_books_as_json_string() throws Exception {
        List<Book> listWithOneBook = List.of(new Book("Testing Done Right", "A book about unit tests", "Nobody Ever", "123"));
        when(bookService.getAllBooks()).thenReturn(listWithOneBook);

        MvcResult result = mockMvc.perform(get("/book"))
                .andDo(print())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Book[] books = objectMapper.readValue(jsonResponse, Book[].class);
        assertThat(books).hasSize(1);
    }

    @Test
    void searchBooksByAuthor_should_fail_if_search_string_is_too_short() throws Exception {
        mockMvc.perform(get("/book").param("author", "Er"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("size must be between 3")));
    }

    @TestConfiguration
    static class JsonTestConfiguration {

        @Bean
        public ObjectMapper mapper() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            return mapper;
        }
    }
}