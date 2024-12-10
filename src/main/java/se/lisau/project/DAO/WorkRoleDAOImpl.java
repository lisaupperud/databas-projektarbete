package se.lisau.project.DAO;

import se.lisau.project.model.WorkRole;
import se.lisau.project.util.JBDCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// hanterar direkt kommunikation med databasen
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
            // sätter värden på parametrarna i sql queryn
            ps.setString(1, workRole.getTitle());
            ps.setString(2, workRole.getDescription());
            ps.setDouble(3, workRole.getSalary());
            ps.setDate(4, workRole.getCreation_date());

            // för att se att en rad uppdateras
            int rowsUpdated = ps.executeUpdate();
            // kör uppdateringen
            JBDCUtil.commit(conn);

            System.out.println("Work role added: " + workRole);
            System.out.println("Rows updated: " + rowsUpdated);
        } catch (SQLException e) {
            // för att ångra ändringen om vi hamnar i catch-blocket
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

        try {
            // hämtar connection
            conn = JBDCUtil.getConnection();
            // sql query
            String sql = "SELECT * FROM work_role";
            // ställer frågan till databasen
            ps = conn.prepareStatement(sql);
            // utför frågan och hämtar resultatet i ett ResultSet
            rs = ps.executeQuery();
            // iterera över resultaten
            while (rs.next()) {
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
            // om ett exception inträffar
            JBDCUtil.rollback(conn);
            e.printStackTrace();
            throw e;
        } finally {
            // stänger resurser
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
        try {
            // hämtar en connection till db
            conn = JBDCUtil.getConnection();
            // sql query
            String sql = "SELECT * FROM work_role WHERE role_id = ?";
            // skickar in frågan till databasen
            ps = conn.prepareStatement(sql);
            // sätter parametern i sql-frågan
            ps.setInt(1, role_id);
            // sparar resultatet i ett ResultSet
            rs = ps.executeQuery();
            // om role_id matchar en rad i tabellen
            if (rs.next()) {
                // skapa en ny WorkRole instans
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
            // stänger resurser
            JBDCUtil.closeResultSet(rs);
            JBDCUtil.closeStatement(ps);
            JBDCUtil.closeConnection(conn);
        }
        // returnerar WorkRole instansen från if-satsen
        return workRole;
    }

    @Override
    public void updateWorkRole(WorkRole workRole) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            // hämtar en connection till db
            conn = JBDCUtil.getConnection();
            // sql query
            String sql = """
                    UPDATE work_role 
                    SET title = ?, description = ?, salary = ?, creation_date = ?
                    WHERE role_id = ?
                    """;
            // skickar frågan till db via PreparedStatement
            ps = conn.prepareStatement(sql);
            // sätter värden på parametrarna i queryn
            ps.setString(1, workRole.getTitle());
            ps.setString(2, workRole.getDescription());
            ps.setDouble(3, workRole.getSalary());
            ps.setDate(4, workRole.getCreation_date());
            ps.setInt(5, workRole.getRole_id());
            // utför uppdateringen
            int rowsUpdated = ps.executeUpdate();
            // commit ändringen
            JBDCUtil.commit(conn);
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
        try {
            conn = JBDCUtil.getConnection();
            // sql query
            String sql = "DELETE FROM work_role WHERE role_id = ?";
            // skickar in frågan
            ps = conn.prepareStatement(sql);
            // säger att ? i sql queryn = 1, hämta rollens id
            ps.setInt(1, workRole.getRole_id());
            // utför uppdateringen
            int rowsUpdated = ps.executeUpdate();
            // commit ändringen
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
