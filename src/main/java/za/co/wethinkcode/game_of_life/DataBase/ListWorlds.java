package za.co.wethinkcode.game_of_life.DataBase;

import java.sql.*;

public class ListWorlds {
    public static String sql_statement = "SELECT * FROM Worlds";
    public static ResultSet db_results = null;

    public static ResultSet listWorlds(Connection connection){
        try {
            DatabaseMetaData DbMd = connection.getMetaData();
            ResultSet tables = DbMd.getTables(null, null, "Worlds", null);

            if (!tables.next()) {
                CreateTable.createTable(connection);
            }

            Statement statement  = connection.createStatement();
            db_results = statement.executeQuery(sql_statement);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return db_results;
    }
}
