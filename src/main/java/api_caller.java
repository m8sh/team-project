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
    // Post:
    public void createRoom(String pin) throws MalformedURLException {
        System.out.println("Creating Room");
    }

    // Pre:
    // Post:
    public void joinRoom(String pin, String username){
        System.out.println("Joining Room");

    }

    // Pre:
    // Post:
    public void endGame(String pin){
        System.out.println("Ending Game");

    }

    // Pre:
    // Post:
    public void sendQuestions(String pin, Object[] questionList){
        System.out.println("Sending Questions");

    }

    // Pre:
    // Post:
    public void recieveQuestions(String pin){
        System.out.println("Recieving Questions");

    }

    // Pre:
    // Post:
    public void sendAnswers(String pin, Object[] answerList){
        System.out.println("Sending Answers");

    }

    // Pre:
    // Post:
    public void recieveAnswers(String pin){
        System.out.println("Recieving Answers");

    }
}
