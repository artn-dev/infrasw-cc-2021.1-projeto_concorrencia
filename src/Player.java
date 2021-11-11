import ui.AddSongWindow;
import ui.PlayerWindow;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Player {
    public static PlayerWindow window;
    public static AddSongWindow addWindow;
    public static int musicCount = 0;
    public static List<String[]> songs;

    public Player() {
        songs = new ArrayList<String[]>();

        ActionListener confirmSong = e -> {
            songs.add(addWindow.getSong());
        };

        ActionListener addSong = e -> {
            addWindow = new AddSongWindow(getSongId(), confirmSong, null);
        };

        window = new PlayerWindow(
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

