package za.co.wethinkcode.game_of_life.DataBase;


import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import java.sql.*;

public class SaveWorld {
    public static String sql = "INSERT INTO Worlds(ID, Epoch, Name, Size, State) VALUES(?,?,?,?,?)";


    public static int saveWorld(int epoch, String name, int Size, int[][] world, Connection connection) {
        int ID = 0;
        try {
            DatabaseMetaData DbMd = connection.getMetaData();
            ResultSet tables = DbMd.getTables(null, null, "Worlds", null);

            if (!tables.next()) {
                CreateTable.createTable(connection);
            }

            JSONObject json = new JSONObject();

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(2, epoch);
            statement.setString(3, name);
            statement.setInt(4, Size);
            statement.setString(5, json.put("WorldState", new JSONArray(world)).toString());
            statement.executeUpdate();
            ResultSet Rs = ListWorlds.listWorlds(connection);
            String RsName = null;
            while (RsName != name && Rs.next()) {
                ID = Rs.getInt("ID");
                RsName = Rs.getString("name");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
        return ID;
    }
}