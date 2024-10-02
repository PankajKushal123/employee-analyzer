package org.example.parser;

import org.example.exception.FileParseException;
import org.example.model.Employee;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EmployeeCSVParserTest {

    private EmployeeCSVParser parser;
    private String validFilePath;
    private String invalidFilePath;

    @Before
    public void setUp() {
        parser = new EmployeeCSVParser();
        // Create a valid CSV file for testing
        validFilePath = "src/test/resources/valid_employees.csv";
        // Create an invalid CSV file for testing
        invalidFilePath = "src/test/resources/invalid_employees.csv";
    }

    @Test
    public void testParse_ValidCSV() throws Exception {
        List<Employee> employees = parser.parse(validFilePath);
        
        // Assert that the correct number of employees is parsed
        assertEquals(3, employees.size());

        // Assert the details of the first employee
        Employee emp1 = employees.get(0);
        assertEquals(123, emp1.getId());
        assertEquals("Joe", emp1.getFirstName());
        assertEquals("Doe", emp1.getLastName());
        assertEquals(60000, emp1.getSalary(), 0.01);
        assertNull(emp1.getManagerId());
    }

    @Test(expected = FileParseException.class)
    public void testParse_InvalidCSV() throws Exception {
        parser.parse(invalidFilePath);
    }

    @Test(expected = FileParseException.class)
    public void testParse_NonExistentFile() throws Exception {
        parser.parse("src/test/resources/non_existent_file.csv");
    }
}
