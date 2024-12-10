package se.lisau.project.UI;

import se.lisau.project.DAO.WorkRoleDAO;
import se.lisau.project.DAO.WorkRoleDAOImpl;
import se.lisau.project.model.Employee;
import se.lisau.project.DAO.EmployeeDAOImpl;
import se.lisau.project.model.WorkRole;
import se.lisau.project.service.WorkRoleService;
import se.lisau.project.util.ScannerUtil;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.List;

// användargränssnitt
// hanterar kommunikationen med användaren
public class EmployeeUI {
    // för att kalla på metoder från klassen
    // och används i WorkRoleService-konstruktorn
    WorkRoleDAO workRoleDAO = new WorkRoleDAOImpl();
    // för att kalla på metoder från klassen
    WorkRoleService workRoleService = new WorkRoleService(workRoleDAO);

    // konstruktor för att kunna kalla på startProgram() i Main
    public EmployeeUI() {

    }

    // för att starta programmet
    public void startProgram() throws SQLException {
        System.out.println("Welcome to the Upperud Inc. Database!");
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

    // meny
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

    private void addWorkRole() {
        try {
            System.out.println("Enter title: ");
            String title = ScannerUtil.getUserInput();
            System.out.println("Enter description: ");
            String description = ScannerUtil.getUserInput();
            System.out.println("Enter salary (sek): ");
            double salary = ScannerUtil.readDouble();
            // för att cleara scanner
            ScannerUtil.getUserInput();
            System.out.println("Enter creation date: yyyy-MM-dd");
            Date creationDate = Date.valueOf(ScannerUtil.getUserInput());

            // skapar upp nytt WorkRole objekt med inputen som argument
            WorkRole newWorkRole = new WorkRole(title, description, salary, creationDate);

            // tillsätter objektet som argument till metoden insertWorkRole
            workRoleService.insertWorkRole(newWorkRole);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());

        }
    }

    private void showAllWorkRoles() throws SQLException {
        // hämtar alla arbetsroller där salary är högre än 0
        // lägger in dem i en lista
        List<WorkRole> workRoles = workRoleService.getAllWorkRoles(0);
        // om listan är tom
        if (workRoles.isEmpty()) {
            System.out.println("No work roles found");
        } else {
            // annars iterera över listan och skriv ut
            for (WorkRole workRole : workRoles) {
                System.out.println(workRole);
            }
        }
    }

    private void showSpecificWorkRole() {
        // loop ifall användaren skriver in något annat än en siffra
        while (true) {
            System.out.println("Enter work role id: ");
            try {
                int workRoleId = ScannerUtil.readInt();
                // clear scanner
                ScannerUtil.getUserInput();
                WorkRole workRole = workRoleService.getWorkRoleById(workRoleId);
                if (workRole != null) {
                    System.out.println("Work role: " + workRole);
                } else {
                    System.out.println("No work role with ID " + workRoleId + " found");
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Enter an valid ID-number");
                // clear scanner
                ScannerUtil.getUserInput();
            }

        }
    }

    private void deleteWorkRole() {
        System.out.println("Enter work role id: ");
        try {
            int workRoleId = ScannerUtil.readInt();
            // clear scanner
            ScannerUtil.getUserInput();
            boolean deleted = workRoleService.deleteWorkRoleById(workRoleId);
            if (deleted) {
                System.out.println("Work role " + workRoleId + " deleted successfully");
            } else {
                System.out.println("No work role with ID " + workRoleId + " found");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Enter an valid ID-number");
            ScannerUtil.getUserInput(); // clear scanner
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

        // ändra titel
        System.out.println("Enter new title: (current: " + workRole.getTitle() + ")");
        String newTitle = ScannerUtil.getUserInput();
        // om newTitle inte är tom så sätt den till newTitle
        if (!newTitle.isEmpty()) {
            workRole.setTitle(newTitle);
            // uppdatera
            workRoleDAO.updateWorkRole(workRole);
            System.out.println("New title set: " + workRole.getTitle());

        }

        // ändra beskrivning
        System.out.println("Enter new description: (current: " + workRole.getDescription() + ")");
        String newDescription = ScannerUtil.getUserInput();
        // om strängen inte är tom
        if (!newDescription.isEmpty()) {
            // sätt ny beskrivning och uppdatera
            workRole.setDescription(newDescription);
            workRoleDAO.updateWorkRole(workRole);
            System.out.println("New description set: " + workRole.getDescription());
        }

        // ändra inkomst
        System.out.println("Enter new salary (sek): (current: " + workRole.getSalary() + ")");
        String newSalary = ScannerUtil.getUserInput();
        if (!newSalary.isBlank()) {
            try {
                double newSalaryDouble = Double.parseDouble(newSalary);
                workRole.setSalary(newSalaryDouble);
                System.out.println("New salary set: " + workRole.getSalary());
                workRoleDAO.updateWorkRole(workRole);
            } catch (NumberFormatException e) {
                System.out.println("Invalid salary format. Enter valid salary: 00.00");
                return;
            }
        }
        // ändra kreationsdatum
        while (true) {
            System.out.println("Enter new creation date (current: " + workRole.getCreation_date() + ")");
            try {
                String newCreationDate = ScannerUtil.getUserInput();
                // om input inte är blank
                if (!newCreationDate.isBlank()) {
                    // om nya datumet matchar yyyy-MM-dd
                    if (newCreationDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        // instans av Date för att spara nya datumet
                        Date date = Date.valueOf(newCreationDate);
                        // sätt nya datumet
                        workRole.setCreation_date(date);
                        // uppdatera rollen
                        workRoleDAO.updateWorkRole(workRole);
                        System.out.println("New creation date set: " + workRole.getCreation_date());
                        break;
                    } else {
                        throw new IllegalArgumentException("Invalid creation date");
                    }
                } else { // om man behåller nuvarande datum
                    // gå ur loopen
                    break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid format. Use yyyy-MM-dd");
                // fortsätt loopen tills användaren anger rätt format
            }
        }
        System.out.println("Successfully updated the work role: " + workRole);
    }

    private void logInAsEmployee() throws SQLException {
        System.out.println("Enter E-mail: ");
        String email = ScannerUtil.getUserInput();
        System.out.println("Enter Password: ");
        String password = ScannerUtil.getUserInput();

        // objekt av EmployeeDAO
        EmployeeDAOImpl employeeDAOImpl = new EmployeeDAOImpl();
        // objekt av Employee
        // lägger in user input i argumenten
        Employee employee = employeeDAOImpl.leftJoin(email, password);

        // om det hittas en employee som matchar en i tabellen
        if (employee != null) {
            System.out.println(employee + " | Logged in successfully!");
            System.out.println(employee.getWork_role());

        } else {
            System.out.println("Invalid email or password");
        }

    }
}

