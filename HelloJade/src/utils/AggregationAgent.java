package utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public abstract class AggregationAgent extends UtilityAgent {

	/**
	 * 
	 */
	
	/**
	 * abstract method to aggregate informations. It could be implemented, for
	 * example, to calculate the mean of an array of numbers
	 * 
	 * @return the aggregated object
	 */
	public abstract Serializable aggregate(List<Serializable> content);

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
		List<Serializable> content = new ArrayList<>();
		addBehaviour(new TickerBehaviour(this, getAgentPeriod("src/config/periods.txt")) {

			@Override
			public void onTick() {
				Serializable aggregatedContent = null;

				ACLMessage message = receive();

				if (message != null) {
					try {
						content.add(message.getContentObject());
					} catch (UnreadableException e) {
						e.printStackTrace();
					}

					aggregatedContent = aggregate(content);

					printRecap(aggregatedContent);
				}

			}
		});
	}
}
