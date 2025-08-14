package utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import temperatureSensors.Temperature;

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
	public abstract void printRecap(Serializable aggregatedContent, List<AID> neighbours, String localName);

	/**
	 * setup method that provides a baseline for a Gossip Agent. It receives a
	 * message, it aggregates its content with what it has received before and then
	 * sends it by following the spreading pattern
	 */
	@Override
	protected void setup() {
		doWait(1000);
		List<AID> neighbours = this.setConnections("src/config/connections.txt");

		printNeighbours(neighbours, getLocalName());

		addBehaviour(new TickerBehaviour(this, getAgentPeriod("src/config/periods.txt")) {

			@Override
			public void onTick() {
			    List<Serializable> receivedContent = new ArrayList<>();
			    ACLMessage message;

			    // reads every message in the buffer
			    while ((message = receive()) != null) {
			        try {
			            receivedContent.add((Serializable) message.getContentObject());
			        } catch (UnreadableException e) {
			            e.printStackTrace();
			        }
			    }

			    if (!receivedContent.isEmpty()) {
			        ACLMessage newMessage = new ACLMessage(ACLMessage.INFORM);
			        Serializable aggregatedContent = aggregate(receivedContent);

			        try {
			            newMessage.setContentObject(aggregatedContent);
			        } catch (IOException e) {
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
