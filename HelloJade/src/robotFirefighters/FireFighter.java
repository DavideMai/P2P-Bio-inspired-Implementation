package robotFirefighters;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import utils.GossipAgent;

/**
 * class that models Firefighter Robots that follow the Gossip Pattern to
 * exchange informations between them
 */

public class FireFighter extends GossipAgent {

	/**
	 * currentPosition is the current room of the robot localFireMap is the local
	 * copy of the fire map fireExtinguished is a flag that says if the robot
	 * extinguished a fire in this period
	 */
	Integer currentPosition;
	SerializableFireMap localFireMap = new SerializableFireMap();
	Boolean fireExtinguished;
	Boolean firstCycle = true;

	/**
	 * implementation of the aggregate method. The robot receives a set of Maps
	 * representing rooms and fires. It changes room randomly, merges its map with
	 * the received one, if there is fire it extinguish it, and then returns the new
	 * map
	 */
	@Override
	public Serializable aggregate(List<Serializable> content) {
		/**
		 * changes room
		 */
		this.changeRoom();

		/**
		 * variable used to determine what to output
		 */
		fireExtinguished = false;

		/**
		 * a copy of the local map
		 */
		Map<Integer, Boolean> oldLocalMap = new HashMap<>(localFireMap.getFireMap());
		/**
		 * for every map it receives, it puts the content into the localMap by following
		 * the AND logic. It puts true in the room only if both the local and the remote
		 * map say that there is fire Otherwise, it puts false
		 */
		for (Serializable ser : content) {
			if (ser instanceof SerializableFireMap) {
				SerializableFireMap receivedMap = (SerializableFireMap) ser;

				for (Map.Entry<Integer, Boolean> entry : receivedMap.getFireMap().entrySet()) {
					Integer room = entry.getKey();
					Boolean remoteStatus = entry.getValue();
					Boolean localStatus = oldLocalMap.get(room);

					if (localStatus != null) {
						oldLocalMap.put(room, localStatus && remoteStatus);
					}
				}
			}
		}

		/**
		 * copy the oldLocalMap into the localFireMap
		 */
		localFireMap.copyMap(oldLocalMap);

		/**
		 * searches in its local map if there is fire in its room. If there is, it then
		 * extinguish the fire
		 */
		Boolean isOnFire = localFireMap.getFireStatus(getCurrentPosition());
		if (isOnFire != null && isOnFire) {
			localFireMap.setFire(getCurrentPosition(), false);
			fireExtinguished = true;
		}

		return localFireMap;
	}

	/**
	 * implementation of the printRecap method. It prints what the robot did in the
	 * cycle. When every fire is extinguished, the robot gets deleted
	 */
	@Override
	public void printRecap(Serializable aggregatedContent, List<AID> neighbours, String localName) {
		synchronized (FireFighter.class) {
			System.out.println("\n\u001B[32m========== " + localName + " ==========");

			System.out.println("\u001B[33m Current map: ");
			localFireMap.printMap();

			if (fireExtinguished) {
				System.out.println(
						"\u001B[32mRobot " + localName + " has extinguished the fire in room " + getCurrentPosition());
			} else {
				System.out.println("Robot " + localName + " went into room " + getCurrentPosition()
						+ " but it didn't find any fire");
			}
			if (checkFires()) {
				setCurrentPosition(-1);
				System.out.println("\u001B[35mRobot " + localName
						+ " can't find any other fire in the building and returned to the charging station");
				this.doDelete();
			}
		}
	}

