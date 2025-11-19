@echo off

rem configuration
set outDir=outC
set mainClass=Main 
set libDir=lib
set srcDir=src


rem clearing old compiled files
if exist "%outDir%\" (
    echo Deleting old compiled files...
    rmdir /s /q "%outDir%"
)

mkdir "%outDir%\"

dir /s /B "*.java" > "%outDir%\src.txt"

rem compile
echo Compiling Java files...
javac -d "%outDir%" -cp ".;%libDir%\hamcrest-core-1.3.jar;%libDir%\junit-4.12.jar" -sourcepath "%srcDir%" @"%outDir%\src.txt"

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Compilation failed!
    pause
    exit /b %errorlevel%
)

rem create jar file
set jar=jar 
rem set jar="C:\Program Files\Java\jdk-21\bin\jar.exe"
%jar% cvf "%outDir%\JungleGame.jar" "%outDir%\."

echo Compilation successful.