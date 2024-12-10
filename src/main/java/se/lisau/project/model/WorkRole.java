package se.lisau.project.model;

import java.sql.Date;
import java.util.Objects;

public class WorkRole {
    // attribut som matchar kolumnerna i tabellen work_role
    private int role_id;
    private String title;
    private String description;
    private double salary;
    private Date creation_date;

    // konstruktor för att hämta från databasen
    public WorkRole(int role_id, String title, String description, double salary, Date creation_date) {
        this.role_id = role_id;
        this.title = title;
        this.description = description;
        this.salary = salary;
        this.creation_date = creation_date;
    }

    // konstruktor för att skapa instanser
    public WorkRole(String title, String description, double salary, Date creation_date) {
        this.title = title;
        this.description = description;
        this.salary = salary;
        this.creation_date = creation_date;
    }

    // get och set
    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    // toString
    @Override
    public String toString() {
        return "Title: " + title + " | Description: " + description + " | Salary: " + salary + " | Creation Date: " + creation_date;
    }

    // equals och hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkRole workRole = (WorkRole) o;
        return role_id == workRole.role_id && Double.compare(salary, workRole.salary) == 0 && Objects.equals(title, workRole.title) && Objects.equals(description, workRole.description) && Objects.equals(creation_date, workRole.creation_date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(role_id, title, description, salary, creation_date);
    }
}
