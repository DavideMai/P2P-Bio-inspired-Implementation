package utils;

import java.util.List;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

public interface SpreadingInterface {

	/**
	 * method to send a message by following the spreading pattern's rules.
	 * 
	 * @param m             	the message that needs to be sent
	 * @param neighbours    	the list of receivers of the message
	 * @param includeSender		if true, the sender of the previous message has to be
	 *                      	included in the receivers list. If false, the message
	 *                      	will not be spread to its previous sender
	 * @param sender		 	the AID of the sender of the previous message.
	 */
	public void sendMessage(ACLMessage m, List<AID> neighbours, Boolean includeSender, AID sender);
}
