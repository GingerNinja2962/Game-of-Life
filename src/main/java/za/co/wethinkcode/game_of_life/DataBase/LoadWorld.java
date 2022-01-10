package za.co.wethinkcode.game_of_life.DataBase;

import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import java.sql.Connection;

public class LoadWorld {

    public static JSONObject loadWorld(int ID, Connection connection) {
        JSONArray worldList = ResultSetToJSON.convert(ListWorlds.listWorlds(connection));
        JSONObject JObject = null;

        for (int i=0; i<worldList.length();i++) {
            JObject = (JSONObject) worldList.get(i);
            if ((int) JObject.get("ID") == ID) {
                return JObject;
            }
        }
        return JObject;
    }
}
