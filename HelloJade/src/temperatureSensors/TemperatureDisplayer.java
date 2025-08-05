package temperatureSensors;

import java.util.List;

import jade.core.AID;
import java.io.Serializable;
import utils.GossipAgent;

public class TemperatureDisplayer extends GossipAgent {

	@Override
	public Serializable aggregate(List<Serializable> content) {
		double sum = 0.0;
		int count = 0;

		for (Serializable item : content) {
			if (item instanceof Temperature) {
				Temperature temp = (Temperature) item;
				sum += temp.getValue();
				count++;
			}
		}

		if (count == 0) {
			return (Serializable) new Temperature(0); // o null, o lancia eccezione a tua discrezione
		}

		double average = sum / count;
		return (Serializable) new Temperature(average);
	}

	@Override
	public void printRecap(Serializable aggregatedContent, List<AID> neighbours, String localName) {

		if (!(aggregatedContent instanceof Temperature)) {
			System.err.println(localName + " – contenuto non valido: " + aggregatedContent);
			return;
		}

		Temperature avgTemp = (Temperature) aggregatedContent;

		synchronized (System.out) {
			System.out.println("\n========== RECAP  (" + localName + ") ==========");
			System.out.printf("Temperatura media ricevuta: %.2f °C%n", avgTemp.getValue());

			System.out.println("Numero vicini: " + neighbours.size());
			if (!neighbours.isEmpty()) {
				System.out.print("Vicini: ");
				for (int i = 0; i < neighbours.size(); i++) {
					System.out.print(neighbours.get(i).getLocalName());
					if (i < neighbours.size() - 1)
						System.out.print(", ");
				}
				System.out.println();
			}
			System.out.println("========================================\n");
		}

	}
}
