package api_caller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URL;
import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import entities.Question;
import use_cases.AddQuestion.SendQuestionsDataAccess;
import use_cases.StartScreen.StartScreenNetworkDataAccessInterface;

public class api_caller implements SendQuestionsDataAccess, StartScreenNetworkDataAccessInterface {

    private final URL urlBase = new URL("https://shrill-forest-40bb.sw-william08.workers.dev");
    private final String wsBase = "wss://shrill-forest-40bb.sw-william08.workers.dev";
    private final HttpClient http = HttpClient.newHttpClient();
    private static Object[] receivedQuestions = null;
    private static Object[] receivedAnswers = null;
    private WebSocket socket;

    public api_caller() throws MalformedURLException {
    }

    public static void main(String[] args) throws MalformedURLException {
        System.out.println("API Caller Started");
    }

    // Pre: pin is a String
    // Post: create a new room on the server and set this client as host
    public void createRoom(String pin) throws IOException, URISyntaxException, InterruptedException {
        System.out.println("Creating Room");
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
        socket = openSocket(pin);
        socket.sendText("{\"type\":\"client/join\",\"username\":\"" + username + "\"}", true).join();
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

    // Receive questions as client
    public Object[] recieveQuestions(String pin) {
        System.out.println("Recieving Questions");
        WebSocket ws = socket;

        receivedQuestions = null;

        ws.sendText("{\"type\":\"client/receiveQuestions\"}", true).join();

        while (receivedQuestions == null) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) { /* ignore */ }
        }

        System.out.println("Recieved questions, returning to game");
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
        System.out.println("Recieving Answers");
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

    static class LoggingListener implements WebSocket.Listener {
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
            String string = data.toString();

            if (string.contains("\"type\":\"questions\"")) {
                receivedQuestions = extractJSON(string, "\"questions\"");
            }

            if (string.contains("\"type\":\"client/answers\"")) {
                receivedAnswers = extractJSON(string, "\"answers\"");
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
