echo "---------------------------------------"
echo "Compilazione del progetto JADE in corso"
echo "---------------------------------------"

rm -rf bin
mkdir -p bin

javac -cp lib/jade.jar -d bin src/utils/*.java src/temperatureSensors/*.java src/robotFirefighters/*.java

if [ $? -ne 0 ]; then
    echo "Errore durante la compilazione!"
    exit 1
else
    echo "Compilazione completata con successo."
fi