package utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.util.leap.Serializable;

public abstract class GossipAgent extends UtilityAgent {
	/**
	 * abstract method to aggregate informations. It could be implemented, for
	 * example, to calculate the mean of an array of numbers
	 * 
	 * @return the aggregated object
	 */
	public abstract Serializable aggregate(List<Serializable> content);

	/**
	 * abstract method to print something at the end of the gossip cycle
	 */
	public abstract void printRecap(Object aggregatedContent, List<AID> neighbours, String localName);

	/**
	 * setup method that provides a baseline for a Gossip Agent. It receives a
	 * message, it aggregates its content with what it has received before and then
	 * sends it by following the spreading pattern
	 */
	@Override
	protected void setup() {
		List<AID> neighbours = this.setConnections("src/connections.txt");
		List<Serializable> receivedContent = new ArrayList<>();

		printNeighbours(neighbours, getLocalName());

		addBehaviour(new TickerBehaviour(this, getAgentPeriod("src/config/periods.txt")) {

			@Override
			public void onTick() {
				ACLMessage message = receive();
				ACLMessage newMessage = new ACLMessage(ACLMessage.INFORM);
				List<AID> receivers = new ArrayList<>();

				Serializable aggregatedContent = null;

				if (message != null) {
					try {
						receivedContent.add((Serializable) message.getContentObject());
					} catch (UnreadableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					aggregatedContent = aggregate(receivedContent);
					try {
						newMessage.setContentObject(aggregatedContent);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					for (AID a : neighbours) {
						newMessage.addReceiver(a);
					}

					send(newMessage);
					
					printRecap(aggregatedContent, neighbours, getLocalName());
				}
			}
		});
	}
}
