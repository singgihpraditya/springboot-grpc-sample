package com.example.grpcsample.server;

import com.example.grpcsample.grpc.GreetingServiceGrpc;
import com.example.grpcsample.grpc.HelloReply;
import com.example.grpcsample.grpc.HelloRequest;
import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Implementasi gRPC server. @GrpcService mendaftarkan kelas ini secara otomatis
 * ke gRPC server yang dijalankan Spring Boot pada port {@code grpc.server.port}.
 */
@GrpcService
public class GreetingGrpcService extends GreetingServiceGrpc.GreetingServiceImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        LocalDate birthDate = toLocalDate(request.getBirthDate());
        String message = "Hello, %s! Umur %d tahun, tinggi %.1f cm, lahir %s."
                .formatted(request.getName(), request.getAge(), request.getHeightCm(),
                        birthDate.format(DateTimeFormatter.ISO_LOCAL_DATE));

        HelloReply reply = HelloReply.newBuilder()
                .setMessage(message)
                .setAgeNextYear(request.getAge() + 1)
                .setHeightM(request.getHeightCm() / 100.0)
                .setProcessedAt(toTimestamp(Instant.now()))
                .build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    private static LocalDate toLocalDate(Timestamp timestamp) {
        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos())
                .atZone(ZoneOffset.UTC)
                .toLocalDate();
    }

    private static Timestamp toTimestamp(Instant instant) {
        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }
}
