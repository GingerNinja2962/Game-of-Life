package za.co.wethinkcode.game_of_life.DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    public static String database_url = "jdbc:sqlite:SQLite_test_1.db";
    public static Connection connection;

    public static Connection connect() {
        connection = null;
        try {
            connection = DriverManager.getConnection(database_url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }
}
