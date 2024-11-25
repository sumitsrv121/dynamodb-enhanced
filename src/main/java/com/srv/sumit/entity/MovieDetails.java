package com.srv.sumit.entity;


import com.srv.sumit.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.LocalDate;

@Setter
@DynamoDbBean
@TableName(name = "tbl_movie_dtl")
@NoArgsConstructor
@AllArgsConstructor
public class MovieDetails {

    public static final String TABLE_NAME = "movie_details";

    private String id;
    private String title;
    private LocalDate year;
    private String genre;
    private String country;
    private Integer duration;
    private String language;


    @DynamoDbPartitionKey
    // @DynamoDbAutoGeneratedUuid
    public String getId() {
        return id;
    }

    @DynamoDbAttribute(value = "title")
    public String getTitle() {
        return title;
    }

    @DynamoDbAttribute(value = "release_year")
    public LocalDate getYear() {
        return year;
    }

    @DynamoDbAttribute(value = "genre")
    public String getGenre() {
        return genre;
    }

    @DynamoDbAttribute(value = "country")
    public String getCountry() {
        return country;
    }

    @DynamoDbAttribute(value = "duration")
    public Integer getDuration() {
        return duration;
    }

    @DynamoDbAttribute(value = "language")
    public String getLanguage() {
        return language;
    }
}
