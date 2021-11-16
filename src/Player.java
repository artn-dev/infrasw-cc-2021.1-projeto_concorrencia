import ui.AddSongWindow;
import ui.PlayerWindow;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class Player {
    public static PlayerWindow window;
    public static AddSongWindow addWindow;
    public static int musicCount = 0;
    public static Map<String, String[]> songs;
    public static Semaphore mutex = new Semaphore(1);
    public static boolean isPlaying = false;

    public Player() {
        songs = new HashMap<>();

        ActionListener confirmSongListener = e -> updateQueue();
        ActionListener addSongListener     = e -> addSong(confirmSongListener);
        ActionListener removeSongListener  = e -> removeSong();
        ActionListener playNowListener     = e -> startPlaying();
        ActionListener playPauseListener   = e -> togglePlay();

        window = new PlayerWindow(
                playNowListener,
                removeSongListener,
                addSongListener,
                playPauseListener,
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

    public static void togglePlay() {
        new Thread() {
            public void run() {
                try {
                    mutex.acquire();
                    isPlaying = !isPlaying;
                    mutex.release();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static void startPlaying() {
        window.enableScrubberArea();
        isPlaying = true;

        int id = window.getSelectedSongID();
        String[] currSong = songs.get(String.valueOf(id));
        int totalTime = Integer.parseInt(currSong[5]);

        window.updateMiniplayer(
                true,
                isPlaying,
                false,
                0,
                totalTime,
                0,
                songs.size()
        );

        new Thread() {
            public void run() {
                int deltaTime = 0;

                while (deltaTime <= totalTime) {
                    try {
                        mutex.acquire();

                        if (!isPlaying) {
                            mutex.release();
                            continue;
                        }

                       window.updateMiniplayer(
                                true,
                                isPlaying,
                                false,
                                deltaTime,
                                totalTime,
                                0,
                                songs.size()
                        );

                        deltaTime += 1;

                        mutex.release();
                        Thread.sleep(1000);

                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public static String getSongId() {
        String id = Integer.toString(musicCount);
        musicCount++;
        return id;
    }
}

