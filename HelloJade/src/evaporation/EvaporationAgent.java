package evaporation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.StaleProxyException;

/**
 * this class models an Agent that follows the evaporation pattern. When an
 * agent receives a message, it stores its content into a HashMap with relevance
 * 10 After a tick (5 seconds), the relevance gets reduced by 1. When the
 * relevance becomes 0, the corresponding content gets removed from the map. To
 * run this pattern, please use -gui
 * A1:evaporation.EvaporationAgent;A2:evaporation.EvaporationAgent;A3:evaporation.EvaporationAgent;A4:evaporation.EvaporationAgent;A5:evaporation.EvaporationAgent;A6:evaporation.EvaporationAgent;A7:evaporation.EvaporationAgent;A8:evaporation.EvaporationAgent;A9:evaporation.EvaporationAgent;A10:evaporation.EvaporationAgent;A11:evaporation.EvaporationAgent;A12:evaporation.EvaporationAgent;A13:evaporation.EvaporationAgent;A14:evaporation.EvaporationAgent;A15:evaporation.EvaporationAgent;A16:evaporation.EvaporationAgent
 * as program arguments.
 * 
 * THERE MAY BE NO FURTHER DEVELOPMENT
 */
public class EvaporationAgent extends Agent {

	@Override
	public void setup() {

		doWait(1000);
		/**
		 * map to set the relevance of a received information after time.
		 */
		Map<String, Integer> receivedContent = new HashMap<>();

		List<AID> receivers = this.setReceivers();
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);

		for (AID a : receivers) {
			message.addReceiver(a);
		}

		int randomNumber = new Random().nextInt(101);
		message.setContent(String.valueOf(randomNumber));
		send(message);

