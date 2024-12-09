package se.lisau.project.DAO;

import se.lisau.project.model.Employee;

import java.sql.SQLException;

public interface EmployeeDAO {
    Employee leftJoin(String email, String password) throws SQLException;
}
