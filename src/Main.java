import ui.PlayerWindow;
import ui.AddSongWindow;

import java.awt.event.ActionListener;

public class Main {
    public static int musicCount = 0;

    public static void main(String[] args) {
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
