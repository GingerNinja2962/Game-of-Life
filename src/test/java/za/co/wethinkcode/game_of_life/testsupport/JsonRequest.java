package za.co.wethinkcode.game_of_life.testsupport;

import java.util.Map;

public class JsonRequest {
    private String command;
    private Map<String, Object> data;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public static JsonRequest create(String command, Map<String, Object> data) {
        JsonRequest jsonRequest = new JsonRequest();
        jsonRequest.setCommand(command);
        jsonRequest.setData(data);
        return jsonRequest;
    }
}
