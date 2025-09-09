package robotFirefighters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fire Map that implements Serializable. It is what the robots send in
 * messages. There is a HashMap between integer and a boolean. The integer is
 * the key and represents the room. The boolean represents if there is fire in
 * the room (true if there is fire)
 * 
 * Aggiungi stazione di carica
 */
public class SerializableFireMap implements Serializable {
	Map<Integer, Boolean> fireMap;

	/**
	 * it copies a map to the fireMap in the class
	 * 
	 * @param newMap an updated fire map
	 */
	public void copyMap(Map<Integer, Boolean> newMap) {
		this.fireMap.putAll(newMap);
	}

	/**
	 * sets the fire in a position
	 * 
	 * @param position the number of the room
	 * @param status   true if there is fire, false if there isn't
	 */
	public void setFire(Integer position, Boolean status) {
		fireMap.put(position, status);
	}

	public SerializableFireMap() {
		this.fireMap = new HashMap<>();
	}

	public Map<Integer, Boolean> getFireMap() {
		return fireMap;
	}

	public Integer getSize() {
		return this.fireMap.size();
	}

	public Boolean getFireStatus(int position) {
		if (fireMap.containsKey(position)) {
			return fireMap.get(position);
		} else {
			return false;
		}

	}

	/**
	 * method to print the map
	 * 
	 * @param name the name of the robot
	 */
	public synchronized void printMap() {
		for (int i = 0; i < this.getSize(); i++) {
			System.out.println("Room : " + i + "\nFire : " + getFireStatus(i));
		}
	}
	
	/**
	 * method to get the rooms that are still on fire
	 * @return a list of rooms on fire
	 */
	public List<Integer> getRoomsOnFire() {

		List<Integer> roomsOnFire = new ArrayList<>();
		for (int i = 0; i < this.getSize(); i++) {
			if (this.getFireStatus(i)) {
				roomsOnFire.add(i);
			}
		}

		return roomsOnFire;
	}
}
