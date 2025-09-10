MAIN_CLASS="jade.Boot"

CLASSPATH=".:lib/jade.jar:bin"

CONFIG_FILE="config.txt"

AGENT_STRING=""
while IFS= read -r line; do
    AGENT_STRING="${AGENT_STRING}${line}"
done < "$CONFIG_FILE"

echo "Avvio agenti: $AGENT_STRING"
java -cp "$CLASSPATH" $MAIN_CLASS -gui $AGENT_STRING
