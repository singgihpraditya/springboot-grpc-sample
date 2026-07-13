package com.example.grpcsample.web;

import com.example.grpcsample.client.GreetingClient;
import com.example.grpcsample.client.GreetingClient.GreetingResult;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * Pintu masuk REST biasa yang di baliknya memanggil gRPC service melalui GreetingClient.
 * Dengan begini, gRPC bisa dicoba lewat browser/Postman tanpa perlu tools khusus seperti grpcurl.
 */
@RestController
public class GreetingController {

    private final GreetingClient greetingClient;

    public GreetingController(GreetingClient greetingClient) {
        this.greetingClient = greetingClient;
    }

    @GetMapping("/api/greet")
    public GreetingResult greet(
            @RequestParam(defaultValue = "World") String name,
            @RequestParam(defaultValue = "25") int age,
            @RequestParam(defaultValue = "170.0") double heightCm,
            @RequestParam(defaultValue = "2000-01-01")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate) {
        return greetingClient.sayHello(name, age, heightCm, birthDate);
    }
}
