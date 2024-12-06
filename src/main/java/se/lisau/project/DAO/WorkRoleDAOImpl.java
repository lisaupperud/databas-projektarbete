package se.lisau.project.DAO;

import se.lisau.project.WorkRole;
import se.lisau.project.util.JBDCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorkRoleDAOImpl implements WorkRoleDAO {
    @Override
    public void insertWorkRole(WorkRole workRole) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            // hämtar en connection till db
            conn = JBDCUtil.getConnection();

            // sql query
            String sql = """
                    INSERT INTO work_role
                    (title, description, salary, creation_date)
                    VALUES (?, ?, ?, ?)
                   """;

            // kallar på ps via conn
            // sql är query som skickas till databasen via JDBC
            ps = conn.prepareStatement(sql);
            // ????
            ps.setString(1, workRole.getTitle());
            ps.setString(2, workRole.getDescription());
            ps.setDouble(3, workRole.getSalary());
            ps.setDate(4, workRole.getCreation_date());

            // för att se att en rad uppdateras
            int rowsUpdated = ps.executeUpdate();
            // // kör uppdateringen
            JBDCUtil.commit(conn);

            System.out.println("Work role added: " + workRole);
            System.out.println("Rows updated: " + rowsUpdated);
        } catch (SQLException e) {
            // för att ångra ändrigen om vi hamnar i catch-blocket
            JBDCUtil.rollback(conn);
            e.printStackTrace();
            throw e;
        } finally {
            // stänger resurserna
            JBDCUtil.closeStatement(ps);
            JBDCUtil.closeConnection(conn);
        }
    }

    @Override
    public List<WorkRole> getWorkRoles() throws SQLException {
        // lista för att samla alla work roles
        List<WorkRole> workRoles = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            conn = JBDCUtil.getConnection();
            String sql = "SELECT * FROM work_role";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            // iterera över resultaten
            while(rs.next()){
                // skapar en ny instans av WorkRole för varje iteration
                WorkRole workRole = new WorkRole(
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDouble("salary"),
                        rs.getDate("creation_date"));
                // lägger till instansen i listan
                workRoles.add(workRole);
            }
        } catch (SQLException e) {
            JBDCUtil.rollback(conn);
            e.printStackTrace();
            throw e;
        } finally {
            JBDCUtil.closeResultSet(rs);
            JBDCUtil.closeStatement(ps);
            JBDCUtil.closeConnection(conn);
        }

        // returnera listan
        return workRoles;
    }

    @Override
    public WorkRole getWorkRole(int role_id) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        WorkRole workRole = null;
        try{
            conn = JBDCUtil.getConnection();
            String sql = "SELECT * FROM work_role WHERE role_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, role_id);
            rs = ps.executeQuery();
            if(rs.next()){
                workRole = new WorkRole(
                        rs.getInt("role_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDouble("salary"),
                        rs.getDate("creation_date")
                );
            }
        } catch (SQLException e) {
            JBDCUtil.rollback(conn);
            e.printStackTrace();
            throw e;
        } finally {
            JBDCUtil.closeResultSet(rs);
            JBDCUtil.closeStatement(ps);
            JBDCUtil.closeConnection(conn);
        }
        return workRole;
    }

    @Override
    public void updateWorkRole(WorkRole workRole) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            conn = JBDCUtil.getConnection();
            String sql = """
                    UPDATE work_role 
                    SET title = ?, description = ?, salary = ?, creation_date = ?
                    WHERE role_id = ?
                    """;
            ps = conn.prepareStatement(sql);
            ps.setString(1, workRole.getTitle());
            ps.setString(2, workRole.getDescription());
            ps.setDouble(3, workRole.getSalary());
            ps.setDate(4, workRole.getCreation_date());
            ps.setInt(5, workRole.getRole_id());
            int rowsUpdated = ps.executeUpdate();
            // commitar ändrigen
            JBDCUtil.commit(conn);
            System.out.println("Work role updated: " + workRole);
            System.out.println("Rows updated: " + rowsUpdated);
        } catch (SQLException e) {
            JBDCUtil.rollback(conn);
            e.printStackTrace();
            throw e;
        } finally {
            JBDCUtil.closeStatement(ps);
            JBDCUtil.closeConnection(conn);

        }

    }

    @Override
    public void deleteWorkRole(WorkRole workRole) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            conn = JBDCUtil.getConnection();
            String sql = "DELETE FROM work_role WHERE role_id = ?";
            ps = conn.prepareStatement(sql);
            // vad gör detta?
            ps.setInt(1, workRole.getRole_id());
            int rowsUpdated = ps.executeUpdate();
            JBDCUtil.commit(conn);
            System.out.println("Work role deleted: " + workRole);
            System.out.println("Rows updated: " + rowsUpdated);
        } catch (SQLException e) {
            JBDCUtil.rollback(conn);
            e.printStackTrace();
            throw e;
        } finally {
            JBDCUtil.closeStatement(ps);
            JBDCUtil.closeConnection(conn);
        }

    }
}
