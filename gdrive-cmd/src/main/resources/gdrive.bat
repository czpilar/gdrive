@echo off
setlocal

set GDRIVE_DIR=.
set GDRIVE_ARTIFACT=${project.artifactId}-${project.version}

java -jar %GDRIVE_DIR%\lib\%GDRIVE_ARTIFACT%-spring-boot.jar %*

endlocal
@echo on
