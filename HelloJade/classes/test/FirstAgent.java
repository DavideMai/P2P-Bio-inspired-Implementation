package test;

/*
 * The agent, when started, waits a second and then
 * searches for every agent in the environment.
 * Then it sends a message to the agent.
 * When it receives a message, it prints its content and the sender
 * Next step should be implementing it in a better way
 */

import jade.core.AID;
import jade.core.Agent;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.lang.acl.ACLMessage;

public class FirstAgent extends Agent {
	@Override
	protected void setup() {
		System.out.println("Hello! Agent " + getLocalName() + " is ready.");

		// the next part is to get every agent in the AMS
		AMSAgentDescription[] agents = null;
		this.doWait(1000);
		try {
			SearchConstraints c = new SearchConstraints();
			c.setMaxResults(new Long(-1));
			agents = AMSService.search(this, new AMSAgentDescription(), c);
		} catch (Exception e) {
			System.out.println("There has been an exception");
		}
		
		// here the agent sends a message to every agent, including itself
		ACLMessage message = new ACLMessage();
		for (int i = 0; i < agents.length; i++) {
			message.clearAllReceiver();
			message.setContent("Hi " + agents[i].getName().getLocalName());
			message.addReceiver(agents[i].getName());
			send(message);
			System.out.println(
					"\u001B[33m" + getLocalName() + ": Message sent to \u001B[0m" + agents[i].getName().getLocalName());
		}
		ACLMessage received;
		
		/*
		 * simple loop that should be implemented in a better way to check if the agent
		 * received a message from every agent. It works because I know that every
		 * agent sends a message.
		 */
		for (int i = 0; i < agents.length; i += 0) {
			received = receive();
			if (received != null) {
				System.out.println("\u001B[32m" + getLocalName() + " has received a message. Its content is: \u001B[0m"
						+ received.getContent() + ". The message was sent by: \u001B[32m "
						+ received.getSender().getLocalName() + "\u001B[0m");
				received = null;
				i++;
			}
		}

	}
}