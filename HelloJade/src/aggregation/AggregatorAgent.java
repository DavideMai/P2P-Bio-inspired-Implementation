package aggregation;

import java.util.ArrayList;
import java.util.List;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.StaleProxyException;

/**
 * Class that specifies an AggregatorAgent. When an AggregatorAgent receives a
 * numerical message, it saves it and does the mean of every received number. To
 * run the aggregator pattern, please use -gui
 * Aggregator1:aggregation.AggregatorAgent;A2:aggregation.SenderAgent;A3:aggregation.SenderAgent;A4:aggregation.SenderAgent;A5:aggregation.SenderAgent;A6:aggregation.SenderAgent;A7:aggregation.SenderAgent;A8:aggregation.SenderAgent;A9:aggregation.SenderAgent;A10:aggregation.SenderAgent;A11:aggregation.SenderAgent;A12:aggregation.SenderAgent;A13:aggregation.SenderAgent;A14:aggregation.SenderAgent;A15:aggregation.SenderAgent;A16:aggregation.SenderAgent
 * as parameters. Every aggregator has to contain the word Aggregator.
 */
public class AggregatorAgent extends Agent {

	@Override
	protected void setup() {
		doWait(1000);

		List<Integer> receivedNumbers = new ArrayList<Integer>();
		/**
		 * every 100ms, it searches for a new message
		 */
		addBehaviour(new TickerBehaviour(this, 100) {

			@Override
			protected void onTick() {
				float mean = (float) 0;
				ACLMessage message = receive();

				if (message != null) {
					receivedNumbers.add(Integer.parseInt(message.getContent()));
					System.out.println(this.getAgent().getLocalName() + " has received "
							+ Integer.parseInt(message.getContent()) + " from " + message.getSender().getLocalName());

				}

				mean = getMean(receivedNumbers);

				System.out.println("The mean of all received numbers is " + mean);
			}
		});

	}

	/**
	 * method to calculate the mean of a list of integers
	 * 
	 * @param numbers the list of integers
	 * @return the mean
	 */
	public float getMean(List<Integer> numbers) {
		float sum = 0;
		for (Integer i : numbers) {
			sum += i;
		}
		return sum / numbers.size();
	}
}
