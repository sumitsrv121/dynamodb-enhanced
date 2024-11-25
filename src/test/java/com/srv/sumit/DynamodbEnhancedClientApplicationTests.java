package com.srv.sumit;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = DynamodbEnhancedClientApplication.class)
@AutoConfigureMockMvc
class DynamodbEnhancedClientApplicationTests {

}
