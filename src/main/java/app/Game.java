package app;

import api_caller.apiCaller;
import data_access.InMemoryDataAccessObject;

import javax.swing.*;

/**
 * Player-side game entry point.
 * It uses the shared api_caller instance provided by AppBuilder.
 * The GameFrame is created inside api_caller when the server
 * sends the "questions" message over WebSocket.
 */
public class Game {

    // Shared api_caller instance, set once by AppBuilder
    private static apiCaller apiCaller;

    public static void setApiCaller(apiCaller caller) {
        apiCaller = caller;
    }

    /**
     * @param username  the player's username
     * @param lobbyPin  the lobby pin as a String
     * @param lobbyDao  kept for compatibility; not used here anymore
     */
    public static void start(String username, String lobbyPin, InMemoryDataAccessObject lobbyDao) {

        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("1");

                if (apiCaller == null) {
                    throw new IllegalStateException("Game.apiCaller has not been set. " +
                            "Make sure AppBuilder calls Game.setApiCaller(...) in its constructor.");
                }

                System.out.println("2");
                int pinInt = Integer.parseInt(lobbyPin);

                // Use the network interface method.
                // This opens the WebSocket and asks the server to send questions.
                apiCaller.joinRemoteRoom(pinInt, username);

                System.out.println("3 (joined via WebSocket, waiting for questions)");

                // DO NOT:
                // - call apiCaller.recieveQuestions(...)
                // - parse questions manually
                // - new GameFrame(...)
                //
                // The WebSocket listener in api_caller will receive the "questions"
                // message, build Lobby + User, and call startGameIfNeeded(...)
                // which creates the SINGLE GameFrame for this client.

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
