package se.lisau.project.service;


import se.lisau.project.DAO.WorkRoleDAO;
import se.lisau.project.model.WorkRole;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// ansvarar för hantering av affärslogik och validering
public class WorkRoleService {
    // interface-typ för DAO-lagret
    private final WorkRoleDAO workRoleDAO;


    // dependency injection via konstruktorn
    public WorkRoleService(WorkRoleDAO workRoleDAO) {
        this.workRoleDAO = workRoleDAO;
    }

    // validerar data innan den läggs till i databasen
    // kontrollerar att angivna värden inte är null eller 0
    public void insertWorkRole(WorkRole workRole) throws SQLException, IllegalArgumentException {
        if (workRole.getTitle() == null || workRole.getTitle().isEmpty()) {
            throw new SQLException("Title can't be null or empty");
        }
        if (workRole.getDescription() == null || workRole.getDescription().isEmpty()) {
            throw new SQLException("Description can't be null or empty");
        }
        if (workRole.getSalary() < 0) {
            throw new IllegalArgumentException("Salary must be greater than 0");
        }
        if (workRole.getCreation_date() == null) {
            throw new IllegalArgumentException("Creation date can't be null");
        }
        workRoleDAO.insertWorkRole(workRole);
    }

    public List<WorkRole> getAllWorkRoles(double salaryThreshold) throws SQLException {
        List<WorkRole> allWorkRoles = workRoleDAO.getWorkRoles();
        List<WorkRole> filteredWorkRoles = new ArrayList<>();
        for (WorkRole workRole : allWorkRoles) {
            if (workRole.getSalary() > salaryThreshold) {
                filteredWorkRoles.add(workRole);
            }
        }
        return filteredWorkRoles; // returnerar bara roller med lön högre än threshold
    }

    // hanterar eventuella undantag
    public WorkRole getWorkRoleById(int id) {
        try {
            return workRoleDAO.getWorkRole(id);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }

    }

    public boolean deleteWorkRoleById(int id) {
        try {
            WorkRole workRole = getWorkRoleById(id);
            if (workRole != null) {
                workRoleDAO.deleteWorkRole(workRole);
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public void updateTitleInWorkRole(WorkRole workRole, String newTitle) throws SQLException {
        if (!newTitle.isEmpty()) {
            workRole.setTitle(newTitle);
            workRoleDAO.updateWorkRole(workRole);
            System.out.println("New Title: " + newTitle);
        } else {
            System.out.println("No changes to Title made");
        }
    }

    public void updateDescriptionInWorkRole(WorkRole workRole, String newDescription) throws SQLException {
        if (!newDescription.isEmpty()) {
            workRole.setDescription(newDescription);
            workRoleDAO.updateWorkRole(workRole);
            System.out.println("New Description: " + newDescription);
        } else {
            System.out.println("No changes to Description made");
        }
    }

    public void updateSalaryInWorkRole(WorkRole workRole, String newSalary) throws SQLException {
        if (!newSalary.isBlank()) {
            try {
                workRole.setSalary(Double.parseDouble(newSalary));
                workRoleDAO.updateWorkRole(workRole);
                System.out.println("New Salary: " + newSalary);
            } catch (NumberFormatException e) {
                System.out.println("Invalid format. Try 00.00");
            }
        } else {
            System.out.println("No changes to Salary made");
        }
    }

    public void updateCreationDateInWorkRole(WorkRole workRole, String newCreationDate) throws SQLException {
        Date date = Date.valueOf(newCreationDate);
        workRole.setCreation_date(date);
        workRoleDAO.updateWorkRole(workRole);
        System.out.println("New Creation Date: " + newCreationDate);

    }


}
