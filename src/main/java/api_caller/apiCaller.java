package api_caller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.swing.SwingUtilities;

import View.GameFrameView;
import entities.Lobby;
import entities.Question;
import entities.User;
import interface_adapters.Scoreboard.ScoreboardController;
import use_cases.AddQuestion.SendQuestionsDataAccess;
import use_cases.StartScreen.StartScreenNetworkDataAccessInterface;

public class apiCaller implements SendQuestionsDataAccess, StartScreenNetworkDataAccessInterface {

    private final URL urlBase = new URL("https://shrill-forest-40bb.sw-william08.workers.dev");
    private final String wsBase = "wss://shrill-forest-40bb.sw-william08.workers.dev";
    private final HttpClient http = HttpClient.newHttpClient();

    // buffers for questions/answers
    private Object[] receivedQuestions = null;
    private Object[] receivedAnswers = null;

    private WebSocket socket;

    // Track current lobby + user on this client
    private String currentLobbyPin;
    private String currentUsername;
    private Lobby lobby;
    private User currentUser;

    // Supabase-backed scoreboard controller (injected from AppBuilder)
    private ScoreboardController scoreboardController;

    // ---- GameFrame guard: ensure we only create ONE per join ----
    private GameFrameView gameFrameView;
    private boolean gameStarted = false;

    public apiCaller() throws MalformedURLException {
    }

    public static void main(String[] args) throws MalformedURLException {
        System.out.println("API Caller Started");
    }

    // Called from AppBuilder.addScoreboardUseCase()
    public void setScoreboardController(ScoreboardController scoreboardController) {
        this.scoreboardController = scoreboardController;
    }

    // Allow resetting game state when connection closes (optional)
    private synchronized void resetGame() {
        System.out.println("[api_caller] Resetting game state");
        this.gameStarted = false;
        this.gameFrameView = null;
        this.lobby = null;
        this.currentUser = null;
        this.receivedQuestions = null;
        this.receivedAnswers = null;
    }

    /**
     * Create the GameFrame only if we haven't already started one.
     * This prevents multiple windows from appearing when multiple
     * "questions" messages are received.
     */
    private synchronized void startGameIfNeeded(Lobby lobby, User user, List<Question> questions) {
        if (gameStarted && gameFrameView != null && gameFrameView.isDisplayable()) {
            System.out.println("[api_caller] Game already running for lobby "
                    + lobby.getPin() + ", not creating another GameFrame");
            return;
        }

        System.out.println("[api_caller] Creating GameFrame for lobby "
                + lobby.getPin() + " and user " + user.getName());

        ScoreboardController scForGame = this.scoreboardController;

        gameFrameView = new GameFrameView(
                lobby,
                user,
                questions.size(),
                scForGame     // this is the Supabase-backed controller (may be null if not wired)
        );
        gameStarted = true;

        SwingUtilities.invokeLater(() -> gameFrameView.setVisible(true));
    }

