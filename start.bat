@echo off
chcp 65001 > nul
setlocal

set "JAVA_JAR=rest-services\target\sun_item_analysis_rest-0.0.1-SNAPSHOT.jar"
set "APP_PORT=8080"
set "APP_URL=http://localhost:%APP_PORT%"
set "WAIT_TIME_SECONDS=5"

where java >nul 2>^&1
if %errorlevel% neq 0 (
    echo Error: Java no se encontró en el PATH. 
    goto :efo
)

echo Iniciando servidor...
start "" java -jar "%JAVA_JAR%"

echo Esperando %WAIT_TIME_SECONDS% segundos
ping -n %WAIT_TIME_SECONDS% 127.0.0.1 > nul

echo Abriendo aplicación en el navegador...
start "" "%APP_URL%"

echo Aplicación iniciada
endlocal
pause