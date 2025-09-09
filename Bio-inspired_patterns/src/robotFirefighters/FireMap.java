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
	/**
	 * the map of the fires in the building
	 */
	private static final Map<Integer, Boolean> fireMap = new ConcurrentHashMap<>();
	/**
	 * flag to say if it has already been initialized
	 */
	private static boolean initialized = false;

	/**
	 * initializes the map by reading a file built as Room,boolean
	 * 
	 * @param pathFile the path of the file
	 */
	public static synchronized void initialize(String pathFile) {
		if (initialized)
			return;

		/**
		 * initialize a BufferReader to read from file
		 */
		try (BufferedReader br = new BufferedReader(new FileReader(pathFile))) {
			/**
			 * create a string that will represent the line
			 */
			String line;
			/**
			 * go on until while the current line isn't null
			 */
			while ((line = br.readLine()) != null) {
				/**
				 * split the line in parts divided by ,
				 */
				String[] parts = line.split(",");
				/**
				 * the room is the first part, the status is the second part
				 */
				int room = Integer.parseInt(parts[0]);
				boolean fire = Boolean.parseBoolean(parts[1]);

				/**
				 * put the binding room - fire in the map
				 */
				fireMap.put(room, fire);
			}
			initialized = true;
			System.out.println("[FireMap] Map loaded from file");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * return if the room is on fire
	 * 
	 * @param room 		a room
	 * @return 			boolean saying if the room is on fire
	 */
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
	 * 
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
