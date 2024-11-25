package com.srv.sumit.service;

import com.srv.sumit.entity.MovieDetails;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final DynamoDbTemplate dynamoDbTemplate;

    public MovieDetails addNewMovie(MovieDetails movieDetails) {
        return dynamoDbTemplate.save(movieDetails);
    }

    public MovieDetails updateMovie(MovieDetails movieDetails) {
        return dynamoDbTemplate.update(movieDetails);
    }

    public MovieDetails deleteByMovieObject(MovieDetails movieDetails) {
        return dynamoDbTemplate.delete(movieDetails);
    }

    public MovieDetails deleteByMovieId(String movieId) {
        Key key = Key.builder().partitionValue(movieId).build();
        return dynamoDbTemplate.delete(key, MovieDetails.class);
    }

    public MovieDetails searchMovieById(String movieId) {
        Key key = Key.builder().partitionValue(movieId).build();
        return dynamoDbTemplate.load(key, MovieDetails.class);
    }

    public List<MovieDetails> scanDataByGenre(String genre) {
        Map<String, AttributeValue> expressionValues = new HashMap<>();
        expressionValues.put(":genre", AttributeValue.fromS(genre));

        Expression expression = Expression.builder().expression("genre = :genre")
                .expressionValues(expressionValues)
                .build();

        ScanEnhancedRequest scanEnhancedRequest = ScanEnhancedRequest.builder()
                .filterExpression(expression)
                .build();

        PageIterable<MovieDetails> returnedList = dynamoDbTemplate.scan(scanEnhancedRequest, MovieDetails.class);
        return returnedList.items().stream().toList();
    }

    public List<MovieDetails> queryData(String partitionKey, String genre) {
        Map<String, AttributeValue> expressionValue = new HashMap<>();
        expressionValue.put(":genre", AttributeValue.fromS(genre));

        Expression expression = Expression.builder()
                .expression("genre = :genre")
                .expressionValues(expressionValue)
                .build();

        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder().partitionValue(partitionKey).build());

        QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .filterExpression(expression)
                .build();

        return dynamoDbTemplate.query(queryRequest, MovieDetails.class).items().stream().toList();
    }
}
