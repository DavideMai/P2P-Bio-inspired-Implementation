package temperatureSensors;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Random;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import utils.GossipAgent;

public class TemperatureSensor extends GossipAgent {

	Temperature t = new Temperature(0);

	/**
	 * method that generates a new temperature when it receives the mean of all the
	 * temperatures. It then calculate the mean between every temperature it
	 * received and the temperature it generated. It returns the new mean
	 */
	@Override
	public Serializable aggregate(List<Serializable> content) {
		double sum = 0.0;
		int count = 0;

		this.updateRandomTemperature();

		for (Serializable item : content) {
			if (item instanceof Temperature) {
				Temperature temp = (Temperature) item;
				sum += temp.getValue();
				count++;
			}
		}

		count++;
		sum += t.getValue();

		if (count == 0) {
			return (Serializable) new Temperature(0);
		}

		double average = sum / count;
		return (Serializable) new Temperature(average);
	}

	/**
	 * implementation of the printRecap method.
	 */
	@Override
	public void printRecap(Serializable aggregatedContent, List<AID> neighbours, String localName) {

		Temperature avgTemp = (Temperature) aggregatedContent;

		synchronized (System.out) {
			System.out.println("\n\u001B[32m========== " + localName + " ==========");
			System.out.printf("\u001B[37mTemperatura media attuale: %.2f", avgTemp.getValue());
			System.out.printf("\nTemperatura attuale nella stanza: %.2f", this.t.getValue());
			
		}

	}

	/**
	 * method that generates a starting temperature between -5°C and 40°C and sends
	 * the first message to start the gossip pattern
	 */
	public void sendFirstMessage() {
		Random rand = new Random();

		double min = -5.0;
		double max = 40.0;
		double random = min + (rand.nextDouble() * (max - min));
		t.setValue(random);

		ACLMessage firstMessage = new ACLMessage(ACLMessage.INFORM);
		doWait(1000);
		List<AID> n = this.setConnections("src/config/connections.txt");
		for (AID a : n) {
			firstMessage.addReceiver(a);
		}
		try {
			firstMessage.setContentObject(t);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		send(firstMessage);
	}

	@Override
	protected void setup() {
		this.sendFirstMessage();
		super.setup();
	}

	/**
	 * method that updates the local temperature to a new random value that is
	 * between local-1°C and local+1°C
	 */
	public void updateRandomTemperature() {
		Random rand = new Random();
		
		double current = t.getValue();
		
		double min = current -1.0;
		double max = current + 1.0;
		double random = min + (rand.nextDouble() * (max - min));

		this.t.setValue(random);
	}
}
