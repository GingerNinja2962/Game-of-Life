package za.co.wethinkcode.game_of_life.DataBase;

import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import java.sql.*;

public class UpdateWorld {

    public static void updateWorld(int ID, int epoch, int[][] world, Connection connection) {
        String sql = "UPDATE Worlds SET Epoch = ?, State = ? WHERE ID = ?;";
        try {
            JSONObject json = new JSONObject();

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, epoch);
            statement.setString(2, json.put("WorldState", new JSONArray(world)).toString());
            statement.setInt(3, ID);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}