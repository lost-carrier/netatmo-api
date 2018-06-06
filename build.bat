@ECHO OFF

SET WD=%CD%
SET SD=%~dp0
SET PARAMS=%*

cd "%SD%"

call mvnw clean install %PARAMS%

cd "%WD%"

PAUSE
