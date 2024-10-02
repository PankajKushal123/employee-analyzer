package org.example.service;

import org.example.model.Employee;
import org.example.model.EmployeeReportLinesResult;
import org.example.model.ManagerSalaryAnalysisResult;

import java.util.List;

public interface EmployeeService {
    List<ManagerSalaryAnalysisResult> analyzeManagerSalaries(List<Employee> employees);
    List<EmployeeReportLinesResult> analyzeReportingLines(List<Employee> employees);
}
