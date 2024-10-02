package org.example.renderer;

import org.example.model.EmployeeReportLinesResult;
import org.example.model.ManagerSalaryAnalysisResult;

import java.util.List;

public interface OutputRenderer {
    void renderManagerSalary(List<ManagerSalaryAnalysisResult> result);
    void renderReportLines(List<EmployeeReportLinesResult> result);
}
