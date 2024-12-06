package se.lisau.project;

import se.lisau.project.util.JBDCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeDAO {

    public Employee leftJoin(String email, String password) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Employee employee = null;

        try{
            conn = JBDCUtil.getConnection();
            //int employeeId = 1;

            String sql = """
                    SELECT e.employee_id, e.name AS employee_name, e.email AS employee_email, e.password AS employee_password,
                    w.role_id, w.title AS role_title, w.description AS role_description, w.salary AS role_salary, w.creation_date AS role_creation_date
                    FROM employee e
                    LEFT JOIN work_role w
                    ON e.role_id = w.role_id
                    WHERE e.email = ? AND e.password = ?
                    """;
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);

            rs = ps.executeQuery();
            if(rs.next()){
                WorkRole workRole = new WorkRole(
                        rs.getInt("role_id"),
                        rs.getString("role_title"),
                        rs.getString("role_description"),
                        rs.getDouble("role_salary"),
                        rs.getDate("role_creation_date")
                );
                employee = new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("employee_name"),
                        rs.getString("employee_email"),
                        rs.getString("employee_password"),
                        workRole
                );
            }
        } catch (SQLException e){
            JBDCUtil.rollback(conn);
            e.printStackTrace();
            throw e;
        } finally {
            JBDCUtil.closeResultSet(rs);
            JBDCUtil.closeStatement(ps);
            JBDCUtil.closeConnection(conn);
        }
        return employee;

    }
}
