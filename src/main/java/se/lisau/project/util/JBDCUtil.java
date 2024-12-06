package se.lisau.project.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class JBDCUtil {
    // instans av Properties
    private static final Properties properties = new Properties();

    // körs varje gång klassens skapas upp/ används
    // liknas vid en konstruktor
    // läser in properties från filen 'application.properties'
    // antingen för databasen eller h2-databasen
    static {
        // läser in från a.p-filen via InputStream
        try(InputStream input = JBDCUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            if(input == null){
                throw new IOException("Could not find 'application.properties' file");
            }
            properties.load(input);
            //System.out.println("Input från filen fungerar");
        } catch (IOException e){
            e.printStackTrace();
            throw new ExceptionInInitializerError("Failed to load database properties");
        }
    }

    // hämtar en connection, ansluter till db
    public static Connection getConnection() throws SQLException {
        // implementation av Driver-if
        Driver hsqlDriver = new org.hsqldb.jdbcDriver();
        DriverManager.registerDriver(hsqlDriver);

        String dbUrl = properties.getProperty("db.Url");
        String dbUser = properties.getProperty("db.userId");
        String dbPassword = properties.getProperty("db.password");

        Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        // stänger av autoCommit
        conn.setAutoCommit(false);

        // metoden returnerar en connection
        return conn;
    }

    // stänger aktiv databasanslutning
    public static void closeConnection(Connection conn) {
        try{
            // om conn inte är null, stäng av
            if(conn != null){
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // stänger aktivt Statement-objekt
    public static void closeStatement(Statement stmt) {
        try{
            // om stmt inte är null
            if(stmt != null){
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // stänger aktivt ResultSet-objekt
    public static void closeResultSet(ResultSet rs) {
        try{
            // om rs inte är null
            if(rs != null){
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // bekräftar och sparar ändringar i db
    // behövs pga autoCommit = av
    public static void commit(Connection conn){
        try{
            if(conn != null){
                conn.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // ångrar ändringar i db om något går fel
    public static void rollback(Connection conn){
        try{
            if(conn != null){
                conn.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // metod för att 'pinga' databasen
    public static String getDatabaseProductName(Connection conn){
        try{
            DatabaseMetaData metadata = conn.getMetaData();
            return metadata.getDatabaseProductName();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }
    public static void main(String[] args) {
        try(Connection conn = getConnection()){
            String dbName = getDatabaseProductName(conn);
            System.out.println("Database: " + dbName);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }


}
