package spreading;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.StaleProxyException;

/**
 * Class for creating agents that follow the spreading pattern. Each agent, when
 * receiving a message, checks if it isn't null. If it isn't, it sends it to its
 * neighbors, prints the received message and stops. Before running the program,
 * make sure that the arguments are -gui
 * A1:spreading.DiffusionAgent;A2:spreading.SpreadingAgent;A3:spreading.SpreadingAgent;A4:spreading.SpreadingAgent;A5:spreading.SpreadingAgent;A6:spreading.SpreadingAgent;A7:spreading.SpreadingAgent;A8:spreading.SpreadingAgent;A9:spreading.SpreadingAgent;A10:spreading.SpreadingAgent;A11:spreading.SpreadingAgent;A12:spreading.SpreadingAgent;A13:spreading.SpreadingAgent;A14:spreading.SpreadingAgent;A15:spreading.SpreadingAgent;A16:spreading.SpreadingAgent
 */
public class SpreadingAgent extends Agent {

	/**
	 * everytime an agent starts, it starts the setup
	 */
	@Override
	protected void setup() {
		doWait(1000);
		List<AID> receivers = this.setReceivers();
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setContent(this.getLocalName());

		/**
		 * cycle to set every connection for the agent.
		 */
		for (AID a : receivers) {
			message.addReceiver(a);
		}

		/**
		 * the behavior of the Agent. It starts every 100ms aggiungi file di
		 * configurazione per il parametro temporale e per ogni cosa hard-coded
		 * (collegamenti tra nodi)
		 */
		addBehaviour(new TickerBehaviour(this, 100) {

			@Override
			protected void onTick() {
				ACLMessage receivedMessage = receive();

				if (receivedMessage != null) {
					String content = receivedMessage.getContent();
					AID sender = receivedMessage.getSender();

					/**
					 * forward the received message to every neighbor, except the sender
					 */
					ACLMessage newMessage = new ACLMessage(ACLMessage.INFORM);
					newMessage.setContent(content);
					for (AID aid : receivers) {
						if (!aid.equals(sender)) {
							newMessage.addReceiver(aid);
						}
					}
					send(newMessage);
					System.out.println(this.getAgent().getLocalName() + " received " + content);
					/**
					 * removes the behaviour once the agent has spread the received message.
					 * può essere rimosso
					 */
					this.getAgent().removeBehaviour(this);
				}

			}
		});

	}

