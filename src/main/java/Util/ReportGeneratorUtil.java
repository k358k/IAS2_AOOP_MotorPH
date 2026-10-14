package Util;

import Model.ReportType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class ReportGeneratorUtil {
    //Generates a report based on parameter
    public void generateReport(Connection conn, ReportType reportType) {
        JasperReport jasperReport = null;
        try {
            // Choose which file to compile based on parameter
            if (reportType == ReportType.EmployeePersonalEmployment) {
                jasperReport = JasperCompileManager.compileReport("src/main/resources/ReportEmployee_PersonalEmployment.jrxml");
            } else if (reportType == ReportType.EmployeeFinancial) {
                jasperReport = JasperCompileManager.compileReport("src/main/resources/ReportEmployee_Financial.jrxml");
            } 

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    null,
                    conn
            );

            // View the report
            JasperViewer.viewReport(jasperPrint, false);     

        } catch (JRException ex) {
            Logger.getLogger(ReportGeneratorUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void generateReport(Connection conn, int employeeId, int month, int year) {
        JasperReport jasperReport = null;
        try {
            // Compile JRXML to Jasper file
            jasperReport = JasperCompileManager.compileReport("src/main/resources/PayslipEmployee.jrxml");

            // Prepare parameters for the report
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("employeeIdParam", employeeId);
            parameters.put("monthParam", month);
            parameters.put("yearParam", year);

            // Fill the report with parameters
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    parameters, // Pass the parameters map
                    conn
            );

            // View the report
            JasperViewer.viewReport(jasperPrint, false);     

        } catch (JRException ex) {
            Logger.getLogger(ReportGeneratorUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
