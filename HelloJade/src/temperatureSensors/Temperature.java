package temperatureSensors;

import java.io.Serializable;

/**
 * Temperature class that is used to send temperature between thermometers.
 * It includes a double value, the current temperature. It has getters and setters and a basic constructor
 */
public class Temperature implements Serializable {
	private double value;

	public Temperature(double value) {
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

}
