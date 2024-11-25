package com.srv.sumit.glue;

import com.srv.sumit.DynamodbEnhancedClientApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = DynamodbEnhancedClientApplication.class)
public class CucumberSpringConfiguration {
}
