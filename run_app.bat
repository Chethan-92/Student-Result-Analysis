@echo off
echo =========================================================
echo  Student Result Analysis - JavaFX GUI Build ^& Launch
echo =========================================================
echo.

echo [1/3] Compiling Java source files...
if not exist bin mkdir bin
javac -d bin --module-path C:\Users\Cheth\javafx-sdk-21.0.2\lib --add-modules javafx.controls *.java
if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Compilation failed!
    pause
    exit /b %errorlevel%
)

echo [2/3] Copying stylesheet resources...
copy /Y style.css bin\style.css >nul
if %errorlevel% neq 0 (
    echo.
    echo [WARNING] Could not copy style.css stylesheet file to bin folder.
)

echo [3/3] Launching JavaFX Desktop Application...
echo.
java -cp bin --module-path C:\Users\Cheth\javafx-sdk-21.0.2\lib --add-modules javafx.controls MainFxApp
if %errorlevel% neq 0 (
    echo.
    echo [NOTE] Application exited. If you saw a 'QuantumRenderer' error,
    echo        please ensure you are running this batch file in an active,
    echo        interactive desktop user session.
    echo.
    pause
)
