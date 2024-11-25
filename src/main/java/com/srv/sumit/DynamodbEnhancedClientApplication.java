package com.srv.sumit;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.ArrayList;

@SpringBootApplication
@RequiredArgsConstructor
public class DynamodbEnhancedClientApplication implements CommandLineRunner {

    private final DynamoDbClient dynamoDbClient;

    public static void main(String[] args) {
        SpringApplication.run(DynamodbEnhancedClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<>();
        attributeDefinitions.add(AttributeDefinition.builder().attributeName("id").attributeType("S").build());

        ArrayList<KeySchemaElement> tableKeySchema = new ArrayList<>();
        tableKeySchema.add(KeySchemaElement.builder().attributeName("id").keyType(KeyType.HASH).build());

        String tableName = "movie_details";
        CreateTableRequest createTableRequest = CreateTableRequest.builder().tableName(tableName)
                .provisionedThroughput(ProvisionedThroughput.builder().readCapacityUnits((long) 1)
                        .writeCapacityUnits((long) 1).build())
                .attributeDefinitions(attributeDefinitions).keySchema(tableKeySchema)
                .build();

        try {
            dynamoDbClient.createTable(createTableRequest);
        } catch (ResourceInUseException e) {
            // table already exists, do nothing
            System.out.println("=".repeat(1000));
            System.out.println("Table already created");
            System.out.println("=".repeat(1000));
        }
    }
}
