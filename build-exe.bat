@echo off
rem Build Windows .exe (needs JDK 17+ jpackage)
cd /d "%~dp0"
if not exist dist\PawCare360.jar (
  echo Build the project in NetBeans first: Clean and Build.
  pause
  exit /b 1
)
jpackage --type app-image --name PawCare360 --input dist --main-jar PawCare360.jar --main-class view.LoginForm --dest exe-output --java-options "-Dfile.encoding=UTF-8"
echo.
echo Done. Run exe-output\PawCare360\PawCare360.exe
echo Put db.properties next to the .exe if MySQL settings differ.
pause
