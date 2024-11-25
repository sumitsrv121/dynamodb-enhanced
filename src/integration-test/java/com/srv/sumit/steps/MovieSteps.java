package com.srv.sumit.steps;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.srv.sumit.configuration.AbstractIntegrationTest;
import com.srv.sumit.entity.MovieDetails;
import io.cucumber.java8.En;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import java.io.FileNotFoundException;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class MovieSteps extends AbstractIntegrationTest implements En {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DynamoDbTable<MovieDetails> movieDetailsTable;

    private String movieId;
    private MvcResult result;
    private String requestBody;

    public MovieSteps() {
        // Hook: Before each scenario
        // Insert initial data into the database
        Before(this::initData);

        // Hook: After each scenario
        // Clear the database and reset IDs
        After(this::clearData);


        When("^def movieId = java\\.util\\.UUID\\.randomUUID\\(\\)$", () -> {
            movieId = UUID.randomUUID().toString();
        });
        Given("^the Movie API is available$", () -> {
        });
        When("^a POST request is made to \"([^\"]*)\" with the following JSON request body:$", (String endpoint, String jsonBody) -> {
            // Replace placeholder for `movieId` in the request body
            requestBody = jsonBody.replace("#(movieId)", movieId);

            // Perform the POST request
            result = mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andDo(print())
                    .andReturn();
        });
        Then("^the response status code should be (\\d+)$", (Integer statusCode) -> {
            Assertions.assertNotNull(result, "No response received from the API");
            Assertions.assertEquals(statusCode, result.getResponse().getStatus(),
                    "The response status code does not match the expected value");
        });
        And("^the response should contain the following JSON:$", (String jsonBody) -> {
            JsonElement requestJson = JsonParser.parseString(requestBody);
            JsonElement responseJson = JsonParser.parseString(result.getResponse().getContentAsString());

            Assertions.assertEquals(requestJson, responseJson, "The response body does not match the request body");
        });
    }


    void clearData() {
        movieDetailsTable.deleteTable();
    }


    void initData() {
        movieDetailsTable.createTable();
        try {
            initialization();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private void initialization() throws FileNotFoundException {
        MovieDetails movieDetails1 = gson().fromJson(getResourceFileReader
                ("/MovieControllerIT/itShouldGetListOfMovies/movieDetails1.json"), MovieDetails.class);
        MovieDetails movieDetails2 = gson().fromJson(getResourceFileReader
                ("/MovieControllerIT/itShouldGetListOfMovies/movieDetails2.json"), MovieDetails.class);
        MovieDetails movieDetails3 = gson().fromJson(getResourceFileReader
                ("/MovieControllerIT/itShouldGetListOfMovies/movieDetails3.json"), MovieDetails.class);

        movieDetailsTable.putItem(movieDetails1);
        movieDetailsTable.putItem(movieDetails2);
        movieDetailsTable.putItem(movieDetails3);
    }
}
