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

    }

    public void createRoom(String pin) throws MalformedURLException {
        URL createNewRoomURL = new URL((this.urlBase + "/"));
    }

    public void joinRoom(String pin, String username){

    }

    public void endGame(String pin){
    }

    public void sendQuestions(String pin, String[] questionList){

    }

    public void recieveQuestions(String pin){

    }

    public void sendAnswers(String pin, String[] answerList){

    }

    public void recieveAnswers(String pin){

    }
}
