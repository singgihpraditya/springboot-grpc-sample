package com.example.grpcsample.server;

import com.example.grpcsample.grpc.EmployeeServiceGrpc;
import com.example.grpcsample.grpc.EmployeeStatus;
import com.example.grpcsample.grpc.RegisterEmployeeRequest;
import com.example.grpcsample.grpc.RegisterEmployeeResponse;
import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.Instant;

@GrpcService
public class EmployeeGrpcService extends EmployeeServiceGrpc.EmployeeServiceImplBase {

    @Override
    public void registerEmployee(RegisterEmployeeRequest request, StreamObserver<RegisterEmployeeResponse> responseObserver) {
        String employeeCode = "EMP-%05d".formatted(Math.abs(request.getNationalId()) % 100000);
        EmployeeStatus status = request.getIsActive() ? EmployeeStatus.STATUS_ACTIVE : EmployeeStatus.STATUS_PENDING;
        double annualSalary = request.getSalary() * 12;
        float bonusPercentage = Math.min(request.getPerformanceScore() * 10f, 50f);

        RegisterEmployeeResponse.Builder response = RegisterEmployeeResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Employee %s berhasil didaftarkan di departemen %s."
                        .formatted(request.getFullName(), request.getDepartment()))
                .setEmployeeCode(employeeCode)
                .setEmployeeId(request.getNationalId())
                .setAnnualSalary(annualSalary)
                .setBonusPercentage(bonusPercentage)
                .setStatus(status)
                .setDepartment(request.getDepartment())
                .setRegisteredAt(toTimestamp(Instant.now()));

        request.getSkillsList()
                .forEach(skill -> response.addAssignedTeams(request.getDepartment() + "-" + skill));

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    private static Timestamp toTimestamp(Instant instant) {
        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }
}
