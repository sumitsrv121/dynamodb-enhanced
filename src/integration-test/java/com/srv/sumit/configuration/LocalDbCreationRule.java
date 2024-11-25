package com.srv.sumit.configuration;


import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import lombok.Getter;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.net.ServerSocket;

public class LocalDbCreationRule implements AfterAllCallback, BeforeAllCallback {
    private DynamoDBProxyServer server;

    @Getter
    private String port;

    public LocalDbCreationRule() {
        System.setProperty("aws.dynamodb.region", "ap-south-1");
        System.setProperty("aws.dynamodb.accessKeyId", "dummyKey123");
        System.setProperty("aws.dynamodb.secretAccessKey", "dummyValue123");
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        this.stopUnchecked(server);
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            port = String.valueOf(serverSocket.getLocalPort());
        }
        server = ServerRunner.createServerFromCommandLineArgs(
                new String[]{"-inMemory", "-port", port});
        server.start();
    }

    protected void stopUnchecked(DynamoDBProxyServer dynamoDbServer) {
        try {
            dynamoDbServer.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
