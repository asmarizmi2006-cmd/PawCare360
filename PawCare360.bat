@echo off
rem Run PawCare360 from dist
cd /d "%~dp0dist"
if not exist db.properties copy ..\db.properties.example db.properties >nul
java -jar PawCare360.jar
pause
