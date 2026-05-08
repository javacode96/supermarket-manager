package main;

// Autor java code
import java.sql.*;

public class DatabaseConnection {

    // Connection data
    private static final String HOST     = "localhost";
    private static final String PORT     = "3306";
    private static final String DATABASE = "supermercado_la_torre";
    private static final String USER     = "root";
    private static final String PASSWORD = "123456";

    private static final String URL =
        "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE +
        "?useSSL=false&useUnicode=true&characterEncoding=UTF-8";

    // Get connection
    public static Connection getConnection() {
        Connection cn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            cn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println("Connection error: " + e.getMessage());
        }
        return cn;
    }
}

