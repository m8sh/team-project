package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            AppBuilder builder = new AppBuilder();

            builder.addScoreboardView()
                    .addScoreboardUseCase()
                    .addStartScreenView()
                    .addStartScreenUseCase()
                    .addLobbyPrepView(123);    // creates LobbyPrepViewModel + controller
                       // presenter now gets non-null LobbyPrepViewModel

            JFrame app = builder.build();
            app.pack();
            app.setVisible(true);
        });
    }
}
