package se.lisau.project.DAO;

import se.lisau.project.model.Employee;
import se.lisau.project.model.WorkRole;
import se.lisau.project.util.JBDCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// hanterar databas-logiken för Employee
public class EmployeeDAOImpl implements EmployeeDAO {

    public Employee leftJoin(String email, String password) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Employee employee = null;

        try {
            conn = JBDCUtil.getConnection();
            // varför behövs denna? programmet funkar ju utan?
            //int employeeId = 1;

            // sql query med LEFT JOIN
            String sql = """
                    SELECT e.employee_id, e.name AS employee_name, e.email AS employee_email, e.password AS employee_password,
                    w.role_id, w.title AS role_title, w.description AS role_description, w.salary AS role_salary, w.creation_date AS role_creation_date
                    FROM employee e
                    LEFT JOIN work_role w
                    ON e.role_id = w.role_id
                    WHERE e.email = ? AND e.password = ?
                    """;
            // skickar in frågan
            ps = conn.prepareStatement(sql);
            // sätter parametrarna i frågan
            ps.setString(1, email);
            ps.setString(2, password);

            // utför frågan
            rs = ps.executeQuery();
            if (rs.next()) {
                // WorkRole instans
                WorkRole workRole = new WorkRole(
                        rs.getInt("role_id"),
                        rs.getString("role_title"),
                        rs.getString("role_description"),
                        rs.getDouble("role_salary"),
                        rs.getDate("role_creation_date")
                );
                // employee instans
                employee = new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("employee_name"),
                        rs.getString("employee_email"),
                        rs.getString("employee_password"),
                        //employees tillhörande WorkRole
                        workRole
                );
            }
        } catch (SQLException e) {
            JBDCUtil.rollback(conn);
            e.printStackTrace();
            throw e;
        } finally {
            // stänger resurser
            JBDCUtil.closeResultSet(rs);
            JBDCUtil.closeStatement(ps);
            JBDCUtil.closeConnection(conn);
        }
        // returnerar employee
        return employee;

    }
}
