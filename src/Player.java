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
        songs = new ArrayList<>();

        ActionListener confirmSongListener = e -> updateQueue();
        ActionListener addSongListener     = e -> addSong(confirmSongListener);

        window = new PlayerWindow(
                null,
                null,
                addSongListener,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "Mastn Music Player",
                songs.toArray(new String[0][])
        );
    }

    public static void updateQueue() {
        songs.add(addWindow.getSong());
        window.updateQueueList(songs.toArray(new String[songs.size()][]));
    }

    public static void addSong(ActionListener onOk) {
        addWindow = new AddSongWindow(getSongId(), onOk, window.getAddSongWindowListener());
    }

    public static String getSongId() {
        String id = Integer.toString(musicCount);
        musicCount++;
        return id;
    }
}

