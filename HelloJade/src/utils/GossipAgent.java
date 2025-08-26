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

public abstract class GossipAgent extends PatternAgent implements GossipInterface {

	
	/**
	 * setup method that provides a baseline for a Gossip Agent. It receives a
	 * message, it aggregates its content with what it has received before and then
	 * sends it by following the spreading pattern
	 */
	@Override
	protected void setup() {
		doWait(1000);
		
		/**
		 * the list of AIDs that identify neighbors 
		 */
		List<AID> neighbours = this.setConnections("src/config/connections.txt");

		printNeighbours(neighbours, getLocalName());
		
		addBehaviour(new TickerBehaviour(this, getAgentPeriod("src/config/periods.txt")) {

			@Override
			public void onTick() {
				
				/**
				 * list of received content
				 */
				List<Serializable> receivedContent = new ArrayList<>();
				ACLMessage message;

				/**
				 * reads every message in the buffer, and adds its content to receivedContent
				 */
				while ((message = receive()) != null) {
					try {
						receivedContent.add(message.getContentObject());
					} catch (UnreadableException e) {
						e.printStackTrace();
					}
				}
				/**
				 * if I received something, I aggregate the content received and send it with sendMessage
				 */
				if (!receivedContent.isEmpty()) {
					ACLMessage newMessage = new ACLMessage(ACLMessage.INFORM);
					Serializable aggregatedContent = aggregate(receivedContent);

					try {
						newMessage.setContentObject(aggregatedContent);
					} catch (IOException e) {
						e.printStackTrace();
					}

					sendMessage(newMessage, neighbours, true, this.getAgent().getAID());
					printRecap(aggregatedContent, neighbours, getLocalName());
				}
			}

		});
	}
	
	/**
	 * implementation of sendMessage. If includeSender is true, it will set the
	 * receivers of the message to every agent in neighbours. Else, it doesn't
	 * include the sender in the receivers.
	 */
	@Override
	public void sendMessage(ACLMessage m, List<AID> neighbours, Boolean includeSender, AID sender) {
		for(AID a : neighbours) {
			if(includeSender || !sender.equals(a)) {
				m.addReceiver(a);
			}
		}
		send(m);
	}

}
