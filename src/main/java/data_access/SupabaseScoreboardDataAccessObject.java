package data_access;
////////////
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
import java.util.Comparator;
import java.util.List;

/**
 * Persists scoreboard results to Supabase via the REST API.
 * This ONLY implements ScoreboardDataAccessInterface, so other
 * use cases (AddQuestion, StartScreen, etc.) keep using your existing
 * InMemoryDataAccessObject.
 */
public class SupabaseScoreboardDataAccessObject implements ScoreboardDataAccessInterface {

    private final String restUrl;  // e.g. https://xxxx.supabase.co/rest/v1
    private final String apiKey;
    private final HttpClient client = HttpClient.newHttpClient();

    public SupabaseScoreboardDataAccessObject(String supabaseUrl, String apiKey) {
        // Ensure we end with /rest/v1
        String base = supabaseUrl;
        if (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }
        if (!base.endsWith("/rest/v1")) {
            base = base + "/rest/v1";
        }
        this.restUrl = base;
        this.apiKey = apiKey;
    }

    @Override
    public void saveResults(int lobbyPin, List<User> users) {
        if (users == null || users.isEmpty()) {
            System.out.println("[SupabaseScoreboardDAO] saveResults: empty users for lobby " + lobbyPin);
            return;
        }

        // Sort by score DESC and compute ranks (tie-aware)
        List<User> sorted = new ArrayList<>(users);
        sorted.sort(Comparator.comparingInt(User::getScore).reversed());

        JSONArray rows = new JSONArray();
        int rankCounter = 1;
        int lastScore = Integer.MIN_VALUE;
        int lastRank = 1;

        for (User u : sorted) {
            int score = u.getScore();
            if (score != lastScore) {
                lastScore = score;
                lastRank = rankCounter;
            }

            JSONObject row = new JSONObject();
            row.put("lobby_pin", lobbyPin);
            row.put("username", u.getName());
            row.put("score", score);
            row.put("rank", lastRank);
            rows.put(row);

            rankCounter++;
        }

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(restUrl + "/scoreboard_entries"))
                    .header("apikey", apiKey)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .header("Prefer", "return=minimal")
                    .POST(HttpRequest.BodyPublishers.ofString(rows.toString(), StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("[SupabaseScoreboardDAO] saveResults status " + response.statusCode());
            if (response.statusCode() >= 400) {
                System.out.println("[SupabaseScoreboardDAO] saveResults body: " + response.body());
            }
        } catch (Exception e) {
            System.out.println("[SupabaseScoreboardDAO] saveResults exception");
            e.printStackTrace();
        }
    }

    @Override
    public List<User> loadResults(int lobbyPin) {
        List<User> result = new ArrayList<>();
        try {
            String url = restUrl
                    + "/scoreboard_entries"
                    + "?lobby_pin=eq." + lobbyPin
                    + "&order=score.desc";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("apikey", apiKey)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("[SupabaseScoreboardDAO] loadResults status " + response.statusCode());

            if (response.statusCode() >= 400) {
                System.out.println("[SupabaseScoreboardDAO] loadResults body: " + response.body());
                return result;
            }

            JSONArray array = new JSONArray(response.body());
            for (int i = 0; i < array.length(); i++) {
                JSONObject row = array.getJSONObject(i);
                String username = row.getString("username");
                int score = row.getInt("score");

                User u = new User(username, lobbyPin, score);
                result.add(u);
            }
        } catch (Exception e) {
            System.out.println("[SupabaseScoreboardDAO] loadResults exception");
            e.printStackTrace();
        }
        return result;
    }
}
