package use_cases.StartScreen;

public interface StartScreenNetworkDataAccessInterface {

    boolean createRemoteRoom(int pin);
    boolean joinRemoteRoom(int pin, String username);

}
