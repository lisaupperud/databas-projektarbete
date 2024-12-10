package se.lisau.project.model;

import java.util.Objects;

public class Employee {
    // attribut som matchar kolumnerna i tabellen employee
    private final int employee_id;
    private final String name;
    private final String email;
    private final String password;
    private final WorkRole work_role;

    // för att hämta information från db
    public Employee(int employee_id, String name, String email, String password, WorkRole work_role) {
        this.employee_id = employee_id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.work_role = work_role;
    }

    // ingen konstruktor för att skapa instanser då detta inte behövs


    // endast en getter för work_role då det är det enda attributet som måste hämtas
    public WorkRole getWork_role() {
        return work_role;
    }


    @Override
    public String toString() {
        return "Employee: " + employee_id + " | Name: " + name + " | E-mail: " + email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return employee_id == employee.employee_id && Objects.equals(name, employee.name) && Objects.equals(email, employee.email) && Objects.equals(password, employee.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employee_id, name, email, password);
    }
}
