package utils;

import java.io.Serializable;
import java.util.List;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

public interface GossipInterface extends SpreadingInterface, AggregationInterface {

	/**
	 * method that aggregates a list of content
	 * 
	 * @param content the list of content that has to be aggregated
	 * @return a Serializable, the aggregated object
	 */
	public Serializable aggregate(List<Serializable> content);

	/**
	 * method to send a message by following the spreading pattern's rules.
	 * 
	 * @param m             the message that needs to be sent
	 * @param neighbours    the list of receivers of the message
	 * @param includeSender if true, the sender of the previous message has to be
	 *                      included in the receivers list. If false, the message
	 *                      will not be spread to its previous sender
	 * @param sender        the AID of the sender of the previous message.
	 */
	public void sendMessage(ACLMessage m, List<AID> neighbours, Boolean includeSender, AID sender);

	/**
	 * method to print something at the end of a Gossip Cycle
	 * 
	 * @param aggregatedContent the aggregated content
	 * @param neighbours        the list of agents that received the aggregated
	 *                          content
	 * @param localName         the name of the agent that did the aggregation
	 */
	public void printRecap(Serializable aggregatedContent, List<AID> neighbours, String localName);

}
