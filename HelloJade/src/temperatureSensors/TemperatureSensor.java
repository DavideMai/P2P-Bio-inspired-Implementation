package temperatureSensors;

import java.util.List;
import java.util.Random;

import utils.SenderAgent;
import jade.core.AID;
import jade.util.leap.Serializable;

public class TemperatureSensor extends SenderAgent {

	

	@Override
	public java.io.Serializable generateContent() {

		
		
		Random rand = new Random();

		double min = -5.0;
		double max = 40.0;
		double random = min + (rand.nextDouble() * (max - min));
		Temperature t = new Temperature(random);
		
		return t;
	}

}
