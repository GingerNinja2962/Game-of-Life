package za.co.wethinkcode.game_of_life;

import io.javalin.Javalin;
import za.co.wethinkcode.game_of_life.DataBase.Connect;

import java.sql.Connection;

public class GameServer {
    private final Javalin server;
    private final int DEFAULT_PORT = 8080;
    private final Connection connection = Connect.connect();

    public GameServer() {
        server = Javalin.create();
        server.get("/_ping", context -> context.result("pong"));
        // <SOLUTION>
        WorldApiHandler worldApiHandler = new WorldApiHandler(connection);
        server.post("/world", context -> worldApiHandler.createNew(context));
        // </SOLUTION>
        server.get("/worlds", context -> worldApiHandler.listAll(context));
        // </SOLUTION>
        server.post("/world/{ID}/next", context -> worldApiHandler.nextEpoch(context));
        // </SOLUTION>
        server.post("/worlds/clean", context -> worldApiHandler.clean(context));
        // </SOLUTION>

    }

    public void start(int port) {
        int listen_port = port > 0 ? port : DEFAULT_PORT; // use port if > 0 otherwise use DEFAULT_PORT value
        this.server.start(listen_port);
    }

    public void stop() {
        this.server.stop();
    }

    public static void main(String[] args) {
        // COMPLETE THIS
    }
}
