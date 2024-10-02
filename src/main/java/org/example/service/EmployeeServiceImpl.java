package org.example.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.constant.Constants;
import org.example.model.Employee;
import org.example.model.EmployeeReportLinesResult;
import org.example.model.ManagerSalaryAnalysisResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmployeeServiceImpl implements EmployeeService {
    private static final Logger logger = LogManager.getLogger(EmployeeServiceImpl.class);

    public List<ManagerSalaryAnalysisResult> analyzeManagerSalaries(List<Employee> employees) {
        logger.info("analyzeManagerSalaries started");
        Map<Integer, List<Employee>> subordinatesByManager = groupEmployeeByManagerId(employees);

        // Analyze salaries for each manager
        List<ManagerSalaryAnalysisResult> salaryAnalysisResults = new ArrayList<>();
        for (Employee manager : employees) {
            if (subordinatesByManager.containsKey(manager.getId())) {
                List<Employee> subordinates = subordinatesByManager.get(manager.getId());
                double averageSubordinateSalary = subordinates.stream()
                        .mapToDouble(Employee::getSalary)
                        .average().orElse(0.0);

                double managerSalary = manager.getSalary();
                double minAllowedSalary = averageSubordinateSalary * ((Constants.MIN_SALARY_THRESHOLD_PERCENTAGE + 100)/100); // 20% more
                double maxAllowedSalary = averageSubordinateSalary * ((Constants.MIN_SALARY_THRESHOLD_PERCENTAGE + 100)/100); // 50% more

                ManagerSalaryAnalysisResult salaryAnalysisForManager;
                if (managerSalary < minAllowedSalary) {
                    salaryAnalysisForManager = new ManagerSalaryAnalysisResult(manager.getFirstName(), manager.getLastName(),
                            (minAllowedSalary - managerSalary), null);
                    salaryAnalysisResults.add(salaryAnalysisForManager);

                } else if (managerSalary > maxAllowedSalary) {
                    salaryAnalysisForManager = new ManagerSalaryAnalysisResult(manager.getFirstName(), manager.getLastName(), null,
                            managerSalary - maxAllowedSalary);
                    salaryAnalysisResults.add(salaryAnalysisForManager);
                }
            }
        }
        logger.info("analyzeManagerSalaries completed");
        return salaryAnalysisResults;
    }

    public List<EmployeeReportLinesResult> analyzeReportingLines(List<Employee> employees) {
        logger.info("analyzeReportingLines started");
        List<EmployeeReportLinesResult> reportingLineResults = new ArrayList<>();
        Map<Integer, Employee> employeeMap = employees.stream()
                .collect(Collectors.toMap(Employee::getId, e -> e));

        for (Employee emp : employees) {
            int reportingLineLength = calculateReportingLineLength(emp, employeeMap);
            if (reportingLineLength > 4) {
                reportingLineResults.add(new EmployeeReportLinesResult(emp.getFirstName(), emp.getLastName(), reportingLineLength - 4));
            }
        }
        logger.info("analyzeReportingLines completed");
        return reportingLineResults;
    }

    private int calculateReportingLineLength(Employee emp, Map<Integer, Employee> employeeMap) {
        int count = 0;
        Integer managerId = emp.getManagerId();
        while (managerId != null) {
            Employee manager = employeeMap.get(managerId);
            if (manager == null) {
                break;
            }
            count++;
            managerId = manager.getManagerId();
        }
        return count;
    }

    private Map<Integer, List<Employee>> groupEmployeeByManagerId(List<Employee> employees) {
        Map<Integer, List<Employee>> subordinatesByManager = new HashMap<>();

        // Group employees by manager ID
        for (Employee employee : employees) {
            if (employee.getManagerId() != null) {
                subordinatesByManager
                        .computeIfAbsent(employee.getManagerId(), k -> new ArrayList<>())
                        .add(employee);
            }
        }
        logger.debug("grouping of employee by manager completed");
        return subordinatesByManager;
    }
}