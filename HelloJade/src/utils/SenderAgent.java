package utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public abstract class SenderAgent extends UtilityAgent {

	/**
	 * baseline for the setup method of an Agent that sends some data
	 */
	@Override
	public void setup() {
		doWait(1000);
		List<AID> neighbours = this.setConnections("src/config/connections.txt");

		printNeighbours(neighbours, getLocalName());

		ACLMessage message = new ACLMessage(ACLMessage.INFORM);

		setReceivers(message, neighbours);

		addBehaviour(new TickerBehaviour(this, getAgentPeriod("src/config/periods.txt")) {

			@Override
			public void onTick() {
				try {
					Serializable a = generateContent();
					if (a != null) {
						message.setContentObject(a);
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

				send(message);

			}
		});
	}

	/**
	 * method that generates the content of a message. Has to be implemented for
	 * every use case.
	 * 
	 * @return the content of the message
	 */
	public abstract Serializable generateContent();
}