    // Pre: pin is a String
    // Post: create a new room on the server and set this client as host
    public void createRoom(String pin) throws IOException, URISyntaxException, InterruptedException {
        System.out.println("Creating Room");
        this.currentLobbyPin = pin;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlBase + "/api/newRoom/" + pin))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
        socket = openSocket(pin);
        socket.sendText("{\"type\":\"host/setHost\"}", true).join();

        System.out.println(response.body());
    }

    // Connect client to room and upload username
    public void joinRoom(String pin, String username) {
        System.out.println("Joining Room");
        this.currentLobbyPin = pin;
        this.currentUsername = username;

        // clear previous buffers in case this client joins multiple rooms
        this.receivedQuestions = null;
        this.receivedAnswers = null;

        socket = openSocket(pin);
        socket.sendText("{\"type\":\"client/join\",\"username\":\"" + username + "\"}", true).join();

        // immediately ask server to send questions to this client
        socket.sendText("{\"type\":\"client/receiveQuestions\"}", true).join();
    }

    // End game for host
    public void endGame(String pin) {
        System.out.println("Ending Game");
        WebSocket ws = socket;
        ws.sendText("{\"type\":\"host/endGame\"}", true).join();
    }

    // Send questions to server
    @Override
    public void sendQuestions(String pin, Question[] questionList) {
        System.out.println("Sending Questions");
        WebSocket ws = socket;
        StringBuilder questionsListString = new StringBuilder();
        questionsListString.append("[");

        for (int i = 0; i < questionList.length; i++) {
            Question question = questionList[i];
            if (i > 0) questionsListString.append(",");

            questionsListString.append("{\"prompt\":\"");
            questionsListString.append(question.getPrompt());
            questionsListString.append("\",\"choices\":[");
            List<String> choices = question.getChoices();
            for (int j = 0; j < choices.size(); j++) {
                if (j > 0) questionsListString.append(",");
                questionsListString.append("\"").append(choices.get(j)).append("\"");
            }
            questionsListString.append("],");

            questionsListString.append("\"correctIndex\":").append(question.getCorrectIndex()).append("}");
        }

        questionsListString.append("]");

        ws.sendText(
                "{\"type\":\"host/sendQuestions\",\"questions\":" + questionsListString.toString() + "}",
                true
        ).join();
    }

    /**
     * Old polling-style API – kept for backwards compatibility.
     * New flow: the WebSocket listener receives questions and calls handleQuestionsFromReceived().
     */
    public Object[] recieveQuestions(String pin) {
        System.out.println("Receiving Questions (legacy polling)");
        WebSocket ws = socket;

        receivedQuestions = null;

        ws.sendText("{\"type\":\"client/receiveQuestions\"}", true).join();

        while (receivedQuestions == null) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) { /* ignore */ }
        }

        System.out.println("Received questions, returning to caller");
        return receivedQuestions;
    }

    // Send answers as client
    public void sendAnswers(String pin, String[] choicesList, String username) {
        System.out.println("Sending Answers");
        WebSocket ws = socket;

        StringBuilder answersListString = new StringBuilder();
        answersListString.append("[");
        for (int i = 0; i < choicesList.length; i++) {
            answersListString.append('"').append(choicesList[i]).append('"');
            if (i + 1 < choicesList.length) {
                answersListString.append(",");
            }
        }
        answersListString.append("]");

        ws.sendText(
                "{\"type\":\"client/sendAnswers\",\"username\":\"" + username + "\",\"answers\":" +
                        answersListString.toString() + "}",
                true
        ).join();
    }

    // Receive answers as host
    public Object[] recieveAnswers(String pin) {
        System.out.println("Receiving Answers");
        WebSocket ws = socket;

        receivedAnswers = null;

        ws.sendText("{\"type\":\"host/receiveAnswers\"}", true).join();

        while (receivedAnswers == null) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) { /* ignore */ }
        }
        return receivedAnswers;
    }

    // helper: open websocket for a room
    private WebSocket openSocket(String pin) {
        String wsUrl = wsBase + "/ws/rooms/" + pin;
        CompletableFuture<Void> opened = new CompletableFuture<>();
        WebSocket ws = http.newWebSocketBuilder().buildAsync(
                URI.create(wsUrl),
                new LoggingListener(pin, opened)
        ).join();
        opened.join();
        return ws;
    }

    /**
     * Build Lobby + User + Questions from receivedQuestions[]
     * and open a GameFrame for this player (guarded so it only happens once).
     */
    private void handleQuestionsFromReceived() {
        if (receivedQuestions == null || receivedQuestions.length == 0) {
            System.out.println("[api_caller] No receivedQuestions to start game");
            return;
        }

        List<Question> questions = new ArrayList<>();

        for (Object obj : receivedQuestions) {
            if (!(obj instanceof String)) {
                continue;
            }
            String qJson = (String) obj;

            String prompt = extractPrompt(qJson);
            List<String> choices = extractChoices(qJson);
            int correctIndex = extractCorrectIndex(qJson);

            Question q = new Question(prompt, choices, correctIndex);
            questions.add(q);
        }

        int pinInt = 0;
        try {
            pinInt = Integer.parseInt(currentLobbyPin);
        } catch (Exception e) {
            // leave as 0 if parsing fails
        }

        this.lobby = new Lobby(pinInt);
        this.lobby.setQuestions(questions);
        this.currentUser = new User(currentUsername, pinInt);
        this.lobby.addUser(currentUser);

        System.out.println("[api_caller] Questions parsed, starting GameFrame for lobby "
                + pinInt + " and user " + currentUsername);

        // Guarded creation: only first call will actually open a window
        startGameIfNeeded(lobby, currentUser, questions);
    }

    // --- small helpers to parse JSON-ish strings without org.json ---

    private static String extractPrompt(String qJson) {
        String key = "\"prompt\":\"";
        int idx = qJson.indexOf(key);
        if (idx < 0) return "";
        int start = idx + key.length();
        int end = qJson.indexOf("\"", start);
        if (end < 0) end = qJson.length();
        return qJson.substring(start, end);
    }

    private static List<String> extractChoices(String qJson) {
        List<String> choices = new ArrayList<>();
        String key = "\"choices\":[";
        int idx = qJson.indexOf(key);
        if (idx < 0) return choices;
        int start = idx + key.length();
        int end = qJson.indexOf("]", start);
        if (end < 0) end = qJson.length();
        String inside = qJson.substring(start, end); // e.g. "3","4","5","6"
        if (inside.isBlank()) return choices;

        String[] parts = inside.split(",");
        for (String p : parts) {
            p = p.trim();
            if (p.startsWith("\"")) p = p.substring(1);
            if (p.endsWith("\"")) p = p.substring(0, p.length() - 1);
            choices.add(p);
        }
        return choices;
    }

    private static int extractCorrectIndex(String qJson) {
        String key = "\"correctIndex\":";
        int idx = qJson.indexOf(key);
        if (idx < 0) return 0;
        int start = idx + key.length();
        int end = start;
        while (end < qJson.length() && Character.isDigit(qJson.charAt(end))) {
            end++;
        }
        try {
            return Integer.parseInt(qJson.substring(start, end));
        } catch (Exception e) {
            return 0;
        }
    }

    // Listener is now an inner class so it can call handleQuestionsFromReceived()
    class LoggingListener implements WebSocket.Listener {
        private final String pin;
        private final CompletableFuture<Void> opened;

        public LoggingListener(String pin, CompletableFuture<Void> opened) {
            this.pin = pin;
            this.opened = opened;
        }

        @Override
        public void onOpen(WebSocket webSocket) {
            if (opened != null && !opened.isDone()) {
                opened.complete(null);
            }
            WebSocket.Listener.super.onOpen(webSocket);
            System.out.println("opened");

            webSocket.request(1);
        }

        @Override
        public CompletableFuture<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            String stringData = data.toString();
            System.out.println("text received: " + stringData);

            if (stringData.contains("\"type\":\"questions\"")) {
                // populate the buffer
                receivedQuestions = extractJSON(stringData, "\"questions\"");
                // and now actually start the game (guarded)
                handleQuestionsFromReceived();
            }

            if (stringData.contains("\"type\":\"client/answers\"")) {
                receivedAnswers = extractJSON(stringData, "\"answers\"");
            }

            webSocket.request(1);
            return CompletableFuture.completedFuture(null);
        }

        @Override
        public void onError(WebSocket webSocket, Throwable t) {
            System.out.println("error");
            t.printStackTrace();
            WebSocket.Listener.super.onError(webSocket, t);
        }

        @Override
        public CompletableFuture<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            System.out.println("closed");
            // Allow future joins to start a new game
            resetGame();
            return WebSocket.Listener.super.onClose(webSocket, statusCode, reason).toCompletableFuture();
        }
    }

    private static Object[] extractJSON(String json, String key) {
        int index = json.indexOf(key);
        int depth;
        int start = -1;
        boolean inString = false;
        char previous = '0';

        if (index < 0) return new Object[0];

        int arrStartIndex = json.indexOf('[', index);
        if (arrStartIndex < 0) return new Object[0];

        depth = 1;
        int arrEndIndex = -1;
        for (int i = arrStartIndex + 1; i < json.length(); i++) {
            char ch = json.charAt(i);
            if (ch == '"' && previous != '\\') inString = !inString;
            if (!inString) {
                if (ch == '[') depth++;
                else if (ch == ']') {
                    depth--;
                    if (depth == 0) {
                        arrEndIndex = i;
                        break;
                    }
                }
            }
            previous = ch;
        }

        String body = json.substring(arrStartIndex + 1, arrEndIndex);
        if (body.isEmpty()) return new Object[0];

        depth = 0;
        inString = false;
        previous = '0';

        List<String> out = new ArrayList<>();
        for (int i = 0; i < body.length(); i++) {
            char ch = body.charAt(i);
            if (ch == '"' && previous != '\\') inString = !inString;
            if (!inString) {
                if (ch == '{') {
                    if (depth == 0) {
                        start = i;
                        depth++;
                    }
                } else if (ch == '}') {
                    depth--;
                    if (depth == 0) {
                        out.add(body.substring(start, i + 1));
                    }
                }
            }
            previous = body.charAt(i);
        }
        return out.toArray();
    }

    // ---- StartScreenNetworkDataAccessInterface methods ----

    @Override
    public boolean createRemoteRoom(int pin) {
        try {
            createRoom(String.valueOf(pin));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean joinRemoteRoom(int pin, String username) {
        try {
            joinRoom(String.valueOf(pin), username);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
