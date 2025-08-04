package utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public abstract class SenderAgent extends UtilityAgent{
	
	/**
	 * baseline for the setup method of an Agent that sends some data
	 */
	@Override
	public void setup() {
		List<AID> neighbours = this.setConnections("src/connections.txt");
		
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		
		setReceivers(message, neighbours);
		
		addBehaviour(new TickerBehaviour(this, getAgentPeriod("src/config/periods.txt")) {

			@Override
			public void onTick() {
				try {
					message.setContentObject(generateContent());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				send(message);

			}
		});
	}
	
	/**
	 * method that generates the content of a message. Has to be implemented for every use case.
	 * @return the content of the message
	 */
	public abstract Serializable generateContent();
}
