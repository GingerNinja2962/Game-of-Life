package za.co.wethinkcode.game_of_life.DataBase;

import java.sql.*;

public class CleanTable {
    public static String sql_statement = "DELETE FROM Worlds";

    public static void cleanTable(Connection connection) {
        try {
            DatabaseMetaData DbMd = connection.getMetaData();
            ResultSet tables = DbMd.getTables(null, null, "Worlds", null);

            if (!tables.next()) {
                CreateTable.createTable(connection);
            }

            Statement statement = connection.createStatement();
            statement.executeQuery(sql_statement);
        } catch (SQLException e) {
        }
    }
}
