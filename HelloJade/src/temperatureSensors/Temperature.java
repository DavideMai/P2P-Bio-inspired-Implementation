package temperatureSensors;

import java.io.Serializable;

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

