package com.example.grpcsample.client;

import com.example.grpcsample.grpc.GreetingServiceGrpc;
import com.example.grpcsample.grpc.HelloReply;
import com.example.grpcsample.grpc.HelloRequest;
import com.google.protobuf.Timestamp;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

/**
 * Wrapper gRPC client. @GrpcClient meng-inject stub yang sudah terhubung
 * ke alamat service "greeting-service" sesuai konfigurasi di application.properties.
 */
@Component
public class GreetingClient {

    @GrpcClient("greeting-service")
    private GreetingServiceGrpc.GreetingServiceBlockingStub greetingStub;

    public GreetingResult sayHello(String name, int age, double heightCm, LocalDate birthDate) {
        HelloRequest request = HelloRequest.newBuilder()
                .setName(name)
                .setAge(age)
                .setHeightCm(heightCm)
                .setBirthDate(toTimestamp(birthDate))
                .build();

        HelloReply reply = greetingStub.sayHello(request);
        return new GreetingResult(
                reply.getMessage(),
                reply.getAgeNextYear(),
                reply.getHeightM(),
                toInstant(reply.getProcessedAt()));
    }

    private static Timestamp toTimestamp(LocalDate date) {
        Instant instant = date.atStartOfDay(ZoneOffset.UTC).toInstant();
        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }

    private static Instant toInstant(Timestamp timestamp) {
        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
    }

    public record GreetingResult(String message, int ageNextYear, double heightM, Instant processedAt) {
    }
}
