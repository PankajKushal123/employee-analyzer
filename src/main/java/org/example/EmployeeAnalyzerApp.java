package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exception.FileParseException;
import org.example.model.Employee;
import org.example.model.EmployeeReportLinesResult;
import org.example.model.ManagerSalaryAnalysisResult;
import org.example.parser.DataParser;
import org.example.parser.EmployeeCSVParser;
import org.example.renderer.ConsoleOutputRenderer;
import org.example.renderer.OutputRenderer;
import org.example.service.EmployeeService;
import org.example.service.EmployeeServiceImpl;

import java.util.List;

import static org.apache.logging.log4j.core.config.Configurator.initialize;

/**
 * The EmployeeAnalyzerApp class serves as the entry point for the Employee Analyzer application.
 * It manages the loading, processing, and rendering of employee data from a CSV file.
 */
public class EmployeeAnalyzerApp {
    private static final Logger logger = LogManager.getLogger(EmployeeAnalyzerApp.class);

    static {
        //Load log4j configuration file
        initialize(null, "src/main/resources/log4j.xml");
    }

    /**
     * The main method that executes the application logic.
     *
     * @param args command-line arguments; expects the path to the CSV file as the first argument
     */
    public static void main(String[] args) {
        DataParser<Employee> parser = new EmployeeCSVParser();
        EmployeeService service = new EmployeeServiceImpl();

        if(args.length <1) {
            System.out.println("Please provide the csv file");
            return;
        }
        String filePath = args[0];

        try {
            // Load records
            List<Employee> employees = parser.parse(filePath);
            logger.info("Employee data loaded. Total employees: {}", employees.size());

            // Analyzing salary discrepancies
            List<ManagerSalaryAnalysisResult> managerSalaryAnalysisResults = service.analyzeManagerSalaries(employees);

            // Analyzing reporting line length
            List<EmployeeReportLinesResult> reportLinesResult = service.analyzeReportingLines(employees);

            // Display Result
            OutputRenderer renderer = new ConsoleOutputRenderer();
            renderer.renderManagerSalary(managerSalaryAnalysisResults);
            renderer.renderReportLines(reportLinesResult);

        } catch (FileParseException fpe) {
            logger.error(fpe.getMessage());
        } catch (Exception e) {
            logger.error("Error occurred during processing of data", e);
        }
    }
}