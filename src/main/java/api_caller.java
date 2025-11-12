import java.net.MalformedURLException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URL;

public class api_caller {

    private URL urlBase = new URL("http://shrill-forest-40bb.sw-william08.workers.dev");

    public api_caller() throws MalformedURLException {
    }

    public static void main(String[] args) throws MalformedURLException {
        System.out.println("API Caller Started");
    }
    // Pre: pin is a String
    // Post: Sends a message to "http://shrill-forest-40bb.sw-william08.workers.dev/api/newRoom/{pin}"
    // The durable object is initialised and the PIN -> room mapping is made server-side
    // All that to say - makes a server and connects the host computer to it
    public void createRoom(String pin) throws MalformedURLException {
        System.out.println("Creating Room");
        URL createNewRoomURL = new URL((this.urlBase + "/"));
    }

    // Pre:
    // Post: Connects the client computer to the server that is associated with the given PIN
    // also uploads the username to the server, so we know who is who
    public void joinRoom(String pin, String username){
        System.out.println("Joining Room");

    }

    // Pre:
    // Post: Closes the server, making it so nobody can send or receive any questions or answers,
    // or any other data
    public void endGame(String pin){
        System.out.println("Ending Game");

    }

    // Pre: make the questionList contain an array of arrays, that contains a bunch of questions
    // For example, ["how many sides in a square?", "what is the color of the sun?", etc.]
    // Post: Sends those questions to the server
    public void sendQuestions(String pin, Object[] questionList){
        System.out.println("Sending Questions");

    }

    // Pre: just put in the pin so it knows what server to talk to
    // Post: it will return the set of questions in the same format that they were uploaded
    public void recieveQuestions(String pin){
        System.out.println("Recieving Questions");

    }

    // Pre:
    // Post: sends the answers in a simple array of [<answer 1>, <answer 2>, etc.] to the server
    public void sendAnswers(String pin, Object[] answerList, String username){
        System.out.println("Sending Answers");

    }

    // Pre:
    // Post: returns the answers to the host, in the same fomat that they were originally given
    public void recieveAnswers(String pin){
        System.out.println("Recieving Answers");

    }
}
