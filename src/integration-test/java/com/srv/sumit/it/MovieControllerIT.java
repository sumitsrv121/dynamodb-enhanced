package com.srv.sumit.it;

import com.srv.sumit.DynamodbEnhancedClientApplication;
import com.srv.sumit.configuration.AbstractIntegrationTest;
import com.srv.sumit.configuration.LocalDbCreationRule;
import com.srv.sumit.entity.MovieDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = DynamodbEnhancedClientApplication.class)
@AutoConfigureMockMvc
public class MovieControllerIT extends AbstractIntegrationTest {
    @RegisterExtension
    static LocalDbCreationRule localDynamoDb = new LocalDbCreationRule();
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DynamoDbTable<MovieDetails> movieDetailsTable;

    @DynamicPropertySource
    static void localDynamoDbProperties(DynamicPropertyRegistry registry) {
        String port = localDynamoDb.getPort();
        registry.add("aws.dynamodb.endpoint", () ->
                String.format("http://localhost:%s", port));
        registry.add("aws.accessKeyId", () -> "dummyKey123");
        registry.add("aws.secretAccessKey", () -> "dummyValue123");
    }

    @BeforeEach
    public void beforeEach() {
        movieDetailsTable.createTable();
        System.out.println("Successfully created table");
    }

    @AfterEach
    public void afterEach() {
        movieDetailsTable.deleteTable();
        System.out.println("Successfully deleted table");
    }

    @Test
    void itShouldGetListOfMovies() throws Exception {
        MovieDetails movieDetails1 = gson.fromJson(getResourceFileReader
                ("/MovieControllerIT/itShouldGetListOfMovies/movieDetails1.json"), MovieDetails.class);
        MovieDetails movieDetails2 = gson.fromJson(getResourceFileReader
                ("/MovieControllerIT/itShouldGetListOfMovies/movieDetails2.json"), MovieDetails.class);
        MovieDetails movieDetails3 = gson.fromJson(getResourceFileReader
                ("/MovieControllerIT/itShouldGetListOfMovies/movieDetails3.json"), MovieDetails.class);

        movieDetailsTable.putItem(movieDetails1);
        movieDetailsTable.putItem(movieDetails2);
        movieDetailsTable.putItem(movieDetails3);

        mockMvc.perform(get("/apis/v1/movies").queryParam("genre", "thriller")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
                /*.andExpect(jsonPath("", is("")))
                .andExpect(jsonPath("", is("")))
                .andExpect(jsonPath("", is("")))
                .andExpect(jsonPath("", is("")));*/
    }
}
