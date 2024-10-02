package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ManagerSalaryAnalysisResult {
    private String firstName;
    private String lastName;
    private Double underPaidAmount;
    private Double overPaidAmount;
}
