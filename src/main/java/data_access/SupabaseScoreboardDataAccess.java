package data_access;

import entities.User;
import org.json.JSONArray;
import org.json.JSONObject;
import use_cases.Scoreboard.ScoreboardDataAccessInterface;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SupabaseScoreboardDataAccess implements ScoreboardDataAccessInterface {


    private static final String SUPABASE_URL = "https://sccjnpwhzilknkoqcktq.supabase.co";


    private static final String SUPABASE_API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InNjY2pucHdoemlsa25rb3Fja3RxIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjQyNzA0ODcsImV4cCI6MjA3OTg0NjQ4N30.pv55IeSxPxfrSV5hRuP2hSqrrUK2nW71kKYoyxhq_u0";

    private static final String TABLE = "scoreboard";

    private final HttpClient client = HttpClient.newHttpClient();

    @Override
    public void saveResults(int lobbyPin, List<User> users) {
        try {
            JSONArray payload = new JSONArray();
            int rank = 1;
            for (User u : users) {
                JSONObject obj = new JSONObject();
                obj.put("lobby_pin", lobbyPin);
                obj.put("username", u.getName());
                obj.put("score", u.getScore());
                obj.put("rank", rank++);
                payload.put(obj);
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SUPABASE_URL + "/rest/v1/" + TABLE))
                    .header("apikey", SUPABASE_API_KEY)
                    .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                    .header("Content-Type", "application/json")
                    .header("Prefer", "return=minimal")
                    .POST(HttpRequest.BodyPublishers.ofString(payload.toString(), StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("[SupabaseDAO] saveResults code: "
                    + response.statusCode() + ", body: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> loadResults(int lobbyPin) {
        List<User> users = new ArrayList<>();
        try {
            // filter by lobby_pin
            String filter = "?lobby_pin=eq." + lobbyPin + "&select=username,score,rank";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SUPABASE_URL + "/rest/v1/" + TABLE + filter))
                    .header("apikey", SUPABASE_API_KEY)
                    .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("[SupabaseDAO] loadResults code: "
                    + response.statusCode() + ", body: " + response.body());

            if (response.statusCode() == 200) {
                JSONArray arr = new JSONArray(response.body());
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    String username = obj.getString("username");
                    int score = obj.getInt("score");

                    User u = new User(username, lobbyPin);
                    u.setScore(score);
                    users.add(u);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
}
