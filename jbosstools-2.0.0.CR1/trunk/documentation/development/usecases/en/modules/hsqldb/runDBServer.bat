@ECHO OFF
set ROOT=%~dp0
rem ECHO Restoring initial database
rem DEL "%ROOT%database\*.*"
MKDIR "%ROOT%database\"
CD "%ROOT%database\"
java -classpath "%ROOT%lib\hsqldb.jar" org.hsqldb.Server
pause