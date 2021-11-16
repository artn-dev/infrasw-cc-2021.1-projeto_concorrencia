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
    public static boolean isActive = false;
    public static int[] currSongData;

    public Player() {
        songs = new HashMap<>();
        currSongData = new int[]{0, 0};

        ActionListener confirmSongListener = e -> updateQueue();
        ActionListener addSongListener     = e -> addSong(confirmSongListener);
        ActionListener removeSongListener  = e -> removeSong();
        ActionListener playNowListener     = e -> startPlaying();
        ActionListener playPauseListener   = e -> togglePlay();
        ActionListener stopListener        = e -> stopPlaying();

        window = new PlayerWindow(
                playNowListener,
                removeSongListener,
                addSongListener,
                playPauseListener,
                stopListener,
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
                    updateMiniplayer();
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
        isActive  = true;

        int id = window.getSelectedSongID();
        String[] currSong = songs.get(String.valueOf(id));
        currSongData[1] = Integer.parseInt(currSong[5]);

        updateMiniplayer();

        new Thread() {
            public void run() {
                while (isActive && currSongData[0] <= currSongData[1]) {
                    try {
                        mutex.acquire();

                        if (!isPlaying) {
                            mutex.release();
                            continue;
                        }

                        updateMiniplayer();
                        currSongData[0] += 1;

                        mutex.release();
                        Thread.sleep(1000);

                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }

                stopPlaying();
            }
        }.start();
    }

    public static void stopPlaying() {
        isPlaying = false;
        isActive  = false;
        currSongData[0] = 0;
        currSongData[1] = 0;
        updateMiniplayer();
    }

    public static void updateMiniplayer() {
        window.updateMiniplayer(
                isActive,
                isPlaying,
                false,
                currSongData[0],
                currSongData[1],
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

