import ui.AddSongWindow;
import ui.PlayerWindow;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class Player {
    public static PlayerWindow window;
    public static AddSongWindow addWindow;
    public static int musicCount = 0;
    public static Map<String, String[]> songs;

    public Player() {
        songs = new HashMap<>();

        ActionListener confirmSongListener = e -> updateQueue();
        ActionListener addSongListener     = e -> addSong(confirmSongListener);
        ActionListener removeSongListener  = e -> removeSong();
        ActionListener playNowListener     = e -> startPlaying();

        window = new PlayerWindow(
                playNowListener,
                removeSongListener,
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
                songs.values().toArray(new String[0][])
        );
    }

    public static void updateQueue() {
        String[] newSong = addWindow.getSong();
        songs.put(newSong[6], newSong);
        window.updateQueueList(songs.values().toArray(new String[songs.size()][]));
    }

    public static void addSong(ActionListener onOk) {
        addWindow = new AddSongWindow(getSongId(), onOk, window.getAddSongWindowListener());
    }

    public static void removeSong() {
        songs.remove(Integer.toString(window.getSelectedSongID()));
        window.updateQueueList(songs.values().toArray(new String[songs.size()][]));
    }

    public static void startPlaying() {
        window.enableScrubberArea();

        int id = window.getSelectedSongID();
        String[] currSong = songs.get(String.valueOf(id));
        int totalTime = Integer.parseInt(currSong[5]);

        window.updateMiniplayer(
                true,
                true,
                false,
                0,
                totalTime,
                0,
                songs.size()
        );
    }

    public static String getSongId() {
        String id = Integer.toString(musicCount);
        musicCount++;
        return id;
    }
}

