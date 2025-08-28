package utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

/**
 * simple class built to develop agents that simply send something periodically
 */
public abstract class SenderAgent extends PatternAgent {

	/**
	 * baseline for the setup method of an Agent that sends some data
	 */
	@Override
	public void setup() {
		doWait(1000);
		/**
		 * the list of neighbours
		 */
		List<AID> neighbours = this.setConnections("src/config/connections.txt");
		
		printNeighbours(neighbours, getLocalName());
		
		/**
		 * prepare a new message that will be sent
		 */
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		/**
		 * add the receivers of the message
		 */
		setReceivers(message, neighbours);
		
		/**
		 * add a new ticker behaviour
		 */
		addBehaviour(new TickerBehaviour(this, getAgentPeriod("src/config/periods.txt")) {

			@Override
			public void onTick() {
				try {
					/**
					 * try to generate an information and add it to the message
					 */
					Serializable a = generateContent();
					if (a != null) {
						message.setContentObject(a);
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
				 /**
				  * send the message containing the generated information
				  */
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
