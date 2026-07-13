package com.example.grpcsample.client;

import com.example.grpcsample.grpc.EmployeeServiceGrpc;
import com.example.grpcsample.grpc.Gender;
import com.example.grpcsample.grpc.RegisterEmployeeRequest;
import com.example.grpcsample.grpc.RegisterEmployeeResponse;
import com.google.protobuf.Timestamp;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Component
public class EmployeeClient {

    @GrpcClient("greeting-service")
    private EmployeeServiceGrpc.EmployeeServiceBlockingStub employeeStub;

    public EmployeeResult registerEmployee(String fullName, String email, int age, long nationalId,
            double salary, float performanceScore, boolean isActive, Gender gender,
            String department, List<String> skills, LocalDate joinDate) {

        RegisterEmployeeRequest request = RegisterEmployeeRequest.newBuilder()
                .setFullName(fullName)
                .setEmail(email)
                .setAge(age)
                .setNationalId(nationalId)
                .setSalary(salary)
                .setPerformanceScore(performanceScore)
                .setIsActive(isActive)
                .setGender(gender)
                .setDepartment(department)
                .addAllSkills(skills)
                .setJoinDate(toTimestamp(joinDate))
                .build();

        RegisterEmployeeResponse response = employeeStub.registerEmployee(request);

        return new EmployeeResult(
                response.getSuccess(),
                response.getMessage(),
                response.getEmployeeCode(),
                response.getEmployeeId(),
                response.getAnnualSalary(),
                response.getBonusPercentage(),
                response.getStatus().name(),
                response.getDepartment(),
                response.getAssignedTeamsList(),
                toInstant(response.getRegisteredAt()));
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

    public record EmployeeResult(boolean success, String message, String employeeCode, long employeeId,
            double annualSalary, float bonusPercentage, String status, String department,
            List<String> assignedTeams, Instant registeredAt) {
    }
}
