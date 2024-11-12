@echo off
cd %~dp0
set PATH=%CD%\node\;%PATH%
node "node_modules\@angular\cli\bin\ng.js" %*