		addBehaviour(new TickerBehaviour(this, 2000) {

			@Override
			protected void onTick() {
				int number = new Random().nextInt(101);
				message.setContent(String.valueOf(number));

				send(message);

				/**
				 * cycle that reduces the relevance of a message of 1. If a message has a
				 * relevance of 0, it gets removed.
				 */
				Iterator<Map.Entry<String, Integer>> iterator = receivedContent.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry<String, Integer> entry = iterator.next();
					int newValue = entry.getValue() - 1;

					if (newValue == 0) {
						iterator.remove();
					} else {
						entry.setValue(newValue);
					}
				}

				ACLMessage receivedMessage = receive();
				Boolean messageFound = false;

				/**
				 * cycle that searches if the received message is already present in the map. If
				 * it is, it restores its relevance to 10
				 */
				if (message != null) {
					for (Map.Entry<String, Integer> entry : receivedContent.entrySet()) {
						if (entry.getKey().equals(receivedMessage.getContent())) {
							entry.setValue(10);
							messageFound = true;
						}
					}

					/**
					 * if the received message isn't in the map, add it and set its importance to 10
					 */
					if (!messageFound) {
						receivedContent.put(receivedMessage.getContent(), 10);
					}
				}
				printReceivedContent(receivedContent, getLocalName());

			}
		});
	}

	/**
	 * This method sets connections for every agent. I plan on using 16 Agents,
	 * named A1...A16. With the help of Gemini AI I created a connection map where
	 * every agent is connected to 4 agents.
	 * 
	 * @return the list of receivers for the given agent
	 */
	public List<AID> setReceivers() {
		List<AID> neighbors = new ArrayList<>();
		switch (this.getAID().getLocalName()) {
		case "A1":
			doWait(100);
			neighbors.add(new AID("A16", AID.ISLOCALNAME));
			neighbors.add(new AID("A15", AID.ISLOCALNAME));
			neighbors.add(new AID("A2", AID.ISLOCALNAME));
			neighbors.add(new AID("A3", AID.ISLOCALNAME));
			break;

		case "A2":
			doWait(200);
			neighbors.add(new AID("A1", AID.ISLOCALNAME));
			neighbors.add(new AID("A3", AID.ISLOCALNAME));
			neighbors.add(new AID("A16", AID.ISLOCALNAME));
			neighbors.add(new AID("A4", AID.ISLOCALNAME));
			break;

		case "A3":
			doWait(300);
			neighbors.add(new AID("A2", AID.ISLOCALNAME));
			neighbors.add(new AID("A4", AID.ISLOCALNAME));
			neighbors.add(new AID("A1", AID.ISLOCALNAME));
			neighbors.add(new AID("A5", AID.ISLOCALNAME));
			break;

		case "A4":
			doWait(400);
			neighbors.add(new AID("A3", AID.ISLOCALNAME));
			neighbors.add(new AID("A5", AID.ISLOCALNAME));
			neighbors.add(new AID("A2", AID.ISLOCALNAME));
			neighbors.add(new AID("A6", AID.ISLOCALNAME));
			break;

		case "A5":
			doWait(500);
			neighbors.add(new AID("A4", AID.ISLOCALNAME));
			neighbors.add(new AID("A6", AID.ISLOCALNAME));
			neighbors.add(new AID("A3", AID.ISLOCALNAME));
			neighbors.add(new AID("A7", AID.ISLOCALNAME));
			break;
		case "A6":
			doWait(600);
			neighbors.add(new AID("A5", AID.ISLOCALNAME));
			neighbors.add(new AID("A7", AID.ISLOCALNAME));
			neighbors.add(new AID("A4", AID.ISLOCALNAME));
			neighbors.add(new AID("A8", AID.ISLOCALNAME));
			break;
		case "A7":
			doWait(700);
			neighbors.add(new AID("A6", AID.ISLOCALNAME));
			neighbors.add(new AID("A8", AID.ISLOCALNAME));
			neighbors.add(new AID("A5", AID.ISLOCALNAME));
			neighbors.add(new AID("A9", AID.ISLOCALNAME));
			break;
		case "A8":
			doWait(800);
			neighbors.add(new AID("A7", AID.ISLOCALNAME));
			neighbors.add(new AID("A9", AID.ISLOCALNAME));
			neighbors.add(new AID("A6", AID.ISLOCALNAME));
			neighbors.add(new AID("A10", AID.ISLOCALNAME));
			break;
		case "A9":
			doWait(900);
			neighbors.add(new AID("A8", AID.ISLOCALNAME));
			neighbors.add(new AID("A10", AID.ISLOCALNAME));
			neighbors.add(new AID("A7", AID.ISLOCALNAME));
			neighbors.add(new AID("A11", AID.ISLOCALNAME));
			break;
		case "A10":
			doWait(1000);
			neighbors.add(new AID("A9", AID.ISLOCALNAME));
			neighbors.add(new AID("A11", AID.ISLOCALNAME));
			neighbors.add(new AID("A12", AID.ISLOCALNAME));
			neighbors.add(new AID("A8", AID.ISLOCALNAME));
			break;
		case "A11":
			doWait(1100);
			neighbors.add(new AID("A10", AID.ISLOCALNAME));
			neighbors.add(new AID("A12", AID.ISLOCALNAME));
			neighbors.add(new AID("A9", AID.ISLOCALNAME));
			neighbors.add(new AID("A13", AID.ISLOCALNAME));
			break;
		case "A12":
			doWait(1200);
			neighbors.add(new AID("A11", AID.ISLOCALNAME));
			neighbors.add(new AID("A13", AID.ISLOCALNAME));
			neighbors.add(new AID("A10", AID.ISLOCALNAME));
			neighbors.add(new AID("A14", AID.ISLOCALNAME));
			break;
		case "A13":
			doWait(1300);
			neighbors.add(new AID("A12", AID.ISLOCALNAME));
			neighbors.add(new AID("A14", AID.ISLOCALNAME));
			neighbors.add(new AID("A11", AID.ISLOCALNAME));
			neighbors.add(new AID("A15", AID.ISLOCALNAME));
			break;
		case "A14":
			doWait(1400);
			neighbors.add(new AID("A13", AID.ISLOCALNAME));
			neighbors.add(new AID("A15", AID.ISLOCALNAME));
			neighbors.add(new AID("A12", AID.ISLOCALNAME));
			neighbors.add(new AID("A16", AID.ISLOCALNAME));
			break;
		case "A15":
			doWait(1500);
			neighbors.add(new AID("A13", AID.ISLOCALNAME));
			neighbors.add(new AID("A14", AID.ISLOCALNAME));
			neighbors.add(new AID("A1", AID.ISLOCALNAME));
			neighbors.add(new AID("A16", AID.ISLOCALNAME));
			break;
		case "A16":
			doWait(1600);
			neighbors.add(new AID("A15", AID.ISLOCALNAME));
			neighbors.add(new AID("A1", AID.ISLOCALNAME));
			neighbors.add(new AID("A14", AID.ISLOCALNAME));
			neighbors.add(new AID("A2", AID.ISLOCALNAME));
			break;
		default:
			System.out.println("\u001B[31mPlease check agent's name. They are wrong.\u001B[0m ");
			/**
			 * this is used to kill every agent if one of them has a wrong name. Used to
			 * guarantee a correct execution of the algorithm
			 */
			try {
				getContainerController().kill();
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}
		}
		return neighbors;
	}

	/**
	 * synchronized method to print the received content of an Agent. I need this to
	 * not make the outputs overlap.
	 * 
	 * @param receivedContent
	 * @param localName
	 */
	public static synchronized void printReceivedContent(Map<String, Integer> receivedContent, String localName) {

		System.out.println("\n--- Agent " + localName + " received content summary ---");
		if (receivedContent.isEmpty()) {
			System.out.println("No content received.");
		} else {
			for (Map.Entry<String, Integer> entry : receivedContent.entrySet()) {
				System.out.println("Content: " + entry.getKey() + " | Relevance: " + entry.getValue());
			}
		}
		System.out.println("--------------------------------------------\n");
	}

}
