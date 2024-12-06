package se.lisau.project;

import java.util.Objects;

public class Employee {
    private int employee_id;
    private String name;
    private String email;
    private String password;
    private WorkRole work_role;

    // för att hämta information från db
    public Employee(int employee_id, String name, String email, String password, WorkRole work_role) {
        this.employee_id = employee_id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.work_role = work_role;
    }

    // för att skapa instanser
    public Employee(String name, String email, String password, WorkRole work_role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.work_role = work_role;
    }

    // get och set
    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public WorkRole getWork_role() {
        return work_role;
    }
    public void setWork_role(WorkRole work_role) {
        this.work_role = work_role;
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
