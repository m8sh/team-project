package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            AppBuilder builder = new AppBuilder();

            builder.addStartScreenView()
                    .addScoreboardView()
                    .addScoreboardUseCase()
                    .addLobbyPrepView(123)      // creates LobbyPrepViewModel + controller
                    .addStartScreenUseCase();   // presenter now gets non-null LobbyPrepViewModel

            JFrame app = builder.build();
            app.pack();
            app.setVisible(true);
        });
    }
}
