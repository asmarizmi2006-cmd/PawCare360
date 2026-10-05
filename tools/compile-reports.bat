@echo off
rem Rebuild .jasper files
cd /d "%~dp0.."
javac -cp "lib/*" -d tools tools\CompileReports.java
java -cp "tools;lib/*" CompileReports src/reports
pause
