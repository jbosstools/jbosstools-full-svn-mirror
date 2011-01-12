#!/bin/bash

# first, make sure JBT parent pom is built + installed into local ~/.m2 repo (?)

mkdir drools-eclipse; cd drools-eclipse

# fetch drools-eclipse sources into child folder, "drools"
svn co https://anonsvn.jboss.org/repos/labs/labs/jbossrules/trunk/drools-eclipse drools

# fetch Drools' parent pom into root folder, "drools-eclipse"
rm -fr pom.xml; wget https://anonsvn.jboss.org/repos/labs/labs/jbossrules/trunk/pom.xml

# build w/ maven using Drools' parent pom (will fail with missing deps)
kill -9 `cat /tmp/.X4-lock`; rm -fr /tmp/.X4-lock; Xvfb :4 -ac 2>&1 1>/dev/null & DISPLAY=:4 /opt/maven3/bin/mvn -B -fn clean install -f drools/pom.xml

# fetch JBT parent pom into root folder, "drools-eclipse"
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
kill -9 `cat /tmp/.X4-lock`; rm -fr /tmp/.X4-lock; Xvfb :4 -ac 2>&1 1>/dev/null & DISPLAY=:4 /opt/maven3/bin/mvn -B -fae clean install -f drools/pom.xml

