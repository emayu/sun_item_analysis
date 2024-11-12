@echo off
cd %~dp0
set PATH=%CD%\node\;%PATH%
node "node\node_modules\npm\bin\npm-cli.js" %*