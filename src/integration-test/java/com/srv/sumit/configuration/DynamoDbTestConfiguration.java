package com.srv.sumit.configuration;

import com.srv.sumit.entity.MovieDetails;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

@Configuration
@Profile("integration-test")
public class DynamoDbTestConfiguration implements InitializingBean, DisposableBean {
    private static final String LOCAL_DYNAMODB_ENDPOINT = "http://localhost:";
    @RegisterExtension
    private static final LocalDbCreationRule localDynamoDb = new LocalDbCreationRule();

    @DynamicPropertySource
    static void localDynamoDbProperties(DynamicPropertyRegistry registry) {
        String port = localDynamoDb.getPort();
        registry.add("aws.dynamodb.endpoint", () ->
                String.format("http://localhost:%s", port));
    }

    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create("dummy123", "dummy123")
                        ))
                .endpointOverride(URI.create(LOCAL_DYNAMODB_ENDPOINT + localDynamoDb.getPort()))
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    @Bean
    public DynamoDbTable<MovieDetails> getCountryLocaleTable(DynamoDbEnhancedClient dbClient) {
        return dbClient.table(MovieDetails.TABLE_NAME, TableSchema.fromBean(MovieDetails.class));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // Start local DynamoDB when the context is initialized
        localDynamoDb.beforeAll(null);
    }

    @Override
    public void destroy() {
        // Stop local DynamoDB when the context is closed
        localDynamoDb.afterAll(null);
    }
}
