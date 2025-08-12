@echo off
echo ---------------------------------------
echo Compilazione del progetto JADE in corso
echo ---------------------------------------

rem === Pulisce la cartella bin (opzionale)
rmdir /s /q bin
mkdir bin

rem === Compila tutti i .java in bin/
javac -cp lib\jade.jar -d bin src\utils\*.java src\temperatureSensors\*.java src\robotFirefighters\*.java


if %errorlevel% neq 0 (
    echo Errore durante la compilazione!
    pause
    exit /b %errorlevel%
) else (
    echo Compilazione completata con successo.
)

pause
