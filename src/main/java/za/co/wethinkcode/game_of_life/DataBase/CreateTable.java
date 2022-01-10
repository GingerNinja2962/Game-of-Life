package za.co.wethinkcode.game_of_life.DataBase;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {
    public static String sql_statement = "CREATE TABLE IF NOT EXISTS Worlds (\n"
            + " ID INTEGER NOT NULL,\n"
            + " Epoch INTEGER NOT NULL DEFAULT 0,\n"
            + " Name TEXT NOT NULL,\n"
            + " Size INTEGER NOT NULL,\n"
            + " State TEXT NOT NULL DEFAULT'[[0]]',\n"
            + " PRIMARY KEY(\"ID\")\n"
            + ");";

    public static boolean createTable(Connection connection){
        try{
            Statement statement = connection.createStatement();
            statement.execute(sql_statement);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
}
