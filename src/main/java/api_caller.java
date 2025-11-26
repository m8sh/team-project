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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


public class api_caller {

    private final URL urlBase = new URL("https://shrill-forest-40bb.sw-william08.workers.dev");
    private final String wsBase = "wss://shrill-forest-40bb.sw-william08.workers.dev";
    private final HttpClient http = HttpClient.newHttpClient();
    private final Map<String, WebSocket> sockets =  new HashMap<>();
    private static Object[] receivedQuestions = null;
    private static Object[] receivedAnswers = null;

    public api_caller() throws MalformedURLException {
    }

    public static void main(String[] args) throws MalformedURLException {
        System.out.println("API Caller Started");
    }
    // Pre: pin is a String
    // Post: Sends a message to "http://shrill-forest-40bb.sw-william08.workers.dev/api/newRoom/{pin}"
    // The durable object is initialised and the PIN -> room mapping is made server-side
    // All that to say - makes a server and connects the host computer to it
    public void createRoom(String pin) throws IOException, URISyntaxException, InterruptedException {
        System.out.println("Creating Room");
        HttpRequest request = HttpRequest.newBuilder().uri(new URI(urlBase + "/api/newRoom/" + pin)).POST(HttpRequest.BodyPublishers.noBody()).build();

        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
        WebSocket ws = openSocket(pin);
        ws.sendText("{\"type\":\"host/setHost\"}", true).join();
        sockets.put(pin, ws);

        System.out.println(response.body());
    }

    // Pre:
    // Post: Connects the client computer to the server that is associated with the given PIN
    // also uploads the username to the server, so we know who is who
    public void joinRoom(String pin, String username){
        System.out.println("Joining Room");
        WebSocket ws = openSocket(pin);
        ws.sendText("{\"type\":\"client/join\",\"username\":\"" + username + "\"}", true).join();
        sockets.put(pin, ws);
    }

    // Pre:
    // Post: Closes the server, making it so nobody can send or receive any questions or answers,
    // or any other data
    public void endGame(String pin){
        System.out.println("Ending Game");
        WebSocket ws = sockets.get(pin);
        ws.sendText("{\"type\":\"host/endGame\"}", true).join();
        sockets.remove(pin);
    }

    // Pre: make the questionList contain an array of arrays, that contains a bunch of questions
    // For example, ["how many sides in a square?", "what is the color of the sun?", etc.]
    // Post: Sends those questions to the server
    public void sendQuestions(String pin, Question[] questionList){
        System.out.println("Sending Questions");
        WebSocket ws = sockets.get(pin);
        StringBuilder questionsListString = new StringBuilder();
        questionsListString.append("[");

        for (int i = 0; i < questionList.length; i++){
            Question question = questionList[i];
            if (i > 0) questionsListString.append(",");

            questionsListString.append("{\"prompt\":\"");
            questionsListString.append(question.getPrompt());
            questionsListString.append("\",\"choices\":[");
            ArrayList<String> choices = question.getChoices();
            for (int j = 0; j < choices.size(); j++){
                if (j > 0) questionsListString.append(",");
                questionsListString.append("\"" + choices.get(j) + "\"");
            }
            questionsListString.append("]");

            questionsListString.append("\"correctIndex\":" + question.getCorrectIndex() + "}");

            questionsListString.append('"').append(questionList[i].toString()).append('"');
        }

        questionsListString.append("]");

        ws.sendText("{\"type\":\"host/sendQuestions\",\"questions\":" + questionsListString.toString() + "}", true).join();
    }

    // Pre: just put in the pin so it knows what server to talk to
    // Post: it will return the set of questions in the same format that they were uploaded
    public Object[] recieveQuestions(String pin){
        System.out.println("Recieving Questions");
        WebSocket ws = sockets.get(pin);

        receivedQuestions = null;

        ws.sendText("{\"type\":\"client/receiveQuestions\"}", true).join();

        while (receivedQuestions == null){
            try{
                Thread.sleep(20);
            } catch (InterruptedException e){}
        }
        return receivedQuestions;
    }

