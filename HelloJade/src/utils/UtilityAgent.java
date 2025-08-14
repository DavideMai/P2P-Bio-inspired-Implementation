package utils;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * class that contains a few methods that are shared across all Agents
 */
public class UtilityAgent extends Agent {

	/**
	 * method that reads a file that specifies the connections between agents based
	 * on their LocalName
	 * 
	 * @param filePath the path of the file
	 * @return a List of neighbors
	 */
	public List<AID> setConnections(String filePath) {
		List<AID> neighbors = new ArrayList<>();
		String myName = this.getLocalName();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(":");
				if (parts.length != 2)
					continue;

				String agentName = parts[0].trim();
				String[] connections = parts[1].split(",");

				if (agentName.equalsIgnoreCase(myName)) {
					for (String neighbor : connections) {
						neighbors.add(new AID(neighbor.trim(), AID.ISLOCALNAME));
					}
					break;
				}
			}

		} catch (IOException e) {
			System.err.println("Error reading connections file: " + e.getMessage());
		}

		return neighbors;
	}

	public static synchronized void printNeighbours(List<AID> neighbours, String localName) {
		System.out.println("The agent " + localName + " is connected to:\n");
		for (AID a : neighbours) {
			System.out.println(a.getLocalName());
		}
	}

	public void setReceivers(ACLMessage m, List<AID> neighbours) {
		for (AID a : neighbours) {
			m.addReceiver(a);
		}
	}

	public int getAgentPeriod(String filePath) {
		String myName = this.getLocalName();
		int value = -1; // default value if it is missing
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(":");
				if (parts.length != 2)
					continue;

				String agentName = parts[0].trim();
				if (agentName.equalsIgnoreCase(myName)) {
					value = Integer.parseInt(parts[1].trim());
					break;
				}
			}

		} catch (IOException | NumberFormatException e) {
			System.err.println("Errore nella lettura del valore per " + myName + ": " + e.getMessage());
		}

		return value;
	}

}
