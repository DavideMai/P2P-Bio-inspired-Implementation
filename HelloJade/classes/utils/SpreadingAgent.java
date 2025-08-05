package utils;

import java.io.IOException;
import java.util.List;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public abstract class SpreadingAgent extends UtilityAgent {

	@Override
	protected void setup() {
		List<AID> neighbours = this.setConnections("src/connections.txt");

		printNeighbours(neighbours, getLocalName());

		addBehaviour(new TickerBehaviour(this, getAgentPeriod("src/config/periods.txt")) {

			@Override
			public void onTick() {
				ACLMessage receivedMessage = receive();
				AID sender = receivedMessage.getSender();

				ACLMessage newMessage = new ACLMessage(ACLMessage.INFORM);
				try {
					newMessage.setContentObject(receivedMessage.getContentObject());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				for (AID aid : neighbours) {
					if (!aid.equals(sender)) {
						newMessage.addReceiver(aid);
					}
				}

				send(newMessage);

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

}
