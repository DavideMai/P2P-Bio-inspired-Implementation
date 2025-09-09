package utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public abstract class AggregationAgent extends PatternAgent implements AggregationInterface {

	/**
	 * abstract method to print the aggregated object
	 */
	public abstract void printRecap(Serializable aggregatedContent);

	/**
	 * baseline for the setup method of an Agent that follows the aggregation
	 * pattern. It specifies an arrayList of objects and a cyclic behaviour. In the
	 * cyclic behaviour, it receives a message and, if it isn't null, it adds its
	 * content to the previous array. Then, it aggregates the received content using
	 * the aggregate method and prints the recap
	 */
	@Override
	protected void setup() {
		/**
		 * a list of known informations
		 */
		List<Serializable> content = new ArrayList<>();
		addBehaviour(new TickerBehaviour(this, getAgentPeriod("src/config/periods.txt")) {

			@Override
			public void onTick() {
				/**
				 * a Serializable object that will represent the aggregated content
				 */
				Serializable aggregatedContent = null;

				ACLMessage message = receive();

				/**
				 * if I receive a message, I add its content to the list content
				 */
				if (message != null) {
					try {
						content.add(message.getContentObject());
					} catch (UnreadableException e) {
						e.printStackTrace();
					}

					/**
					 * I apply an aggregation function to the known content
					 */
					aggregatedContent = aggregate(content);

					/**
					 * print something about the aggregated content
					 */
					printRecap(aggregatedContent);
				}

			}
		});
	}
}
