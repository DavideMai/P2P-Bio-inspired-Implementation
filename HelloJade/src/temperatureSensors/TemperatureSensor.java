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
		
		/**
		 * update the temperature of the room
		 */
		this.updateRandomTemperature();
		
		/**
		 * update the sum and the count for every temperature the sensor receives
		 */
		for (Serializable item : content) {
			if (item instanceof Temperature) {
				Temperature temp = (Temperature) item;
				sum += temp.getValue();
				count++;
			}
		}
		
		/**
		 * add 1 to count to account for its own temperature. Add its own temperature to the sum
		 */
		count++;
		sum += t.getValue();
		
		/**
		 * if there are no temperatures, return a new temperature of 0
		 */
		if (count == 0) {
			return (Serializable) new Temperature(0);
		}
		
		/**
		 * calculate the average and return it
		 */
		double average = sum / count;
		return (Serializable) new Temperature(average);
	}

	/**
	 * implementation of the printRecap method.
	 */
	@Override
	public void printRecap(Serializable aggregatedContent, List<AID> neighbours, String localName) {

		Temperature avgTemp = (Temperature) aggregatedContent;

		synchronized (TemperatureSensor.class) {
			System.out.println("\n\u001B[32m========== " + localName + " ==========");
			System.out.printf("\u001B[37mCurrent average temperature: %.2f°C", avgTemp.getValue());
			System.out.printf("\nCurrent temperature in the room: %.2f°C", this.t.getValue());

		}

	}

	/**
	 * method that generates a starting temperature between -5°C and 40°C and sends
	 * the first message to start the gossip pattern
	 */
	public void sendFirstMessage() {
		Random rand = new Random();
		
		/**
		 * define limits for the starting temperature, and generate it
		 */
		double min = -5.0;
		double max = 40.0;
		double random = min + (rand.nextDouble() * (max - min));
		t.setValue(random);
		
		/**
		 * prepare the first message that will be sent
		 */
		ACLMessage firstMessage = new ACLMessage(ACLMessage.INFORM);
		doWait(1000);
		/**
		 * set receivers for the message
		 */
		List<AID> n = this.setConnections("src/config/connections.txt");
		for (AID a : n) {
			firstMessage.addReceiver(a);
		}
		/**
		 * set the content of the message to the starting temperature
		 */
		try {
			firstMessage.setContentObject(t);
		} catch (IOException e) {
			e.printStackTrace();
		}
		/**
		 * send the starting temperature
		 */
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

		double min = current - 1.0;
		double max = current + 1.0;
		double random = min + (rand.nextDouble() * (max - min));

		this.t.setValue(random);
	}
}
