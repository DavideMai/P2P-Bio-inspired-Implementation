## Framework per lo sviluppo di applicazioni ad agenti auto-organizzanti mediante design pattern di ispirazione biologica

In questa repository si trova un framework che permette di sviluppare agenti che seguono alcuni pattern di comunicazione presentati da Fernandez-Marquez in "Description and composition of bio-inspired design patterns:
a complete overview".

In particolare, gli agenti sviluppati possono seguire lo Spreading Pattern, l'Aggregation Pattern e il Gossip Pattern.

## Descrizione del framework

Il framework presenta tre classi astratte, chiamate GossipAgent, SpreadingAgent e AggregationAgent, che descrivono il funzionamento di un agente che segue un determinato pattern di comunicazione, invocando alcuni metodi astratti. Presento il diagramma delle classi del framework.

![Class Diagram](Bio-inspired_patterns/img/Classes.drawio.png)
Per sviluppare un agente che segua uno di questi pattern è necessario creare una classe che estenda la classe astratta che descrive il comportamento da seguire, e ridefinire alcuni metodi astratti per far sì che eseguano le azioni previste. È inoltre necessario definire un oggetto che implementi l'interfaccia Serializable che sarà il contenuto dei messaggi scambiati tra gli agenti.

## Requisiti
Per eseguire gli esempi è necessario avere il jdk di Java 20 (o successivi) configurato correttamente sulla propria macchina. Esistono numerose guide online per l'installazione.
La versione minima di JADE è la 4.6.0, già inclusa nel framework.

## Esempi

Nella repository sono presenti due esempi basati sugli agenti:
- Termometri Intelligenti, nel package temperatureSensors. Modella il funzionamento di termometri che si scambiano la temperatura di una stanza e calcolano la media delle temperature in una casa, per mostrare entrambi i dati
- Robot Firefighters, nel package robotFirefighters. Modella il funzionamento di alcuni robot firefighters che si spostano in un edificio e comunicano tra di loro quando riescono a spegnere un incendio.

I parametri da passare a Java per creare agenti che seguono questi esempi sono:
- NomeRobot:robotFirefighters.FireFighter; per i robot firefighter
- NomeTermometro:temperatureSensors.TemperatureSensor; per i termometri intelligenti.

I parametri "NomeRobot" e "NomeTermometro" devono essere modificati per assegnare un nome all'agente.
La stringa Robot1:robotFirefighters.FireFighter; indica a JADE di creare un agente chiamato "Robot1" che segue il comportamento specificato nella classe FireFighter nel package robotFirefighters.
I nomi degli agenti sono nomi simbolici che possono essere inventati al momento. È sufficiente mantenere gli stessi nomi in ogni file di configurazione per garantire il corretto funzionamento.
Nel caso di una macchina windows, questi parametri possono essere inseriti nel file config.txt, inserendo un agente per riga.

Ho aggiunto due file compile.bat e start_agents.bat per compilare ed eseguire gli esempi. Attualmente avviene l'esecuzione dell'esempio dei Robot Firefighters.

Tutte le informazioni necessarie all'avvio, come gli agenti da creare e i loro periodi, sono presenti in alcuni file di testo. Questa lista è da seguire per cambiare esempio, o per eseguire un qualsiasi altro tipo di agente precedentemente creato.
- In config.txt si specificano gli agenti da eseguire inserendo un agente per riga. La struttura è NomeAgente:package.Classe;. Viene utilizzato solo su macchine windows, per poter utilizzare i file .bat per eseguire gli esempi. Obbligatorio per ogni esempio.
- In src/config/connections.txt si specificano le connessioni tra agenti. La linea Robot1: Robot2,Robot3,Robot4 indica che l'agente chiamato Robot1 può comunicare con gli agenti chiamati Robot2, Robot3 e Robot4. Obbligatorio per ogni esempio.
- In src/config/periods.txt si specificano i periodi di esecuzione in millisecondi. La linea Robot1: 5000 indica che l'agente chiamato Robot1 ha un periodo di 5000ms. Obbligatorio per ogni esempio.
- In src/config/startingpositions.txt si specificano le posizioni di partenza dei robot firefighters. Robot1,10 indica che l'agente chiamato Robot1 partirà dalla stanza 10. Obbligatorio per i robot firefighter
- In src/config/firemap.txt si specificano le condizioni iniziali dell'edificio nell'esempio dei robot firefighters. La riga 0,true indica che nella stanza 0 è presente un incendio. La riga 1,false indica che nella stanza 1 non è presente un incendio. Obbligatorio per i robot firefighter.

## Guida all'utilizzo

Per eseguire gli esempi è sufficiente clonare la repository sulla propria macchina ed eseguire alcuni comandi.
Nella repository i file sono già configurati per l'esecuzione dell'esempio riguardante i robot firefighters.
Per eseguire l'esempio dei termometri intelligenti, è necessario modificare i file config.txt, src/config/connections.txt, src/config/periods.txt. In particolare, nel file config.txt vanno specificati i nomi degli agenti, in connections.txt i periodi degli agenti associati ai nomi e in connections.txt le connessioni tra agenti associate ai nomi.

### Macchina Windows

Per l'esecuzione degli esempi su una macchina windows, è necessario eseguire il file windows_compile.bat e seguire le istruzioni a schermo. Successivamente, è necessario eseguire il file windows_start_agents.bat.

È necessario configurare i file sopra descritti per eseguire ogni esempio.

### Altri sistemi operativi (Linux, MacOS)

Per l'esecuzione degli esempi su altri sistemi operativi come Linux e MacOS ho creato due script bash che permettono la compilazione e l'avvio del framework, denominati linux_compile.sh e linux_start_agents.sh. Prima di poterli eseguire, è necessario conferire loro i permessi di esecuzione.
Si apre la console nella cartella del progetto (all'interno di Bio-inspired_patterns) e si digita

```bash
   chmod +x linux_compile.sh
 ```
e

```bash
   chmod +x linux_start_agents.sh
 ```
Successivamente si possono eseguire con 
```bash
   ./linux_compile.sh
 ```
e con 
```bash
   ./linux_start_agents.sh
 ```
È necessario che prima avvenga la compilazione e successivamente l'avvio del framework.
In ogni caso è di estrema importanza che tutti i nomi degli agenti presenti nei file di configurazione e che vengono passati come parametri di avvio a Java siano gli stessi. Altrimenti, gli esempi non funzionano.

### Sviluppo di altri agenti

Per sviluppare altri agenti si consiglia vivamente l'utilizzo di un IDE come Eclipse.
Utilizzando Eclipse, è sufficiente clonare la repository e, all'avvio, aprire un progetto esistente indicizzando il framework.
La libreria JADE è già inclusa nel framework.
