package za.co.wethinkcode.game_of_life;

import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import za.co.wethinkcode.game_of_life.DataBase.CleanTable;
import za.co.wethinkcode.game_of_life.ProblemDriverSupport.JsonRequest;

import java.util.Map;

public class ProblemDriver {
    static final ObjectMapper mapper = new ObjectMapper();
    private static final int TEST_SERVER_PORT = 8081;
    private static final String BASE_URL = "http://localhost:" + TEST_SERVER_PORT;
    private static GameServer server;


    private static void stopServer() {
        server.stop();
    }


    private static void startServer() {
        server = new GameServer();
        server.start(TEST_SERVER_PORT);
    }


    private static JSONArray intAtoJArray(int[][] intArray){
        JSONArray JArray0 = new JSONArray();
        for (int i=0; i< intArray.length; i++) {
            JSONArray JArray1 = new JSONArray();
            for(int j=0; j< intArray[i].length; j++) {
                JArray1.put(intArray[i][j]);
            }
            JArray0.put(JArray1);
        }
        return JArray0;
    }


    private static void cleanDB() {
        HttpResponse<String> resp = Unirest.post(BASE_URL + "/worlds/clean").asString();
        System.out.println("Cleaning out DataBase: (Response should be 200)\n\t\t"+resp.getStatus());
    }


    private static void testStatusPingReturnsSuccessfulPong() {
        HttpResponse<String> resp = Unirest.get(BASE_URL + "/_ping").asString();
        System.out.println("Test of server ping: (Response should be 200)\n\t\t"+resp.getStatus());
    }


    private static void testPostWithDataDefinesNewWorld() {
        int[][] worldState = new int[][]{
                {1, 0, 1},
                {0, 1, 0},
                {1, 0, 1}};
        JsonRequest defineWorldRequest = JsonRequest.create("DEFINE", Map.of(
                "name", "Cruel World",
                "size", 3,
                "state", worldState));
        HttpResponse<JsonNode> resp = Unirest.post(BASE_URL + "/world")
                .header("content-type", "application/json")
                .body(defineWorldRequest)
                .asJson();

        System.out.println("Test Post with data DEFINE should make a new world: (Response should be 201)\n\t\t"+resp.getStatus());
        System.out.println("The Response data should be\n\t"+intAtoJArray(worldState)+"\nThe data is:\n\t"+resp.getBody().getObject().get("state"));
    }


    private static void testPostToCalculateNextStateOfWorld() {
        int[][] worldState = new int[][]{
                {1, 0, 1},
                {0, 1, 0},
                {1, 0, 1}};
        JsonRequest defineWorldRequest = JsonRequest.create("DEFINE", Map.of(
                "name", "Testing World",
                "size", 3,
                "state", worldState));
        HttpResponse<JsonNode> resp0 = Unirest.post(BASE_URL + "/world")
                .header("content-type", "application/json")
                .body(defineWorldRequest)
                .asJson();
        System.out.println("+================================================================+");
        System.out.println("|\t\t\t\t\tSEEDED world RESPONSE\t\t\t\t\t\t |");
        System.out.println("| The Response Code from the server: (Should be 201)\t\t\t |");
        System.out.println("|\t\t"+resp0.getStatus()+"\t\t\t\t\t\t\t\t\t\t\t\t\t\t |");
        System.out.println("| The Response for POST of new world:\t\t\t\t\t\t\t |");
        System.out.println("|\t\t"+resp0.getBody()+"\t |");
        System.out.println("+================================================================+");
        JSONObject responseJson = resp0.getBody().getObject();

        int[][] new_worldState = new int[][]{
                {0, 1, 0},
                {1, 0, 1},
                {0, 1, 0}};
        JsonRequest NextTurnWorldRequest = JsonRequest.create("NEXT", Map.of(
                "name", "Testing World",
                "size", 3,
                "state", worldState));
        HttpResponse<JsonNode> resp1 = Unirest.post(BASE_URL + "/world/"+responseJson.getInt("id")+"/next")
                .header("content-type", "application/json")
                .body(NextTurnWorldRequest)
                .asJson();
        System.out.println("+================================================================+");
        System.out.println("|\t\t\t\t\tNEXT epoch world RESPONSE\t\t\t\t\t |");
        System.out.println("| The Response Code from the server: (Should be 201)\t\t\t |");
        System.out.println("|\t\t"+resp1.getStatus()+"\t\t\t\t\t\t\t\t\t\t\t\t\t\t |");
        System.out.println("| The Response for POST of next epoch world:\t\t\t\t\t |");
        System.out.println("| Should be:   "+intAtoJArray(new_worldState)+"\t\t\t\t\t\t |");
        System.out.println("| Actually is: "+resp1.getBody().getObject().get("state")+"\t\t\t\t\t\t |");
//        System.out.println("|\t\t"+resp0.getBody()+"\t |");
        System.out.println("+================================================================+");

//        System.out.println("The Response Code from the server: (Should be 200)\n\t\t"+resp1.getStatus());
//        System.out.println("The next Epoch given from the server This should be:\n"+intAtoJArray(new_worldState)+"\n"+resp1.getBody().getObject().get("state"));
    }


    private static void testGetWorldsReturnsListOfWorlds() {
        HttpResponse<JsonNode> resp = Unirest.get(BASE_URL + "/worlds")
                .header("content-type", "application/json")
                .asJson();

        System.out.println("Test GET worlds should return a list of all the worlds: (Response should be 200)\n\t\t"+resp.getStatus());
        System.out.println("The Response data should be a list of worlds\n");
        printJSONString(resp.getBody().toString());
    }


    private static void printJSONString(String JObject){
        String fString = "";
        String tabs = "";
        for (int i=0; i<JObject.length();i++) {
            if (JObject.charAt(i) == '{') {
                tabs = tabs+"\t";
                fString = fString+tabs+JObject.charAt(i)+"\n";
            } else if (JObject.charAt(i) == '}' && JObject.charAt(i+1) == ',') {
                tabs = new StringBuffer(tabs).deleteCharAt(tabs.length()-1).toString();
                fString = fString+tabs+" },\n"+tabs;
                i++;
            } else if (JObject.charAt(i) == ',' && JObject.charAt(i+1) == '"'){
                fString = fString+",\n"+tabs;
                i++;
            } else {
                fString = fString+JObject.charAt(i);
            }
        }
        System.out.println("Testing");
        System.out.println(fString);
    }


    public static void main(String[] args) {
        startServer();
        System.out.println("------------------------------------------------------------------------------------------------------------------CLEAN DB");
        cleanDB();
        System.out.println("\n\n-----------------------------------------------------------------------------------------------------------------PING TEST");
        testStatusPingReturnsSuccessfulPong();
        System.out.println("\n\n---------------------------------------------------------------------------------------------------------DEFINE WORLD TEST");
        testPostWithDataDefinesNewWorld();
        System.out.println("\n\n------------------------------------------------------------------------------------------------CALCULATE NEXT WORLD STATE");
        testPostToCalculateNextStateOfWorld();
        System.out.println("\n\n--------------------------------------------------------------------------------------------------------GET LIST OF WORLDS");
        testGetWorldsReturnsListOfWorlds();
        stopServer();
    }

}
