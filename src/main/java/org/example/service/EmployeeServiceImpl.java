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

/**
 * This class implements the EmployeeService interface to analyze employee data.
 * It provides methods to analyze manager salaries and employee reporting lines.
 */
public class EmployeeServiceImpl implements EmployeeService {
    private static final Logger logger = LogManager.getLogger(EmployeeServiceImpl.class);

    /**
     * Analyzes the salaries of managers based on the salaries of their subordinates.
     *
     * @param employees a list of Employee objects to analyze.
     * @return a list of ManagerSalaryAnalysisResult containing the analysis results.
     */
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

    /**
     * Analyzes the reporting lines of employees to check for excessively long reporting hierarchies.
     *
     * @param employees a list of Employee objects to analyze.
     * @return a list of EmployeeReportLinesResult containing the analysis results.
     */
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

    /**
     * Calculates the length of the reporting line for a given employee.
     *
     * @param emp the Employee object whose reporting line length is to be calculated.
     * @param employeeMap a map of employee IDs to Employee objects.
     * @return the number of managers in the reporting line.
     */
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

    /**
     * Groups employees by their manager ID.
     *
     * @param employees a list of Employee objects to group.
     * @return a map where the key is a manager ID and the value is a list of employees under that manager.
     */
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