package temperatureSensors;

import java.io.Serializable;

public class Temperature implements Serializable {
	private static final long serialVersionUID = 1L;

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

	@Override
	public String toString() {
		return value + " °C";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Temperature that = (Temperature) obj;
		return Double.compare(that.value, value) == 0;
	}

	@Override
	public int hashCode() {
		return Double.hashCode(value);
	}
}
