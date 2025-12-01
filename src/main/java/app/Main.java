package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            AppBuilder builder = new AppBuilder();

            builder.addStartScreenView()
                    .addLobbyPrepView(123)
                    .addScoreboardView()
                    .addScoreboardUseCase()
                        // creates LobbyPrepViewModel + controller
                    .addStartScreenUseCase();   // presenter now gets non-null LobbyPrepViewModel

            JFrame app = builder.build();
            app.pack();
            app.setVisible(true);
        });
    }
}
