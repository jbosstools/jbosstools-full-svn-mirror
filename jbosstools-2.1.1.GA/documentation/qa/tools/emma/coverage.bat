call ant
call .\eclipse\eclipse.exe -clean -vm %JAVA_HOME%/bin/java -vmargs  -Xms64m -Xmx512m -XX:MaxPermSize=128m -Xbootclasspath/a:emma.jar
call %JAVA_HOME%/bin/java -cp emma.jar emma report -r html -in coverage.em -in coverage.ec