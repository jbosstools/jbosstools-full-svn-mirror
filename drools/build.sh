#!/bin/bash

if [[ -f /opt/maven3/bin/mvn ]] && [[ -f /usr/bin/Xvfb ]]; then
	alias mvn3='kill -9 `cat /tmp/.X4-lock`; rm -fr /tmp/.X4-lock; /usr/bin/Xvfb :4 -ac 2>&1 1>/dev/null & DISPLAY=:4 /opt/maven3/bin/mvn'
elif [[ -f /qa/tools/opt/maven-3.0.1/bin/mvn ]]; then
	alias mvn3='/qa/tools/opt/maven-3.0.1/bin/mvn'
else
	for d in $(whereis mvn | grep 3); do e=$(echo $d | grep -v ".bat" | grep 3); if [[ $e ]] && [[ -x $d ]]; then alias mvn3=$d; break; fi; done
fi

# first, make sure JBT parent pom is built + installed into local ~/.m2 repo (?)

mkdir -p sources; cd sources

# fetch drools-eclipse sources into child folder, "drools"
if [[ ! -d drools ]]; then svn co https://anonsvn.jboss.org/repos/labs/labs/jbossrules/trunk/drools-eclipse drools; fi

# store pom.xml from trunk/drools/ for later use (running tests)
mv pom.xml pom_drools.xml

# fetch Drools' parent pom into root folder, "sources"
rm -fr pom.xml; wget https://anonsvn.jboss.org/repos/labs/labs/jbossrules/trunk/pom.xml

# build w/ maven using Drools' parent pom (will fail with missing deps); suppress logged output
mvn3 -B -fn -q clean install -f drools/pom.xml -Dmaven.repo.local=${WORKSPACE}/m2-repository 2>&1 1>/dev/null

# fetch JBT parent pom into root folder, "sources"
rm -fr pom.xml; wget http://anonsvn.jboss.org/repos/jbosstools/trunk/build/pom.xml

# add missing pom instructions into root pom
mv drools/pom.xml drools/pom.xml_ORIG
head -n -4 drools/pom.xml_ORIG > drools/pom.xml
echo "
     <!-- added by nboldt -->
 	<plugin>
		<groupId>org.sonatype.tycho</groupId>
		<artifactId>maven-osgi-packaging-plugin</artifactId>
		<configuration>
			<format>'v'yyyyMMdd-HHmm</format>
			<archiveSite>true</archiveSite>
		</configuration>
	</plugin>
	
    </plugins>
  </build>

</project>" >> drools/pom.xml

# build w/ maven using JBT parent pom (will pass - all deps available now)
mvn3 -B -fae clean install -f drools/pom.xml -Dmaven.repo.local=${WORKSPACE}/m2-repository

# recover pom.xml from trunk/drools/ for running tests
mv pom_drools.xml pom.xml

