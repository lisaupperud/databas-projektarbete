package se.lisau.project;

import se.lisau.project.DAO.WorkRoleDAOImpl;
import se.lisau.project.util.ScannerUtil;

import java.sql.Date;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        EmployeeUI ui = new EmployeeUI();
        ui.startProgram();
        ScannerUtil.closeScanner();
    }
}
