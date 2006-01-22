@echo off

set target=%1
shift

set releaseName=%2
shift

set releaseType=%3
shift

if "%target%"=="" goto usage
if "%releaseName%"=="" goto usage
if "%releaseType%"=="" goto usage

set ant=%ANT_HOME%\bin\ant

if "%ANT_HOME%"=="" goto noAnt
call %ant% %1 %2 %3 %4 %5 %6 -lib ..\lib -f common/buildRequirements.xml cleanRequirements

if "%target%"=="product" goto productBuild
call %ant% %1 %2 %3 %4 %5 %6 -lib ..\lib -f builder-wrap.xml release -DreleaseNumber=%releaseName% -DreleaseType=%releaseType% -Dbuilder=%target%
goto end

:productBuild
call %ant% %1 %2 %3 %4 %5 %6 -lib ..\lib -f product/productBuild.xml release -DreleaseNumber=%releaseName% -DreleaseType=%releaseType% > build.log
call %ant% %1 %2 %3 %4 %5 %6 -lib ..\lib -f product/buildResults.xml publish.log
goto end

:noAnt
echo ANT_HOME is not set, please set ANT_HOME before calling this script

:usage
echo Usage:
echo         build-release [builder-name] [release name] [release type (stable or development)] (ant options)

:end