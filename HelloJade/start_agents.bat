@echo off
setlocal ENABLEDELAYEDEXPANSION

rem 
set JADE_LIB=lib\jade.jar
set CLASSES_DIR=classes

rem 
set AGENT_STRING=
for /f "tokens=*" %%A in (config.txt) do (
    set "line=%%A"
    set "line=!line:;=!"
    set "AGENT_STRING=!AGENT_STRING!!line!;"
)

rem 
echo Avvio agenti: %AGENT_STRING%
java -cp %JADE_LIB%;%CLASSES_DIR% jade.Boot -gui %AGENT_STRING%
