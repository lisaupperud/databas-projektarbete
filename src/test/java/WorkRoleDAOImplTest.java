import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import se.lisau.project.DAO.WorkRoleDAO;
import se.lisau.project.DAO.WorkRoleDAOImpl;
import se.lisau.project.model.WorkRole;
import se.lisau.project.service.WorkRoleService;
import se.lisau.project.util.JBDCUtil;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WorkRoleDAOImplTest {
    // för att ta bort tabellen efter varje test
    // så att tidigare test inte påverkar
    @AfterEach
    public void tearDown() {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = JBDCUtil.getConnection();
            stmt = conn.createStatement();
            // statement = ta bort tabellen
            stmt.execute("DROP TABLE IF EXISTS workrole");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JBDCUtil.closeStatement(stmt);
            JBDCUtil.closeConnection(conn);
        }
    }

    // varför behövs denna?
    // kolla test-lektion
    @Test
    void testGetConnection() {
        try {
            JBDCUtil.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testInsertWorkRole() throws SQLException {
        WorkRoleDAO dao = new WorkRoleDAOImpl();
        WorkRoleService workRoleService = new WorkRoleService(dao);
        WorkRole workrole = new WorkRole("CEO", "Cheif Executive Officer", 150000, Date.valueOf("2024-12-05"));
        try {
            workRoleService.insertWorkRole(workrole);
            List<WorkRole> workRoleList = workRoleService.getAllWorkRoles(0);
            for (WorkRole workRole : workRoleList) {
                System.out.println(workRole.getTitle());
                assertEquals(1, workRoleList.size());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getWorkRoles() throws SQLException {
        WorkRoleDAO dao = new WorkRoleDAOImpl();
        WorkRole workRole = new WorkRole("CEO", "Cheif Executive Officer", 150000, Date.valueOf("2024-12-05"));
        dao.insertWorkRole(workRole);
        List<WorkRole> workRoles = dao.getWorkRoles();
        for (WorkRole workrole : workRoles) {
            System.out.println(workrole.getTitle());
        }
        // kontrollerar att listan inte är null
        assertNotNull(workRoles);


    }

}
