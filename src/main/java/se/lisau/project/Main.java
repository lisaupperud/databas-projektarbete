package se.lisau.project;

import se.lisau.project.UI.EmployeeUI;
import se.lisau.project.util.ScannerUtil;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        EmployeeUI employeeUI = new EmployeeUI();
        employeeUI.startProgram();
        ScannerUtil.closeScanner();
    }
}
