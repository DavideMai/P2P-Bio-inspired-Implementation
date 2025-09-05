## Framework per lo sviluppo di agenti che seguono pattern di comunicazione P2P di ispirazione biologica

In questa repository si trova un framework che permette di sviluppare agenti che seguono alcuni pattern di comunicazione presentati da Fernandez-Marquez in "Description and composition of bio-inspired design patterns:
a complete overview".

In particolare, gli agenti sviluppati possono seguire lo Spreading Pattern, l'Aggregation Pattern e il Gossip Pattern.

## Guida all'implementazione

Il framework presenta tre classi astratte, chiamate GossipAgent, SpreadingAgent e AggregationAgent, che descrivono il funzionamento di un agente che segue un determinato pattern di comunicazione, invocando alcuni metodi astratti.
Per sviluppare un agente che segua uno di questi pattern è necessario creare una classe che estenda la classe astratta che descrive il comportamento da seguire, e ridefinire alcuni metodi astratti per far sì che eseguano le azioni previste. È inoltre necessario definire un oggetto che implementi l'interfaccia Serializable che sarà il contenuto dei messaggi scambiati tra gli agenti.

## Gli esempi

Nella repository sono presenti due esempi:
- Termometri Intelligenti, nel package temperatureSensors. Modella il funzionamento di termometri che si scambiano la temperatura di una stanza e calcolano la media delle temperature in una casa, per mostrare entrambi i dati
- Robot Firefighters, nel package robotFirefighters. Modella il funzionamento di alcuni robot firefighters che si spostano in un edificio e comunicano tra di loro quando riescono a spegnere un incendio.


Ho aggiunto due file compile.bat e start_agents.bat per compilare ed eseguire gli esempi. Attualmente avviene l'esecuzione dell'esempio dei Robot Firefighters.

Tutte le informazioni necessarie all'avvio, come gli agenti da creare e i loro periodi, sono presenti in alcuni file di testo.
- In config.txt si specificano gli agenti da eseguire inserendo un agente per riga. La struttura è NomeAgente:package.Classe;
- In src/config/connections.txt si specificano le connessioni tra agenti. La linea Robot1: Robot2,Robot3,Robot4 indica che l'agente chiamato Robot1 può comunicare con gli agenti chiamati Robot2, Robot3 e Robot4.
- In src/config/periods.txt si specificano i periodi di esecuzione in millisecondi. La linea Robot1: 5000 indica che l'agente chiamato Robot1 ha un periodo di 5000ms
- In src/config/startingpositions.txt si specificano le posizioni di partenza dei robot firefighters. Robot1,10 indica che l'agente chiamato Robot1 partirà dalla stanza 10
- In src/config/firemap.txt si specificano le condizioni iniziali dell'edificio nell'esempio dei robot firefighters. La riga 0,true indica che nella stanza 0 è presente un incendio. La riga 1,false indica che nella stanza 1 non è presente un incendio.
