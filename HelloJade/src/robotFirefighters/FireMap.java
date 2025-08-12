package robotFirefighters;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * static class used to import the map of fires from a file and other things
 */
public class FireMap {
    private static final Map<Integer, Boolean> fireMap = new ConcurrentHashMap<>();
    private static boolean initialized = false;
    
    /**
     * initializes the map by reading a file built as
     * Room,boolean
     * @param pathFile the path of the file
     */
    public static synchronized void initialize(String pathFile) {
        if (initialized) return;

        try (BufferedReader br = new BufferedReader(new FileReader(pathFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int room = Integer.parseInt(parts[0]);
                boolean fire = Boolean.parseBoolean(parts[1]);
                fireMap.put(room, fire);
            }
            initialized = true;
            System.out.println("[FireMap] Mappa caricata da file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isOnFire(int room) {
        return fireMap.getOrDefault(room, false);
    }

    public static void setFire(int room, boolean fire) {
        fireMap.put(room, fire);
    }

    public static Map<Integer, Boolean> getMap() {
        return new ConcurrentHashMap<>(fireMap);
    }
    
    /**
     * method to find the biggest number associated to a room
     * @return the biggest number
     */
    public static Integer getMaxRoom() {
        Map<Integer, Boolean> map = getMap();
        Integer max = 0;

        for (Integer room : map.keySet()) {
            if (room > max) {
                max = room;
            }
        }

        return max;
    }
}
