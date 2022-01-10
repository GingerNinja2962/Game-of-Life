package za.co.wethinkcode.game_of_life;

import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.game_of_life.DataBase.CleanTable;
import za.co.wethinkcode.game_of_life.testsupport.JsonRequest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


public class WorldApiTest {
    static final ObjectMapper mapper = new ObjectMapper();
    private static final int TEST_SERVER_PORT = 8081;
    private static final String BASE_URL = "http://localhost:" + TEST_SERVER_PORT;
    private static GameServer server;

    @BeforeAll
    public static void startServer() {
        server = new GameServer();
        server.start(TEST_SERVER_PORT);
    }

    @AfterAll
    public static void stopServer() {
        server.stop();
    }

    @Test // ========== DONE ========== DONE ========== DONE ========== DONE ========== DONE ========== DONE ==========
    @DisplayName("Server status: GET /_ping")
    void testStatusPingReturnsSuccessfulPong() {
        HttpResponse<String> resp = Unirest.get(BASE_URL + "/_ping").asString();
        assertThat(resp.getStatus()).isEqualTo(200);
    }

    @Test // ========== DONE ========== DONE ========== DONE ========== DONE ========== DONE ========== DONE ==========
    @DisplayName("Define new World: POST /world")
    void testPostWithDataDefinesNewWorld() {
        // Arrange
        int[][] worldState = new int[][]{
                {0, 0, 0},
                {0, 1, 0},
                {0, 0, 0}
        };
        JsonRequest defineWorldRequest = JsonRequest.create("DEFINE", Map.of(
                "name", "Cruel World",
                "size", 3,
                "state", worldState
        ));

        // Act:
        HttpResponse<JsonNode> resp = Unirest.post(BASE_URL + "/world")
                .header("content-type", "application/json")
                .body(defineWorldRequest)
                .asJson();

        // Assert:
        assertThat(resp.getStatus()).isEqualTo(201); // https://httpstatuses.com/201
        JSONObject responseJson = resp.getBody().getObject(); // parse JSON response
        System.out.println("Test List All Response: "+resp.getBody()); // Prints out in terminal for safely checking DB
        assertThat(responseJson).isNotNull();
        assertThat(responseJson.getInt("id")).isNotNull();
        assertWorldStatesAreEqual(responseJson.getJSONArray("state"), worldState);
    }

    private void assertWorldStatesAreEqual(JSONArray actualState, int[][] expectedState) {
        for (int i = 0; i < actualState.length(); i++) {
            JSONArray row = actualState.getJSONArray(i); // a row in 2d array
            for (int j = 0; j < row.length(); j++) {
                assertThat(row.getInt(j)).isEqualTo(expectedState[i][j]);
            }
        }
    }

    @Test
    @DisplayName("Next epoch/generation: POST /world/{id}/next")
    void testPostToCalculateNextStateOfWorld() {
        int[][] worldState = new int[][]{
                {1, 0, 0},
                {0, 1, 0},
                {1, 0, 0}
        };
        JsonRequest defineWorldRequest = JsonRequest.create("DEFINE", Map.of(
                "name", "Testing World",
                "size", 3,
                "state", worldState
        ));

        // Act:
        HttpResponse<JsonNode> resp1 = Unirest.post(BASE_URL + "/world")
                .header("content-type", "application/json")
                .body(defineWorldRequest)
                .asJson();

        int[][] new_worldState = new int[][]{
                {0, 1, 0},
                {1, 1, 0},
                {0, 1, 0}
        };
        JsonRequest NextTurnWorldRequest = JsonRequest.create("NEXT", Map.of(
                "name", "Testing World",
                "size", 3,
                "state", worldState
        ));
        HttpResponse<JsonNode> resp2 = Unirest.post(BASE_URL + "/world/1/next")
                .header("content-type", "application/json")
                .body(NextTurnWorldRequest)
                .asJson();

        // Assert:
        assertThat(resp2.getStatus()).isEqualTo(200); // https://httpstatuses.com/201
        JSONObject responseJson = resp2.getBody().getObject(); // parse JSON response
        System.out.println("Test world/ID/Next Response: "+resp2.getBody()); // Prints out in terminal for safely checking DB
        assertThat(responseJson).isNotNull();
        assertThat(responseJson.getInt("id")).isNotNull();
        assertWorldStatesAreEqual(responseJson.getJSONArray("state"), new_worldState);
    }

    @Test // ========== DONE ========== DONE ========== DONE ========== DONE ========== DONE ========== DONE ==========
    @DisplayName("List all worlds: GET /worlds")
    void testListAllWorlds() {
        // COMPLETE THIS TEST
        // Create worlds
        HttpResponse<String> resp = Unirest.get(BASE_URL + "/worlds").asString();
        assertThat(resp.getStatus()).isEqualTo(200); // compare to list of worlds
        System.out.println("Test List All Response: "+resp.getBody()); // Prints out in terminal for safely checking DB
    }

//    @Test
//    @DisplayName("Get world at epoch: GET /world/{id}/epoch/{epoch}")
//    void testGetWorldAtEpoch() {
//        // COMPLETE THIS TEST
//    }
}
