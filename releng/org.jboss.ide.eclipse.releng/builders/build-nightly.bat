@if "%ANT_HOME%"=="" goto noAnt
call %ANT_HOME%\bin\ant %2 %3 %4 %5 -lib ..\lib -f common/buildRequirements.xml cleanRequirements

@if "%1"=="product" goto productBuild
call %ANT_HOME%\bin\ant %2 %3 %4 %5 -lib ..\lib -f builder-wrap.xml nightly -Dbuilder=%1
goto end

:productBuild
call %ANT_HOME%\bin\ant %2 %3 %4 %5 -lib ..\lib -f product/productBuild.xml nightly > build.log
call %ANT_HOME%\bin\ant %2 %3 %4 %5 -lib ..\lib -f product/buildResults.xml publish.log
goto end

:noAnt
@echo ANT_HOME is not set, please set ANT_HOME before calling this script

:end

