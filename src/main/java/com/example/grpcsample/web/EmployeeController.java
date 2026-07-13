package com.example.grpcsample.web;

import com.example.grpcsample.client.EmployeeClient;
import com.example.grpcsample.client.EmployeeClient.EmployeeResult;
import com.example.grpcsample.grpc.Gender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * Pintu masuk REST untuk EmployeeService. Body request memuat semua field yang lalu
 * diteruskan sebagai satu panggilan gRPC RegisterEmployee lewat EmployeeClient.
 */
@RestController
public class EmployeeController {

    private final EmployeeClient employeeClient;

    public EmployeeController(EmployeeClient employeeClient) {
        this.employeeClient = employeeClient;
    }

    @PostMapping("/api/employees")
    public EmployeeResult register(@RequestBody EmployeeRegistrationRequest request) {
        return employeeClient.registerEmployee(
                request.fullName(),
                request.email(),
                request.age(),
                request.nationalId(),
                request.salary(),
                request.performanceScore(),
                request.isActive(),
                request.gender(),
                request.department(),
                request.skills(),
                request.joinDate());
    }

    public record EmployeeRegistrationRequest(
            String fullName,
            String email,
            int age,
            long nationalId,
            double salary,
            float performanceScore,
            boolean isActive,
            Gender gender,
            String department,
            List<String> skills,
            LocalDate joinDate) {
    }
}
