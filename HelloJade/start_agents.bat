@echo off
setlocal EnableDelayedExpansion

REM === Imposta la classe principale di JADE (modifica se diverso) ===
set MAIN_CLASS=jade.Boot

REM === Cartella delle classi e librerie ===
set CLASSPATH=.;lib\jade.jar;bin

REM === File di configurazione ===
set CONFIG_FILE=config.txt

REM === Costruisce la stringa degli agenti ===
set AGENT_STRING=

for /f "usebackq delims=" %%A in ("%CONFIG_FILE%") do (
    set LINE=%%A
    set AGENT_STRING=!AGENT_STRING!!LINE!
)

echo Avvio agenti: !AGENT_STRING!
java -cp %CLASSPATH% %MAIN_CLASS% -gui !AGENT_STRING!
