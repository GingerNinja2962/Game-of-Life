package za.co.wethinkcode.game_of_life.DataBase;

import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetToJSON {

    public static JSONArray convert(ResultSet Rs){
        JSONArray JArray = new JSONArray();

        try {
            while (Rs.next()) {
                JSONObject JObject = new JSONObject();

                JObject.put("ID", Rs.getInt("ID"));
                JObject.put("Epoch", Rs.getInt("Epoch"));
                JObject.put("Name", Rs.getString("Name"));
                JObject.put("Size", Rs.getInt("Size"));
                JObject.put("State", new JSONObject(Rs.getString("State")).getJSONArray("WorldState"));
                JArray.put(JObject);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return JArray;
    }
}
