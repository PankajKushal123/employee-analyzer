package org.example.renderer;

import org.example.model.EmployeeReportLinesResult;
import org.example.model.ManagerSalaryAnalysisResult;

import java.util.List;

public class ConsoleOutputRenderer implements OutputRenderer {

    @Override
    public void renderManagerSalary(List<ManagerSalaryAnalysisResult> result) {
        System.out.println("*******************Salary Analysis Output**************************");
        result.forEach(manager -> {
            if (manager.getUnderPaidAmount() != null) {
                System.out.println(manager.getFirstName() + " " + manager.getLastName() +
                        " earns less than they should by " +
                        String.format("%.2f", (manager.getUnderPaidAmount())));
            } else {
                System.out.println(manager.getFirstName() + " " + manager.getLastName() +
                        " earns more than they should by " +
                        String.format("%.2f", (manager.getOverPaidAmount())));
            }
        });
    }

    @Override
    public void renderReportLines(List<EmployeeReportLinesResult> result) {
        System.out.println("*******************Report Lines Output**************************");
        result.forEach(emp ->
                System.out.println(emp.getFirstName() + " " + emp.getLastName() +
                        " has a reporting line too long by " +
                        emp.getOverLimitLevels() + " levels.")
        );
    }
}