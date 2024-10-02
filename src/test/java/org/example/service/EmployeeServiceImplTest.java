package org.example.service;

import org.example.model.Employee;
import org.example.model.EmployeeReportLinesResult;
import org.example.model.ManagerSalaryAnalysisResult;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class EmployeeServiceImplTest {

    private EmployeeServiceImpl employeeService;

    @Before
    public void setUp() {
        employeeService = new EmployeeServiceImpl();
    }

    @Test
    public void testAnalyzeManagerSalaries() {
        //Given
        List<Employee> employees = new ArrayList<>();
        Employee manager = new Employee(1, "John", "Doe", 60000, null); // Manager
        Employee subordinate1 = new Employee(2, "Jane", "Smith", 40000, 1);
        Employee subordinate2 = new Employee(3, "Bob", "Brown", 45000, 1);
        employees.add(manager);
        employees.add(subordinate1);
        employees.add(subordinate2);

        //When
        List<ManagerSalaryAnalysisResult> results = employeeService.analyzeManagerSalaries(employees);

        //Then
        assertEquals(1, results.size());
    }

    @Test
    public void testAnalyzeManagerSalaries_UnderpaidManager() {
        //Given
        List<Employee> employees = new ArrayList<>();
        Employee manager = new Employee(1, "John", "Doe", 40000, null); // Manager underpaid
        Employee subordinate1 = new Employee(2, "Jane", "Smith", 40000, 1);
        Employee subordinate2 = new Employee(3, "Bob", "Brown", 45000, 1);
        employees.add(manager);
        employees.add(subordinate1);
        employees.add(subordinate2);

        //When
        List<ManagerSalaryAnalysisResult> results = employeeService.analyzeManagerSalaries(employees);

        //Then
        assertEquals(1, results.size());
        ManagerSalaryAnalysisResult result = results.get(0);
        assertEquals("John", result.getFirstName());
        assertTrue(result.getUnderPaidAmount() > 0);
    }

    @Test
    public void testAnalyzeManagerSalaries_OverpaidManager() {
        //Given
        List<Employee> employees = new ArrayList<>();
        Employee manager = new Employee(1, "John", "Doe", 90000, null); // Manager overpaid
        Employee subordinate1 = new Employee(2, "Jane", "Smith", 40000, 1);
        Employee subordinate2 = new Employee(3, "Bob", "Brown", 45000, 1);
        employees.add(manager);
        employees.add(subordinate1);
        employees.add(subordinate2);

        //When
        List<ManagerSalaryAnalysisResult> results = employeeService.analyzeManagerSalaries(employees);

        //Then
        assertEquals(1, results.size());
        ManagerSalaryAnalysisResult result = results.get(0);
        assertEquals("John", result.getFirstName());
        assertTrue(result.getOverPaidAmount() > 0);
    }

    @Test
    public void testAnalyzeReportingLines() {
        //Given
        List<Employee> employees = new ArrayList<>();
        Employee ceo = new Employee(1, "CEO", "Boss", 100000, null);
        Employee manager = new Employee(2, "Manager", "One", 70000, 1);
        Employee worker = new Employee(3, "Worker", "Bee", 50000, 2);
        Employee intern = new Employee(4, "Intern", "Person", 30000, 3);
        employees.add(ceo);
        employees.add(manager);
        employees.add(worker);
        employees.add(intern);

        //When
        List<EmployeeReportLinesResult> results = employeeService.analyzeReportingLines(employees);

        //Then
        assertEquals(0, results.size());
    }

    @Test
    public void testAnalyzeReportingLines_TooManyManagers() {
        //Given
        List<Employee> employees = new ArrayList<>();
        Employee ceo = new Employee(1, "CEO", "Boss", 100000, null);
        Employee manager1 = new Employee(2, "employee1", "One", 70000, 1);
        Employee manager2 = new Employee(3, "employee2", "Two", 60000, 2);
        Employee manager3 = new Employee(4, "employee3", "Three", 50000, 3);
        Employee manager4 = new Employee(5, "employee4", "Four", 40000, 4);
        Employee worker = new Employee(6, "Worker", "Bee", 30000, 5);
        employees.add(ceo);
        employees.add(manager1);
        employees.add(manager2);
        employees.add(manager3);
        employees.add(manager4);
        employees.add(worker);

        //When
        List<EmployeeReportLinesResult> results = employeeService.analyzeReportingLines(employees);

        //Then
        assertEquals(1, results.size());
        EmployeeReportLinesResult result = results.get(0);
        assertEquals("Worker", result.getFirstName());
        assertTrue(result.getOverLimitLevels() > 0);
    }
}
