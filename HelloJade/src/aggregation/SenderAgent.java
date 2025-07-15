package aggregation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.lang.acl.ACLMessage;

/**
 * Class that specifies a SenderAgent. A SenderAgent generates a random number
 * between 0 and 100, and then sends it to an Aggregator. To run the aggregator
 * pattern, please use -gui
 * Aggregator1:aggregation.AggregatorAgent;A2:aggregation.SenderAgent;A3:aggregation.SenderAgent;A4:aggregation.SenderAgent;A5:aggregation.SenderAgent;A6:aggregation.SenderAgent;A7:aggregation.SenderAgent;A8:aggregation.SenderAgent;A9:aggregation.SenderAgent;A10:aggregation.SenderAgent;A11:aggregation.SenderAgent;A12:aggregation.SenderAgent;A13:aggregation.SenderAgent;A14:aggregation.SenderAgent;A15:aggregation.SenderAgent;A16:aggregation.SenderAgent
 * as parameters. Every aggregator has to contain the word Aggregator.
 */
public class SenderAgent extends Agent {

	@Override
	protected void setup() {
		doWait(1000);
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		List<AID> receivers = (getAggregatorAgents());

		for (AID a : receivers) {
			message.addReceiver(a);
		}

		/**
		 * every two seconds, it sends a new message
		 */
		addBehaviour(new TickerBehaviour(this, 2000) {

			@Override
			protected void onTick() {
				int number = new Random().nextInt(101);
				message.setContent(String.valueOf(number));

				send(message);

			}
		});

	}

	/**
	 * method to search for every active aggregator
	 * 
	 * @return the list of active aggregators
	 */
	public List<AID> getAggregatorAgents() {
		List<AID> aggregators = new ArrayList<>();

		try {
			this.doWait(1000);

			SearchConstraints constraints = new SearchConstraints();
			constraints.setMaxResults(-1L);

			AMSAgentDescription[] agents = AMSService.search(this, new AMSAgentDescription(), constraints);

			for (AMSAgentDescription agent : agents) {
				String agentName = agent.getName().getLocalName();

				if (agentName.contains("Aggregator")) {
					aggregators.add(agent.getName());
				}
			}

		} catch (Exception e) {
			System.out.println("Exception during AMS search: " + e);
			e.printStackTrace();
		}

		return aggregators;
	}

}
