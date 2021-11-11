import ui.AddSongWindow;
import ui.PlayerWindow;

import java.awt.event.ActionListener;

public class Player {
    public static int musicCount = 0;

    public Player() {
        ActionListener addSong = e -> {
            AddSongWindow addWindow = new AddSongWindow(getSongId(), null, null);
        };

        PlayerWindow window = new PlayerWindow(
                null,
                null,
                addSong,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "Mastn Music Player",
                null
        );
    }

    public static String getSongId() {
        String id = Integer.toString(musicCount);
        musicCount++;
        return id;
    };
}

