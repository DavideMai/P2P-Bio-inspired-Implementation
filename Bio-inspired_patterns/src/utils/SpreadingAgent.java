package utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public abstract class SpreadingAgent extends PatternAgent implements SpreadingInterface {

	@Override
	protected void setup() {
		/**
		 * the list of neighbours of the agent
		 */
		List<AID> neighbours = this.setConnections("src/connections.txt");
		/**
		 * a list of serializables that will contain the content of every message the
		 * agent receives
		 */
		List<Serializable> receivedContent = new ArrayList<>();
		printNeighbours(neighbours, getLocalName());

		/**
		 * adds a tickerBehaviour to the agent, to describe what it has to do
		 */
		addBehaviour(new TickerBehaviour(this, getAgentPeriod("src/config/periods.txt")) {

			@Override
			public void onTick() {
				ACLMessage receivedMessage = receive();
				AID sender = receivedMessage.getSender();
				/**
				 * boolean flag to check if this agent has already received a certain
				 * information. Starts at false, later it can become true
				 */
				Boolean alreadyReceived = false;

				/**
				 * new message that will be sent to neighbors.
				 */
				ACLMessage newMessage = new ACLMessage(ACLMessage.INFORM);
				try {
					/**
					 * Spreading Pattern says that if an agent receives a message it has already
					 * received, then it has to stop its execution. Here I check if the list
					 * receivedContent already contains the content of the received message. In this
					 * case, I set the boolean flag to true. Else, I add the content of the received
					 * message to the list.
					 */
					if (receivedContent.contains(receivedMessage.getContentObject())) {
						alreadyReceived = true;
					} else {
						receivedContent.add(receivedMessage.getContentObject());
					}
				} catch (UnreadableException e) {
					e.printStackTrace();
				}
				/**
				 * to follow the previously described pattern. If the boolean flag is true, the
				 * agent gets deleted from the Agent Management System.
				 */
				if (alreadyReceived) {
					doDelete();
				}
				/**
				 * if possible, set the content of the new message to the content of the
				 * received message.
				 */
				try {
					newMessage.setContentObject(receivedMessage.getContentObject());
				} catch (IOException | UnreadableException e) {
					e.printStackTrace();
				}

				/**
				 * send a message. I use false as flag to not spread the message to the sender
				 * of the received message. It already knows this information.
				 */
				sendMessage(newMessage, neighbours, false, sender);

				/**
				 * method that prints useful informations at the end of a cycle
				 */
				printRecap(newMessage, this.getAgent().getLocalName());
			}
		});
	}

	/**
	 * abstract method to print the recap of a spreading agent
	 * 
	 * @param m         the message the agent has sent
	 * @param localName the name of the agent
	 */
	public abstract void printRecap(ACLMessage m, String localName);

	/**
	 * implementation of sendMessage. If includeSender is true, it will set the
	 * receivers of the message to every agent in neighbours. Else, it doesn't
	 * include the sender in the receivers.
	 */
	@Override
	public void sendMessage(ACLMessage m, List<AID> neighbours, Boolean includeSender, AID sender) {
		for (AID a : neighbours) {
			if (includeSender || !sender.equals(a)) {
				m.addReceiver(a);
			}
		}
		send(m);
	}
}