	/**
	 * method that changes the room of the robot randomly. It takes the number of
	 * rooms and generates a random number between 0 and that. Then, checks if the
	 * room is on fire. If it is on fire, it goes there. Else, it generates another
	 * number
	 */
	public void changeRoom() {

		/**
		 * boolean flag to see if the current room is on fire
		 */
		Boolean isCurrentRoomOnFire = false;

		/**
		 * an integer that is used to set the destination room
		 */
		Integer room = 0;
		Random rand = new Random();
		/**
		 * the sorted set of nearby rooms on fire
		 */
		SortedSet<Integer> nearbyRoomsOnFire = new TreeSet<>();

		/**
		 * check if the current room is on fire. It sets the next room to the current
		 * room and puts the flag as true
		 */
		if (this.localFireMap.getRoomsOnFire().contains(currentPosition)) {
			isCurrentRoomOnFire = true;
			room = currentPosition;
		} else {
			/**
			 * check rooms between current - 2 and current + 2. If they are on fire, they
			 * are added to the SortedSet
			 */
			for (int i = currentPosition - 2; i < currentPosition + 2; i++) {
				/**
				 * i >= 0 because there are only positive rooms
				 */
				if (i >= 0 && i != currentPosition) {
					if (this.localFireMap.getRoomsOnFire().contains(currentPosition)) {
						nearbyRoomsOnFire.add(i);
					}
				}
			}
		}

		/**
		 * if the sorted set contains something, then go in a random room between ones
		 * contained in the set. Else, generate a random room which is on fire
		 */
		if (!nearbyRoomsOnFire.isEmpty()) {
			room = rand.nextInt(nearbyRoomsOnFire.getFirst(), nearbyRoomsOnFire.getLast() + 1);
		} else if (!isCurrentRoomOnFire) {
			do {
				Integer numberOfRooms = FireMap.getMaxRoom();

				room = rand.nextInt(0, numberOfRooms + 1);
			} while (!this.localFireMap.getRoomsOnFire().contains(room));
		}

		/**
		 * go to the position room
		 */
		this.setCurrentPosition(room);
		// considera la possibilità di andare a spegnere tutti un incendio
	}

	/**
	 * method that returns the start position by reading from a file
	 * 
	 * @param pathFile 	the path of the configuration file
	 * @return 			the starting position of the robot
	 */
	private int readStartPosition(String pathFile) {
		int pos = 0; // default value if not found
		try (BufferedReader br = new BufferedReader(new FileReader(pathFile))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length == 2) {
					String robotName = parts[0].trim();
					int position = Integer.parseInt(parts[1].trim());
					if (robotName.equalsIgnoreCase(getLocalName())) {
						pos = position;
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (pos == -1) {
			System.err.println("Posizione non trovata per " + getLocalName());
		}
		return pos;
	}

	public Integer getCurrentPosition() {
		return this.currentPosition;
	}

	public void setCurrentPosition(Integer newPosition) {
		this.currentPosition = newPosition;
	}

	/**
	 * implementation of the setup method. It sends the first message to start the
	 * aggregating function and then follows the Gossip Pattern
	 */
	@Override
	protected void setup() {
		sendFirstMessage();
		super.setup();
	}

	/**
	 * checks if there is still a room with fire. Used to stop a robot
	 * 
	 * @return false if there is a room on fire, true if there isn't
	 */
	public Boolean checkFires() {
		for (Integer i : localFireMap.getFireMap().keySet()) {
			if (localFireMap.getFireStatus(i)) {
				return false;
			}
		}
		return true;

	}

	/**
	 * method to send the first message to the other robots. It reads the map of
	 * fires in the building, the starting position of the robot and sets
	 * connections between robots and sends the fire map
	 */
	public void sendFirstMessage() {
		/**
		 * load the static fireMap
		 */
		FireMap.initialize("src/config/firemap.txt");
		/**
		 * set the starting position by reading it from a file
		 */
		currentPosition = readStartPosition("src/config/startingpositions.txt");
		/**
		 * copy the content of the static map into the local map
		 */
		localFireMap.copyMap(FireMap.getMap());
		/**
		 * prepare the first message
		 */
		ACLMessage firstMessage = new ACLMessage(ACLMessage.INFORM);
		/**
		 * wait a second to be sure that every agent has started
		 */
		doWait(1000);
		/**
		 * set neighboring agents by reading from file
		 */
		List<AID> n = this.setConnections("src/config/connections.txt");
		/**
		 * add receivers to the message
		 */
		for (AID a : n) {
			firstMessage.addReceiver(a);
		}
		/**
		 * set the content of the message to the local fire map
		 */
		try {
			firstMessage.setContentObject(localFireMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
		/**
		 * send the first message
		 */
		send(firstMessage);
	}
}
