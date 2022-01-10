package za.co.wethinkcode.game_of_life.http.responses;

import com.fasterxml.jackson.annotation.JsonCreator;

public class NewTurnResponse {
    private final int id;
    private final String name;
    private final int epoch;
    private final int[][] state;

    @JsonCreator
    public NewTurnResponse(final int worldId, final String name, final int epoch,final int[][] state) {
        this.id = worldId;
        this.name = name;
        this.epoch = epoch;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public String getName(){
        return name;
    }

    public int[][] getState() {
        return state;
    }

    public int getEpoch(){
        return epoch;
    }
}
