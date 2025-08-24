package robotFirefighters;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import utils.GossipAgent;

/**
 * class that models Firefighter Robots that follow the Gossip Pattern to
 * exchange informations between them
 */

public class FireFighter extends GossipAgent {

	/**
	 * 
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
				System.out.println("\u001B[35mRobot " + localName + " can't find any other fire in the building");
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

		Integer room = 0;
		do {
			Random rand = new Random();
			Integer numberOfRooms = FireMap.getMaxRoom();

			room = rand.nextInt(0, numberOfRooms + 1);
		} while (!this.localFireMap.getRoomsOnFire().contains(room));

		this.setCurrentPosition(room);

	}

	/**
	 * method that returns the start position by reading from a file
	 * 
	 * @param pathFile the path of the configuration file
	 * @return the starting position of the robot
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
	 * @return
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
		FireMap.initialize("src/config/firemap.txt");
		currentPosition = readStartPosition("src/config/startingpositions.txt");
		localFireMap.copyMap(FireMap.getMap());
		ACLMessage firstMessage = new ACLMessage(ACLMessage.INFORM);
		doWait(1000);
		List<AID> n = this.setConnections("src/config/connections.txt");
		for (AID a : n) {
			firstMessage.addReceiver(a);
		}
		try {
			firstMessage.setContentObject(localFireMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
		send(firstMessage);
	}
}
