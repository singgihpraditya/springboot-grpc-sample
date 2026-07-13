package com.example.grpcsample;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GrpcSampleApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void greetEndpointShouldCallGrpcServiceAndReturnMessage() {
        String url = "http://localhost:" + port + "/api/greet?name=Ogya";

        String response = restTemplate.getForObject(url, String.class);

        assertThat(response).isEqualTo("Hello, Ogya!");
    }
}
