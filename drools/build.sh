#!/bin/bash

# first, make sure JBT parent pom is built + installed into local ~/.m2 repo (?)

# find mvn3
if [[ -f /qa/tools/opt/maven-3.0.1/bin/mvn ]]; then
	mvn3="/qa/tools/opt/maven-3.0.1/bin/mvn"
else
	for d in $(whereis mvn | grep 3); do e=$(echo $d | grep -v ".bat" | grep 3); if [[ $e ]] && [[ -x $d ]]; then mvn3=$d; break; fi; done
fi

echo "Using mvn3 = $mvn3"

devnull="2>&1 1>/dev/null"
if [[ ! ${WORKSPACE} ]]; then
	WORKSPACE=`pwd`
	devnull=""
	BUILD_NUMBER=000
fi

# create working dir (if not already present in Hudson)
mkdir -p ${WORKSPACE}/sources; cd ${WORKSPACE}/sources

# store JBT's drools pom.xml (for running JBT Drools tests)
if [[ -f ${WORKSPACE}/sources/pom.xml ]]; then
        mv ${WORKSPACE}/sources/pom.xml ${WORKSPACE}/sources/pom_drools.xml
fi

# fetch drools-eclipse sources into child folder, "sources/drools"
if [[ ! -d ${WORKSPACE}/sources/drools ]]; then svn co https://anonsvn.jboss.org/repos/labs/labs/jbossrules/trunk/drools-eclipse drools; fi

# JBIDE-1484: patch org.drools.eclipse/META-INF/MANIFEST.MF
cd ${WORKSPACE}/sources/drools/org.drools.eclipse
wget http://anonsvn.jboss.org/repos/jbosstools/trunk/drools/JBIDE-1484_MANIFEST.MF.patch
patch ${WORKSPACE}/sources/drools/org.drools.eclipse/META-INF/MANIFEST.MF JBIDE-1484_MANIFEST.MF.patch
cd ${WORKSPACE}/sources

# fetch Drools' parent pom into root folder, "sources"
rm -fr ${WORKSPACE}/sources/pom.xml; wget https://anonsvn.jboss.org/repos/labs/labs/jbossrules/trunk/pom.xml

# build w/ maven using Drools' parent pom (will fail with missing deps); suppress logged output
$mvn3 -B -fn clean install -f ${WORKSPACE}/sources/drools/pom.xml -Dmaven.repo.local=${WORKSPACE}/m2-repository $devnull

# fetch JBT parent pom into root folder, "sources"
rm -fr ${WORKSPACE}/sources/pom.xml; wget http://anonsvn.jboss.org/repos/jbosstools/trunk/build/pom.xml

# add missing pom instructions into Drools' root pom
mv ${WORKSPACE}/sources/drools/pom.xml ${WORKSPACE}/sources/drools/pom.xml_ORIG
head -n -4 ${WORKSPACE}/sources/drools/pom.xml_ORIG > ${WORKSPACE}/sources/drools/pom.xml
echo "
     <!-- added by nboldt to fix packaging and artifact naming -->
 	<plugin>
		<groupId>org.sonatype.tycho</groupId>
		<artifactId>maven-osgi-packaging-plugin</artifactId>
		<configuration>
			<format>'v'yyyyMMdd-HHmm'-H${BUILD_NUMBER}'</format>
			<archiveSite>true</archiveSite>
		</configuration>
	</plugin>

    </plugins>
  </build>
</project>" >> ${WORKSPACE}/sources/drools/pom.xml

# build w/ maven using JBT parent pom (will pass - all deps available now)
$mvn3 -B -fae clean install -f ${WORKSPACE}/sources/drools/pom.xml -Dmaven.repo.local=${WORKSPACE}/m2-repository

#revert inserted code
mv -f ${WORKSPACE}/sources/drools/pom.xml_ORIG ${WORKSPACE}/sources/drools/pom.xml

# restore from before (running JBT Drools tests)
if [[ -f ${WORKSPACE}/sources/pom_drools.xml ]]; then
	mv ${WORKSPACE}/sources/pom_drools.xml ${WORKSPACE}/sources/pom.xml
fi

