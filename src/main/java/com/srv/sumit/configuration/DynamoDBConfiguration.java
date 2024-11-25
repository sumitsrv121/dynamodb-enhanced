package com.srv.sumit.configuration;

import com.srv.sumit.annotation.TableName;
import com.srv.sumit.entity.MovieDetails;
import io.awspring.cloud.dynamodb.DynamoDbTableNameResolver;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

@Configuration
@Profile("!integration-test")
public class DynamoDBConfiguration {
    @Value("${aws.dynamodb.accessKey}")
    private String accessKey;

    @Value("${aws.dynamodb.secretKey}")
    private String secretKey;

    @Value("${aws.dynamodb.endpoint}")
    private String endpoint;


    @Bean
    public DynamoDbClient getDynamoDbClient() {
        return DynamoDbClient.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.AP_SOUTH_1)
                .credentialsProvider(StaticCredentialsProvider
                        .create(AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient getDynamoDbEnhancedClient(final DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                //.extensions(AutoGeneratedTimestampRecordExtension.create(), AutoGeneratedUuidExtension.create())
                .build();
    }

    @Bean
    public DynamoDbTemplate getDynamoDbTemplate(final DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        return new DynamoDbTemplate(dynamoDbEnhancedClient);
    }

    // Bind the custom table name resolver
    @Bean
    public DynamoDbTableNameResolver tableNameResolver() {
        return new DynamoDbTableNameResolver() {
            @Override
            public <T> String resolve(Class<T> clazz) {
                TableName tableName = clazz.getAnnotation(TableName.class);
                if (tableName != null && StringUtils.hasText(tableName.name())) {
                    return tableName.name();
                }
                throw new IllegalStateException("Table name not found for class: " + clazz.getName());
            }
        };
    }

    @Bean
    public DynamoDbTable<MovieDetails> getMovieDetailsTable(DynamoDbEnhancedClient dbClient) {
        return dbClient.table(MovieDetails.TABLE_NAME, TableSchema.fromBean(MovieDetails.class));
    }
}