    // Pre:
    // Post: sends the answers in a simple array of [<answer 1>, <answer 2>, etc.] to the server
    public void sendAnswers(String pin, String[] choicesList, String username){
        System.out.println("Sending Answers");
        WebSocket ws = sockets.get(pin);

        StringBuilder answersListString = new StringBuilder();
        answersListString.append("[");
        for (int i = 0; i < choicesList.length; i++){
            answersListString.append('"').append(choicesList[i].toString()).append('"');
            if (i + 1 < choicesList.length){
                answersListString.append(",");
            }
        }
        answersListString.append("]");

        ws.sendText("{\"type\":\"client/sendAnswers\",\"username\":\"" + username + "\",\"answers\":" + answersListString.toString() + "}",true).join();
    }

    // Pre:
    // Post: returns the answers to the host, in the same fomat that they were originally given,
    // also returns username
    public Object[] recieveAnswers(String pin){
        System.out.println("Recieving Answers");
        WebSocket ws = sockets.get(pin);

        receivedAnswers = null;

        ws.sendText("{\"type\":\"host/receiveAnswers\"}", true).join();

        while (receivedAnswers == null){
            try{
                Thread.sleep(20);
            } catch (InterruptedException e){}
        }
        return receivedAnswers;
    }

    // herlper functions
    private WebSocket openSocket(String pin) {
        String wsUrl = wsBase + "/ws/rooms/" + pin;
        CompletableFuture<WebSocket> future = http.newWebSocketBuilder().buildAsync(URI.create(wsUrl), new LoggingListener(pin));
        return future.join();
    }

    static class LoggingListener implements WebSocket.Listener {
        private final String pin;
        public LoggingListener(String pin) {
            this.pin = pin;
        }
        @Override
        public void onOpen(WebSocket webSocket) {
            WebSocket.Listener.super.onOpen(webSocket);
            System.out.println("opened");
        }
        @Override
        public CompletableFuture<?> onText(WebSocket webSocket, CharSequence data, boolean last){
            System.out.println("text received");
            String string = data.toString();

            if (string.contains("\"type\":\"questions\"")){
                receivedQuestions = extractJSON(string, "\"questions\"");
            }

            if (string.contains("\"type\":\"client/answers\"")){
                receivedAnswers = extractJSON(string, "\"answers\"");
            }

            return WebSocket.Listener.super.onText(webSocket,data,last).toCompletableFuture();
        }
        @Override
        public void onError(WebSocket webSocket, Throwable t){
            WebSocket.Listener.super.onError(webSocket,t);
            System.out.println("error");
        }
        @Override
        public CompletableFuture<?> onClose(WebSocket webSocket, int statusCode, String reason){
            System.out.println("closed");
            return WebSocket.Listener.super.onClose(webSocket,statusCode,reason).toCompletableFuture();
        }
    }

    private static Object[] extractJSON(String json, String key){
        int index = json.indexOf(key);
        int depth = 0;
        int start = -1;
        boolean inString = false;
        char previous = '0';

        if (index < 0) return new Object[0];

        int arrStartIndex = json.indexOf('[', index);
        if (arrStartIndex < 0) return new Object[0];

        depth = 1;
        int arrEndIndex = -1;
        for (int i = arrStartIndex + 1; i < json.length(); i++){
            char ch = json.charAt(i);
            if (ch == '"' && previous != '\\') inString = !inString;
            if (!inString){
                if (ch == '[') depth++;
                else if (ch == '{') {
                    depth++;
                    if (depth == 0){
                        arrEndIndex = i;
                        break;
                    }
                }

            }
            previous = ch;
        }

        String body = json.substring(arrStartIndex + 1, arrEndIndex);
        if (body.isEmpty()) return new Object[0];

        List<String> out = new ArrayList<>();
        for (int i = 0; i < body.length(); i++){
            char ch = body.charAt(i);
            if (ch == '"' && previous != '\\') inString = !inString;
            if (!inString){
                if (ch == '{'){
                    if (depth == 0){
                        start = i;
                        depth++;
                    }
                    else if  (ch == '}') {
                        depth--;
                        if (depth == 0) {
                            out.add(body.substring(start, i+1));
                        }
                    }
                }
            }
            previous = body.charAt(i);
        }
        return out.toArray();
    }

}