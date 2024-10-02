package org.example.renderer;

import org.example.model.EmployeeReportLinesResult;
import org.example.model.ManagerSalaryAnalysisResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ConsoleOutputRendererTest {

    private ConsoleOutputRenderer renderer;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {
        renderer = new ConsoleOutputRenderer();
        System.setOut(new PrintStream(outContent)); // Redirect console output to outContent
    }

    @After
    public void tearDown() {
        System.setOut(originalOut); // Reset console output to the original
    }

    @Test
    public void testRenderManagerSalary_Underpaid() {
        // Create a list with a manager who is underpaid
        List<ManagerSalaryAnalysisResult> managerResults = new ArrayList<>();
        managerResults.add(new ManagerSalaryAnalysisResult("John", "Doe", 5000.0, null));

        // Call the render method
        renderer.renderManagerSalary(managerResults);

        // Normalize line endings
        String actualOutput = outContent.toString().replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n");
        String expectedOutput = "John Doe earns less than they should by 5000.00\n";

        // Assert the output is as expected
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testRenderManagerSalary_Overpaid() {
        // Create a list with a manager who is overpaid
        List<ManagerSalaryAnalysisResult> managerResults = new ArrayList<>();
        managerResults.add(new ManagerSalaryAnalysisResult("Jane", "Smith", null, 7000.0));

        // Call the render method
        renderer.renderManagerSalary(managerResults);

        // Normalize line endings
        String actualOutput = outContent.toString().replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n");
        String expectedOutput = "Jane Smith earns more than they should by 7000.00\n";

        // Assert the output is as expected
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testRenderReportLines() {
        // Create a list with an employee who has too many reporting levels
        List<EmployeeReportLinesResult> reportLineResults = new ArrayList<>();
        reportLineResults.add(new EmployeeReportLinesResult("Alice", "Johnson", 2));

        // Call the render method
        renderer.renderReportLines(reportLineResults);

        // Normalize line endings
        String actualOutput = outContent.toString().replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n");
        String expectedOutput = "Alice Johnson has a reporting line too long by 2 levels.\n";

        // Assert the output is as expected
        assertEquals(expectedOutput, actualOutput);
    }
}
