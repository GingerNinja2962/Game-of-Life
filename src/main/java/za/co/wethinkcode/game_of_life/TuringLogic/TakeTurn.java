package za.co.wethinkcode.game_of_life.TuringLogic;

import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import za.co.wethinkcode.game_of_life.DataBase.UpdateWorld;

import java.sql.Connection;
import java.util.HashMap;

public class TakeTurn {

    public static int[][] checkAlive(int[][] worldState){
        int[][] new_worldState = new int[worldState.length][worldState[0].length];
        int neighbors = 0;

        for (int i = 0; i<worldState.length;i++) {
            for (int j = 0; j<worldState[i].length;j++) {
                neighbors = neighborCount(worldState, i, j);

                if (neighbors < 2 || neighbors > 3) { // Cell Dies
                    new_worldState[i][j] = 0;
                } else { // Cell Lives
                    new_worldState[i][j] = 1;
                }
            }
        }
        return new_worldState;
    }


    private static int neighborCount(int[][] worldState, int column, int row){
        int neighbors = 0;
        if (column != 0) { // Top Center
            neighbors += worldState[column-1][row];
        } if (column != worldState.length-1) { // Bottom Center
            neighbors += worldState[column+1][row];
        } if (row != 0) { // Left Center
            neighbors += worldState[column][row-1];
        } if (row != worldState[0].length-1) { // Right Center
            neighbors += worldState[column][row+1];
        }
        if (column != 0 && row != 0) { // Top Left
            neighbors += worldState[column-1][row-1];
        } if (column != 0 && row != worldState[0].length-1) { // Top Right
            neighbors += worldState[column-1][row+1];
        } if (column != worldState.length-1 && row != 0) { // Bottom Left
            neighbors += worldState[column+1][row-1];
        } if (column != worldState.length-1 && row != worldState[0].length-1) { // Bottom Right
            neighbors += worldState[column+1][row+1];
        }
        return neighbors;
    }


    public static HashMap<Object, Object> advanceOneEpoch(JSONObject worldData, Connection connection) {
        JSONArray JArray = worldData.getJSONArray("State");
        int[][] worldState = new int[worldData.getInt("Size")][worldData.getInt("Size")];

        for (int i = 0; i<JArray.length();i++) {
            for (int j = 0; j<JArray.getJSONArray(i).length();j++) {
                worldState[i][j] = JArray.getJSONArray(i).getInt(j);
            }
        }

        HashMap<Object, Object> newWorldState = new HashMap<Object, Object>();
        newWorldState.put("ID",worldData.get("ID"));
        newWorldState.put("Epoch",(int) worldData.get("Epoch")+1);
        newWorldState.put("Name",worldData.get("Name"));
        newWorldState.put("Size",worldData.get("Size"));
        newWorldState.put("State",checkAlive(worldState));

        UpdateWorld.updateWorld((int) newWorldState.get("ID"), (int) newWorldState.get("Epoch"),
                (int[][]) newWorldState.get("State"), connection);

        return newWorldState;
    }


    public static HashMap<Object, Object> advanceMultipleEpochs(JSONObject worldData, int epochsToDo, Connection connection){
        HashMap<Object, Object> newWorldData = new HashMap<Object, Object>();
        while (epochsToDo > 0) {
            newWorldData = advanceOneEpoch(worldData, connection);
            worldData.remove("ID");
            worldData.put("ID", (int) newWorldData.get("ID"));
            worldData.remove("Epoch");
            worldData.put("Epoch", (int) newWorldData.get("Epoch"));
            worldData.remove("Name");
            worldData.put("Name", (String) newWorldData.get("Name"));
            worldData.remove("Size");
            worldData.put("Size", (int) newWorldData.get("Size"));
            worldData.remove("State");
            worldData.put("State", (int[][]) newWorldData.get("State"));
            epochsToDo--;
        }
        return newWorldData;
    }

}
