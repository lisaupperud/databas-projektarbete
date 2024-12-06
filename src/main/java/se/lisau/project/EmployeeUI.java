package se.lisau.project;

import se.lisau.project.DAO.WorkRoleDAO;
import se.lisau.project.DAO.WorkRoleDAOImpl;
import se.lisau.project.util.ScannerUtil;

import java.sql.*;
import java.util.List;

public class EmployeeUI {
    WorkRoleDAO workRoleDAO = new WorkRoleDAOImpl();

    public EmployeeUI() {

    }

    public void startProgram() throws SQLException {
        System.out.println("Welcome to the Database!");
        boolean running = true;
        while (running) {
            menu();
            String choice = ScannerUtil.getUserInput();
            switch (choice) {
                case "1" -> addWorkRole();
                case "2" -> showAllWorkRoles();
                case "3" -> showSpecificWorkRole();
                case "4" -> deleteWorkRole();
                case "5" -> updateWorkRole();
                case "6" -> logInAsEmployee();
                case "7" -> running = false;
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private void menu() {
        System.out.println("|-----------------------------|");
        System.out.println("| 1. Add work role            |");
        System.out.println("| 2. Show all work roles      |");
        System.out.println("| 3. Show specific work role  |");
        System.out.println("| 4. Delete work role         |");
        System.out.println("| 5. Update work role         |");
        System.out.println("| 6. Log in                   |");
        System.out.println("| 7. Exit program             |");
        System.out.println("| Enter your choice:          |");
        System.out.println("|-----------------------------|");
    }

    private void addWorkRole() throws SQLException {
        // BORDE DET VARA TRYCATCH PÅ DESSA METODER?
        //try {
            System.out.println("Enter title: ");
            String title = ScannerUtil.getUserInput();
            System.out.println("Enter description: ");
            String description = ScannerUtil.getUserInput();
            System.out.println("Enter salary: ");
            double salary = ScannerUtil.readDouble();
            // för att cleara scanner
            ScannerUtil.getUserInput();
            System.out.println("Enter creation date: yyyy-MM-dd");
            Date creationDate = Date.valueOf(ScannerUtil.getUserInput());
            WorkRole newWorkRole = new WorkRole(title, description, salary, creationDate);
            workRoleDAO.insertWorkRole(newWorkRole);
        //} catch (SQLException e) {
        //    System.out.println(e.getMessage());
        //    throw e;
        //}
    }

    private void showAllWorkRoles() throws SQLException {
        List<WorkRole> workRoles = workRoleDAO.getWorkRoles();
        if (workRoles.isEmpty()) {
            System.out.println("No work roles found");
        } else {
            for (WorkRole workRole : workRoles) {
                System.out.println(workRole);
            }
        }
    }

    private void showSpecificWorkRole() throws SQLException {
        System.out.println("Enter work role id: ");
        int workRoleId = ScannerUtil.readInt();
        // cleara scanner
        ScannerUtil.getUserInput();
        WorkRole workRole = workRoleDAO.getWorkRole(workRoleId);
        if (workRole != null) {
            System.out.println("Work role: " + workRole);
        } else {
            System.out.println("No work role with ID " + workRoleId + " found");
        }
    }

    private void deleteWorkRole() throws SQLException {
        System.out.println("Enter work role id: ");
        int workRoleId = ScannerUtil.readInt();
        ScannerUtil.getUserInput();
        WorkRole workRole = workRoleDAO.getWorkRole(workRoleId);
        if (workRole != null) {
            workRoleDAO.deleteWorkRole(workRole);
            System.out.println("Work role " + workRole + " deleted successfully");
        } else {
            System.out.println("No work role with ID " + workRoleId + " found");
        }

    }

    private void updateWorkRole() throws SQLException {
        System.out.println("Enter work role id: ");
        int workRoleId = ScannerUtil.readInt();
        ScannerUtil.getUserInput();
        WorkRole workRole = workRoleDAO.getWorkRole(workRoleId);
        if (workRole == null) {
            System.out.println("No work role with ID " + workRoleId + " found");
            return;
        }
        // varför
        workRole.setRole_id(workRole.getRole_id());

        System.out.println("If you want to keep the current attribute, leave blank by pressing enter.");

        System.out.println("Enter new title: (current: " + workRole.getTitle() + ")");
        String newTitle = ScannerUtil.getUserInput();

        // om newTitle inte är tom så sätt den till newTitle
        if (!newTitle.isEmpty()) {
            workRole.setTitle(newTitle);
        }

        System.out.println("Enter new description: (current: " + workRole.getDescription() + ")");
        String newDescription = ScannerUtil.getUserInput();
        if (!newDescription.isEmpty()) {
            workRole.setDescription(newDescription);
        }

        System.out.println("Enter new salary: (current: " + workRole.getSalary() + ")");
        String newSalary = ScannerUtil.getUserInput();
        if (!newSalary.isBlank()) {
            try {
                double newSalaryDouble = Double.parseDouble(newSalary);
                workRole.setSalary(newSalaryDouble);
            } catch (NumberFormatException e) {
                System.out.println("Invalid salary format. Enter valid salary: 00.00");
                return;
            }
        }
        System.out.println("Enter new creation date (current: " + workRole.getCreation_date() + ")");
        String newCreationDate = ScannerUtil.getUserInput();
        if (!newCreationDate.isBlank()) {
            try {
                Date newCreationDateDate = Date.valueOf(newCreationDate);
                workRole.setCreation_date(newCreationDateDate);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid format. Use yyyy-MM-dd");
                return; // avbryter om datum är felaktigt
            }
        }
        workRoleDAO.updateWorkRole(workRole);

    }

    private void logInAsEmployee() throws SQLException {
        System.out.println("Enter E-mail: ");
        String email = ScannerUtil.getUserInput();
        System.out.println("Enter Password: ");
        String password = ScannerUtil.getUserInput();

        EmployeeDAO employeeDAO = new EmployeeDAO();
        Employee employee = employeeDAO.leftJoin(email,password);

        if (employee != null) {
            System.out.println(employee + " logged in successfully");
            System.out.println(employee.getWork_role());

        } else {
            System.out.println("Invalid email or password");
        }

    }

}
