package org.example.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exception.FileParseException;
import org.example.model.Employee;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the DataParser interface to parse employee data from a CSV file.
 * It converts the CSV data into a list of Employee objects.
 */
public class EmployeeCSVParser implements DataParser<Employee> {
    private static final Logger logger = LogManager.getLogger(EmployeeCSVParser.class);

    /**
     * Parses a CSV file and converts it into a list of Employee objects.
     *
     * @param filePath the path to the CSV file to be parsed
     * @return a List of Employee objects populated from the CSV file
     * @throws Exception if an error occurs during file reading or parsing
     */
    @Override
    public List<Employee> parse(String filePath) throws Exception {
        logger.info("CSV Data Parsing started");
        List<Employee> employees = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int id = Integer.parseInt(values[0]);
                String firstName = values[1];
                String lastName = values[2];
                double salary = Double.parseDouble(values[3]);
                Integer managerId = values.length > 4 && !values[4].isEmpty() ? Integer.parseInt(values[4]) : null;
                employees.add(new Employee(id, firstName, lastName, salary, managerId));
            }
        } catch (Exception e) {
            logger.error("Error Occurred during file parsing", e);
            throw new FileParseException("Error Occurred during file parsing " + filePath);
        }
        logger.info("CSV Data Parsing completed");
        return employees;
    }
}
