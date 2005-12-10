@if "%ANT_HOME%"=="" goto noAnt
call %ANT_HOME%\bin\ant %3 %4 %5 %6 -lib ..\lib -f common/buildRequirements.xml cleanRequirements

@if "%1"=="product" goto productBuild
call %ANT_HOME%\bin\ant %3 %4 %5 %6 -lib ..\lib -propertyfile %2 -f builder-wrap.xml integration -Dbuilder=%1
goto end

:productBuild
call %ANT_HOME%\bin\ant %3 %4 %5 %6 -lib ..\lib -propertyfile %2 -f product/productBuild.xml integration > build.log
call %ANT_HOME%\bin\ant %3 %4 %5 %6 -lib ..\lib -propertyfile %2 -f product/buildResults.xml publish.log
goto end

:noAnt
@echo ANT_HOME is not set, please set ANT_HOME before calling this script

:end
