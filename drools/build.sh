#!/bin/bash

# first, make sure JBT parent pom is built + installed into local ~/.m2 repo (?)

# find mvn3
if [[ -f /qa/tools/opt/maven-3.0.1/bin/mvn ]]; then
	mvn3="/qa/tools/opt/maven-3.0.1/bin/mvn"
else
	for d in $(whereis mvn | grep 3); do e=$(echo $d | grep -v ".bat" | grep 3); if [[ $e ]] && [[ -x $d ]]; then mvn3=$d; break; fi; done
fi

devnull="2>&1 1>/dev/null"
if [[ ! ${WORKSPACE} ]]; then
	WORKSPACE=.
	devnull=""
	BUILD_NUMBER=000
fi

# create working dir (if not already present in Hudson)
mkdir -p sources; cd sources

# fetch drools-eclipse sources into child folder, "sources/drools"
if [[ ! -d drools ]]; then svn co https://anonsvn.jboss.org/repos/labs/labs/jbossrules/trunk/drools-eclipse drools; fi

# fetch Drools' parent pom into root folder, "sources"
rm -fr pom.xml; wget https://anonsvn.jboss.org/repos/labs/labs/jbossrules/trunk/pom.xml

# build w/ maven using Drools' parent pom (will fail with missing deps); suppress logged output
$mvn3 -B -fn clean install -f drools/pom.xml -Dmaven.repo.local=${WORKSPACE}/m2-repository $devnull

# fetch JBT parent pom into root folder, "sources"
rm -fr pom.xml; wget http://anonsvn.jboss.org/repos/jbosstools/trunk/build/pom.xml

# add missing pom instructions into root pom
mv drools/pom.xml drools/pom.xml_ORIG
head -n -4 drools/pom.xml_ORIG > drools/pom.xml
echo "
     <!-- added by nboldt to fix packaging and artifact naming -->
 	<plugin>
		<groupId>org.sonatype.tycho</groupId>
		<artifactId>maven-osgi-packaging-plugin</artifactId>
		<configuration>
			<format>'v'yyyyMMdd-HHmm'-H${BUILD_NUMBER}</format>
			<archiveSite>true</archiveSite>
		</configuration>
	</plugin>
    </plugins>
  </build>
</project>" >> drools/pom.xml

# build w/ maven using JBT parent pom (will pass - all deps available now)
$mvn3 -B -fae clean install -f drools/pom.xml -Dmaven.repo.local=${WORKSPACE}/m2-repository

# restore from before (running JBT Drools tests)
if [[ -f pom_drools.xml ]]; then
	mv pom_drools.xml pom.xml
fi
