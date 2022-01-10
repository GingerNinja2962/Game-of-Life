package za.co.wethinkcode.game_of_life.http.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class CreateRequest {
    private static final ObjectMapper mapper = new ObjectMapper();
    private String command;
    private RequestData data;

    @JsonIgnore
    private String worldName;
    @JsonIgnore
    private Integer worldSize;
    @JsonIgnore
    private int[][] worldInitialState;

    @SuppressWarnings("unused")
    @JsonProperty("data")
    private void unpackData(JsonNode data) throws IOException {
        mapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);

        this.worldName = data.get("name").asText();
        this.worldSize = data.get("size").asInt();
        this.worldInitialState = mapper.readValue(data.withArray("state").toString(), int[][].class);
    }


    public String getWorldName() {
        return this.worldName;
    }

    public Integer getWorldSize() {
        return this.worldSize;
    }

    public int[][] getWorldInitialState() {
        return this.worldInitialState;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
