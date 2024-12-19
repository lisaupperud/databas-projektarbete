package se.lisau.project.UI;

import se.lisau.project.DAO.EmployeeDAO;
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
public class UserInteraction {
    // för att kalla på metoder från klassen
    // och används i WorkRoleService-konstruktorn
    WorkRoleDAO workRoleDAO = new WorkRoleDAOImpl();
    // för att kalla på metoder från klassen
    WorkRoleService workRoleService = new WorkRoleService(workRoleDAO);

    // konstruktor för att kunna kalla på startProgram() i Main
    public UserInteraction() {

    }

    // för att starta programmet
    public void startProgram() throws SQLException {
        System.out.println("Welcome to the Upperud Inc. Database!");
        boolean running = true;
        while (running) {
            menu();
            String choice = ScannerUtil.getUserInput();
            switch (choice) {
                case "1" -> promptForNewWorkRole();
                case "2" -> displayFilteredWorkRoles();
                case "3" -> displaySpecificWorkRole();
                case "4" -> confirmAndDeleteWorkRole();
                case "5" -> promptForWorkRoleUpdate();
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
    // add work role
    private void promptForNewWorkRole() {
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
            workRoleService.addNewWorkRole(newWorkRole);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());

        }
    }
    // show all work roles
    private void displayFilteredWorkRoles() throws SQLException {
        // hämtar alla arbetsroller där salary är högre än 0
        // lägger in dem i en lista
        List<WorkRole> workRoles = workRoleService.filterWorkRolesBySalary(0);
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
    // show specific work role
    private void displaySpecificWorkRole() {
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
    // delete workrole
    private void confirmAndDeleteWorkRole() {
        System.out.println("Enter work role id: ");
        while (true) {
            try {
                int workRoleId = ScannerUtil.readInt();
                // clear scanner
                ScannerUtil.getUserInput();
                boolean deleted = workRoleService.removeWorkRoleById(workRoleId);
                // om deleted = true
                if (deleted) {
                    System.out.println("Work role " + workRoleId + " deleted successfully");
                    break;
                } else {
                    System.out.println("No work role with ID " + workRoleId + " found");
                    System.out.println("Try again");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Enter an valid ID-number");
                ScannerUtil.getUserInput(); // clear scanner
            }

        }


    }
    // update workrole
    private void promptForWorkRoleUpdate() throws SQLException {
        System.out.println("Enter work role id: ");
        int workRoleId = ScannerUtil.readInt();
        ScannerUtil.getUserInput();
        WorkRole workRole = workRoleDAO.fetchWorkRole(workRoleId);
        if (workRole == null) {
            System.out.println("No work role with ID " + workRoleId + " found");
            return;
        }
        // säkerställer att role_id inte ändras
        workRole.setRole_id(workRole.getRole_id());

        System.out.println("If you want to keep the current attribute, leave blank by pressing enter.");

        // ändra titel
        System.out.println("Enter new title: (current: " + workRole.getTitle() + ")");
        String newTitle = ScannerUtil.getUserInput();
        workRoleService.updateTitleInWorkRole(workRole, newTitle);

        // ändra beskrivning
        System.out.println("Enter new description: (current: " + workRole.getDescription() + ")");
        String newDescription = ScannerUtil.getUserInput();
        workRoleService.updateDescriptionInWorkRole(workRole, newDescription);

        // ändra inkomst
        while (true) { // hanterar möjliga inmatningsfel
            System.out.println("Enter new salary (sek): (current: " + workRole.getSalary() + ")");
            String newSalary = ScannerUtil.getUserInput();
            // om inputen inte är blank
            if (!newSalary.isBlank()) {
                // uppdatera rollen
                workRoleService.updateSalaryInWorkRole(workRole, newSalary);
                break;
            // om inputen är blank
            } else if (newSalary.isBlank()) {
                // ingen uppdatering gjord
                System.out.println("No changes to Salary made");
                break;
            } else {
                System.out.println("Invalid format. Use 00.00");
            }
        }


        // ändra kreationsdatum
        while (true) { // hanterar möjliga inmatningsfel med en while-loop
            System.out.println("Enter new creation date: (current: " + workRole.getCreation_date() + ")");
            String newCreationDate = ScannerUtil.getUserInput();
            // om input inte är blank och matchar datumformatet
            if (!newCreationDate.isBlank() && newCreationDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                // uppdatera rollen
                workRoleService.updateCreationDateInWorkRole(workRole, newCreationDate);
                break;
            // om input är blank
            } else if (newCreationDate.isBlank()) {
                // inga ändringar sker
                System.out.println("No changes to Creation Date made");
                break;
            } else {
                System.out.println("Invalid date format. Use yyyy-MM-dd");
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
        EmployeeDAO employeeDAO = new EmployeeDAOImpl();
        // objekt av Employee
        // lägger in user input i argumenten
        Employee employee = employeeDAO.leftJoin(email, password);

        // om det hittas en employee som matchar en i tabellen
        if (employee != null) {
            System.out.println(employee + " | Logged in successfully!");
            System.out.println(employee.getWork_role());

        } else { // om strängarna inte matchar en employee
            System.out.println("Invalid email or password");
        }

    }


}