	/**
	 * This method sets connections for every agent. I plan on using 16 Agents,
	 * named A1...A16. With the help of Gemini AI I created a connection map where
	 * every agent is connected to 4 agents.
	 * 
	 * DA ToGLIERE Aggiungi tabella correlazione articolo-codice
	 * 
	 * @return the list of receivers for the given agent
	 */
	public List<AID> setReceivers() {
		List<AID> neighbors = new ArrayList<>();
		switch (this.getAID().getLocalName()) {
		case "A1":
			doWait(100);
			neighbors.add(new AID("A16", AID.ISLOCALNAME));
			neighbors.add(new AID("A15", AID.ISLOCALNAME));
			neighbors.add(new AID("A2", AID.ISLOCALNAME));
			neighbors.add(new AID("A3", AID.ISLOCALNAME));
			break;

		case "A2":
			doWait(200);
			neighbors.add(new AID("A1", AID.ISLOCALNAME));
			neighbors.add(new AID("A3", AID.ISLOCALNAME));
			neighbors.add(new AID("A16", AID.ISLOCALNAME));
			neighbors.add(new AID("A4", AID.ISLOCALNAME));
			break;

		case "A3":
			doWait(300);
			neighbors.add(new AID("A2", AID.ISLOCALNAME));
			neighbors.add(new AID("A4", AID.ISLOCALNAME));
			neighbors.add(new AID("A1", AID.ISLOCALNAME));
			neighbors.add(new AID("A5", AID.ISLOCALNAME));
			break;

		case "A4":
			doWait(400);
			neighbors.add(new AID("A3", AID.ISLOCALNAME));
			neighbors.add(new AID("A5", AID.ISLOCALNAME));
			neighbors.add(new AID("A2", AID.ISLOCALNAME));
			neighbors.add(new AID("A6", AID.ISLOCALNAME));
			break;

		case "A5":
			doWait(500);
			neighbors.add(new AID("A4", AID.ISLOCALNAME));
			neighbors.add(new AID("A6", AID.ISLOCALNAME));
			neighbors.add(new AID("A3", AID.ISLOCALNAME));
			neighbors.add(new AID("A7", AID.ISLOCALNAME));
			break;
		case "A6":
			doWait(600);
			neighbors.add(new AID("A5", AID.ISLOCALNAME));
			neighbors.add(new AID("A7", AID.ISLOCALNAME));
			neighbors.add(new AID("A4", AID.ISLOCALNAME));
			neighbors.add(new AID("A8", AID.ISLOCALNAME));
			break;
		case "A7":
			doWait(700);
			neighbors.add(new AID("A6", AID.ISLOCALNAME));
			neighbors.add(new AID("A8", AID.ISLOCALNAME));
			neighbors.add(new AID("A5", AID.ISLOCALNAME));
			neighbors.add(new AID("A9", AID.ISLOCALNAME));
			break;
		case "A8":
			doWait(800);
			neighbors.add(new AID("A7", AID.ISLOCALNAME));
			neighbors.add(new AID("A9", AID.ISLOCALNAME));
			neighbors.add(new AID("A6", AID.ISLOCALNAME));
			neighbors.add(new AID("A10", AID.ISLOCALNAME));
			break;
		case "A9":
			doWait(900);
			neighbors.add(new AID("A8", AID.ISLOCALNAME));
			neighbors.add(new AID("A10", AID.ISLOCALNAME));
			neighbors.add(new AID("A7", AID.ISLOCALNAME));
			neighbors.add(new AID("A11", AID.ISLOCALNAME));
			break;
		case "A10":
			doWait(1000);
			neighbors.add(new AID("A9", AID.ISLOCALNAME));
			neighbors.add(new AID("A11", AID.ISLOCALNAME));
			neighbors.add(new AID("A12", AID.ISLOCALNAME));
			neighbors.add(new AID("A8", AID.ISLOCALNAME));
			break;
		case "A11":
			doWait(1100);
			neighbors.add(new AID("A10", AID.ISLOCALNAME));
			neighbors.add(new AID("A12", AID.ISLOCALNAME));
			neighbors.add(new AID("A9", AID.ISLOCALNAME));
			neighbors.add(new AID("A13", AID.ISLOCALNAME));
			break;
		case "A12":
			doWait(1200);
			neighbors.add(new AID("A11", AID.ISLOCALNAME));
			neighbors.add(new AID("A13", AID.ISLOCALNAME));
			neighbors.add(new AID("A10", AID.ISLOCALNAME));
			neighbors.add(new AID("A14", AID.ISLOCALNAME));
			break;
		case "A13":
			doWait(1300);
			neighbors.add(new AID("A12", AID.ISLOCALNAME));
			neighbors.add(new AID("A14", AID.ISLOCALNAME));
			neighbors.add(new AID("A11", AID.ISLOCALNAME));
			neighbors.add(new AID("A15", AID.ISLOCALNAME));
			break;
		case "A14":
			doWait(1400);
			neighbors.add(new AID("A13", AID.ISLOCALNAME));
			neighbors.add(new AID("A15", AID.ISLOCALNAME));
			neighbors.add(new AID("A12", AID.ISLOCALNAME));
			neighbors.add(new AID("A16", AID.ISLOCALNAME));
			break;
		case "A15":
			doWait(1500);
			neighbors.add(new AID("A13", AID.ISLOCALNAME));
			neighbors.add(new AID("A14", AID.ISLOCALNAME));
			neighbors.add(new AID("A1", AID.ISLOCALNAME));
			neighbors.add(new AID("A16", AID.ISLOCALNAME));
			break;
		case "A16":
			doWait(1600);
			neighbors.add(new AID("A15", AID.ISLOCALNAME));
			neighbors.add(new AID("A1", AID.ISLOCALNAME));
			neighbors.add(new AID("A14", AID.ISLOCALNAME));
			neighbors.add(new AID("A2", AID.ISLOCALNAME));
			break;
		default:
			System.out.println("\u001B[31mPlease check agent's name. They are wrong.\u001B[0m ");
			/**
			 * this is used to kill every agent if one of them has a wrong name. Used to
			 * guarantee a correct execution of the algorithm
			 */
			try {
				getContainerController().kill();
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}
		}
		return neighbors;
	}

}
