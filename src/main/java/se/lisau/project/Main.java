package se.lisau.project;

import se.lisau.project.UI.UserInteraction;
import se.lisau.project.util.ScannerUtil;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        UserInteraction userInteraction = new UserInteraction();
        userInteraction.startProgram();
        ScannerUtil.closeScanner();
    }
}
