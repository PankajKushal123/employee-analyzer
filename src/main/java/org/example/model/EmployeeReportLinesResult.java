package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeReportLinesResult {
    private String firstName;
    private String lastName;
    private int overLimitLevels;
}
