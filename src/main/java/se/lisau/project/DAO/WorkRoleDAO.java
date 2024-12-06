package se.lisau.project.DAO;

import se.lisau.project.WorkRole;

import java.sql.SQLException;
import java.util.List;

public interface WorkRoleDAO {
    // lägg till arbetsroll
    void insertWorkRole(WorkRole workRole) throws SQLException;

    // hämta alla arbetsroller
    List<WorkRole> getWorkRoles() throws SQLException;

    // hämta en arbetsroll
    WorkRole getWorkRole(int id) throws SQLException;

    // uppdatera arbetsroll
    void updateWorkRole(WorkRole workRole) throws SQLException;

    // ta bort arbetsroll
    void deleteWorkRole(WorkRole workRole) throws SQLException;
}
