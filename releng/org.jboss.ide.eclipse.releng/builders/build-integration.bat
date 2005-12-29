@echo off

REM
REM Usage:
REM
REM         build-integration [builder-name] (-tags [tagfilename]) (ant properties)
REM

set target=%1
shift

set ant=%ANT_HOME%\bin\ant

if "%1"=="-tags" goto settags
goto notags

:settags
shift
set cvstagproperties=%1
shift

:notags


if "%ANT_HOME%"=="" goto noAnt

call %ant %1 %2 %3 %4 %5 %6 -lib ..\lib -f common/buildRequirements.xml cleanRequirements

if "%target"=="product" goto productBuild
if "%cvstagproperties"=="" goto builderBuildNoTags
goto builderBuildWithTags

:builderBuildNoTags
call %ant %1 %2 %3 %4 %5 %6 -lib ..\lib -f builder-wrap.xml integration -Dbuilder=%target
goto end

:builderBuildWithTags
call %ant %1 %2 %3 %4 %5 %6 -lib ..\lib -propertyfile %cvstagsproperties -f builder-wrap.xml integration -Dbuilder=%target
goto end

:productBuild
if "%cvstagproperties"=="" goto productBuildNoTags
goto productBuildWithTags

:productBuildNoTags
call %ant %1 %2 %3 %4 %5 %6 -lib ..\lib -f product/productBuild.xml integration > build.log
call %ant %1 %2 %3 %4 %5 %6 -lib ..\lib -f product/buildResults.xml publish.log
goto end

:productBuildWithTags
call %ant %1 %2 %3 %4 %5 %6 -lib ..\lib -propertyfile %cvstagsproperties -f product/productBuild.xml integration > build.log
call %ant %1 %2 %3 %4 %5 %6 -lib ..\lib -propertyfile %cvstagsproperties -f product/buildResults.xml publish.log
goto end

:noAnt
echo ANT_HOME is not set, please set ANT_HOME before calling this script

:end
