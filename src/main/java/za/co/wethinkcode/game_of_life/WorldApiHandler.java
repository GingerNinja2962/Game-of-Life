package za.co.wethinkcode.game_of_life;

import io.javalin.http.Context;
import kong.unirest.HttpStatus;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import za.co.wethinkcode.game_of_life.DataBase.*;
import za.co.wethinkcode.game_of_life.TuringLogic.TakeTurn;
import za.co.wethinkcode.game_of_life.domain.World;
import za.co.wethinkcode.game_of_life.http.requests.CreateRequest;
import za.co.wethinkcode.game_of_life.http.responses.NewTurnResponse;
import za.co.wethinkcode.game_of_life.http.responses.WorldResponse;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class WorldApiHandler {
    private final Connection connection;

    WorldApiHandler(Connection connection){
        this.connection = connection;
    }

    public void createNew(Context context) {
        CreateRequest createRequest = context.bodyAsClass(CreateRequest.class);
        World world = World.define(createRequest.getWorldName(), createRequest.getWorldSize(), createRequest.getWorldInitialState());

        int ID = SaveWorld.saveWorld(0, world.getName(), world.getSize(), world.getState(), connection);

        context.status(HttpStatus.CREATED);
        context.json(new WorldResponse(ID, 0 ,world.getState()));
    }


    public void listAll(Context context) {
        context.status(HttpStatus.OK);
        context.json(ResultSetToJSON.convert(ListWorlds.listWorlds(connection)).toString());
    }


    public void nextEpoch(Context context) {
        String ID = context.pathParam("ID");
        JSONObject JObject = LoadWorld.loadWorld(Integer.parseInt(ID), connection);

        HashMap<Object, Object> NewMappedObject = TakeTurn.advanceOneEpoch(JObject, connection);
        context.status(HttpStatus.OK);

        context.json(new NewTurnResponse((int) NewMappedObject.get("ID"), (String) NewMappedObject.get("Name"),
                (int) NewMappedObject.get("Epoch"), (int[][]) NewMappedObject.get("State")));
    }


    public void clean(Context context) {
        CleanTable.cleanTable(connection);
        context.status(HttpStatus.OK);
    }
}
// <SOLUTION>
