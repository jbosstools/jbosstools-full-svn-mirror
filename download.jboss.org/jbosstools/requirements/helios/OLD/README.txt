Total of 25 req'd tgzs/zips/jars.

BIRT 2.6 -> http://www.eclipse.org/downloads/download.php?file=/birt/downloads/build_list.php 
->
wget \
http://www.eclipse.org/downloads/download.php?file=/birt/downloads/drops/R-R1-2_6_1-201009171723/birt-report-framework-2_6_1.zip\&r=1 \
http://www.eclipse.org/downloads/download.php?file=/birt/downloads/drops/R-R1-2_6_1-201009171723/birt-wtp-integration-sdk-2_6_1.zip\&r=1

DTP 1.8 -> http://www.eclipse.org/datatools/downloads.php 
wget http://www.eclipse.org/downloads/download.php?file=/datatools/downloads/1.8/dtp_1.8.1.zip\&r=1

Eclipse 3.6 + ETF -> http://download.eclipse.org/eclipse/downloads
->
wget \
http://www.eclipse.org/downloads/download.php?file=/eclipse/downloads/drops/R-3.6.1-201009090800/eclipse-SDK-3.6.1-win32.zip\&r=1 \
http://www.eclipse.org/downloads/download.php?file=/eclipse/downloads/drops/R-3.6.1-201009090800/eclipse-SDK-3.6.1-linux-gtk-x86_64.tar.gz\&r=1 \
http://www.eclipse.org/downloads/download.php?file=/eclipse/downloads/drops/R-3.6.1-201009090800/eclipse-SDK-3.6.1-linux-gtk.tar.gz\&r=1 \
http://www.eclipse.org/downloads/download.php?file=/eclipse/downloads/drops/R-3.6.1-201009090800/eclipse-SDK-3.6.1-macosx-cocoa.tar.gz\&r=1 \
http://www.eclipse.org/downloads/download.php?file=/eclipse/downloads/drops/R-3.6.1-201009090800/eclipse-SDK-3.6.1-macosx-cocoa-x86_64.tar.gz\&r=1 \
http://www.eclipse.org/downloads/download.php?file=/eclipse/downloads/drops/R-3.6.1-201009090800/eclipse-test-framework-3.6.1.zip\&r=1

EMF & XSD 2.6 ->
http://www.eclipse.org/modeling/emf/downloads/?project=emf&showAll=0&showMax=5&sortBy=date 
wget http://www.eclipse.org/downloads/download.php?file=/modeling/emf/emf/downloads/drops/2.6.x/R201009141218/emf-runtime-2.6.1.zip\&r=1 \
 "http://www.eclipse.org/downloads/download.php?file=/modeling/emf/emf/downloads/drops/2.6.x/R201009141218/xsd-runtime-2.6.1.zip&r=1" &

UML2 3.1 ->
http://www.eclipse.org/modeling/mdt/downloads/?project=uml2
wget "http://www.eclipse.org/downloads/download.php?r=1&file=/modeling/mdt/uml2/downloads/drops/3.1.1/R201009141451/mdt-uml2-runtime-3.1.1.zip"

GEF 3.6 ->
http://www.eclipse.org/gef/downloads/?project=&showAll=0&showMax=5&sortBy=date 
wget "http://www.eclipse.org/downloads/download.php?file=/tools/gef/downloads/drops/3.6.1/R201009132020/GEF-runtime-3.6.1.zip&r=1" \
 "http://www.eclipse.org/downloads/download.php?file=/tools/gef/downloads/drops/3.6.1/R201009132020/GEF-zest-3.6.1.zip&r=1" &

Equinox 3.6 -> 
http://download.eclipse.org/equinox/
http://download.eclipse.org/equinox/drops/R-3.6.1-201009090800/index.php
wget http://ftp.osuosl.org/pub/eclipse/equinox/drops/R-3.6-201006080911/org.eclipse.equinox.transforms.hook_1.0.200.v20100503.jar
wget http://ftp.osuosl.org/pub/eclipse/equinox/drops/R-3.6-201006080911/org.eclipse.equinox.transforms.xslt_1.0.200.v20100503.jar
(unchanged in 3.6.1)

RSE 3.2 -> 
wget \
"http://www.eclipse.org/downloads/download.php?file=/dsdp/tm/downloads/drops/M20101217-1000/RSE-runtime-M20101217-1000.zip&r=1" \
"http://www.eclipse.org/downloads/download.php?file=/dsdp/tm/downloads/drops/M20101217-1000/TM-terminal-M20101217-1000.zip&r=1"

TPTP 4.7 -> 
http://eclipse.org/tptp/home/downloads/?ver=4.7.1
wget "http://www.eclipse.org/downloads/download.php?file=/tptp/4.7.1/TPTP-4.7.1/tptp.runtime-TPTP-4.7.1.zip&r=1"

WTP 3.2 ->
http://download.eclipse.org/webtools/downloads/
wget http://www.eclipse.org/downloads/download.php?file=/webtools/downloads/drops/R3.2.2/R-3.2.2-20100915173744/wtp-R-3.2.2-20100915173744.zip\&r=1

SWTBot 2.0 e36 ->
http://www.eclipse.org/swtbot/downloads.php ->
wget "http://www.eclipse.org/downloads/download.php?file=/technology/swtbot/helios/dev-build/org.eclipse.swtbot.eclipse.test.junit4-2.0.1.20101106_1831-73ca7af-dev-e36.zip&r=1" &
wget "http://www.eclipse.org/downloads/download.php?file=/technology/swtbot/helios/dev-build/org.eclipse.swtbot.eclipse-2.0.1.20101106_1831-73ca7af-dev-e36.zip&r=1" &
wget "http://www.eclipse.org/downloads/download.php?file=/technology/swtbot/helios/dev-build/org.eclipse.swtbot.eclipse.gef-2.0.1.20101106_1831-73ca7af-dev-e36.zip&r=1"
wget "http://www.eclipse.org/downloads/download.php?r=1&file=/athena/repos/swtbot-update-site-2.0.1.20101106_1831-73ca7af-dev-e36.zip&r=1"

------------

5 custom built zips:

Basebuilder from latest tag (http://wiki.eclipse.org/Platform-releng-basebuilder#Current_build_tag_for_3.6_stream_builds_.28Helios.29)
	cvs -d :pserver:anonymous@dev.eclipse.org:/cvsroot/eclipse -q co -d
org.eclipse.releng.basebuilder -r R36_RC4
org.eclipse.releng.basebuilder
	zip -r9 org.eclipse.releng.basebuilder_R36_RC4.zip org.eclipse.releng.basebuilder

m2eclipse-0.10.2.20100623-1649_mylyn.zip
	run m2eclipse/build.xml

Subvsve079.I201005121900_SVNconn222.I201005121900_SVNKit133.6648_JNA323_ECF310.v201005082345-Update.zip
	run svn/build.xml
	http://divby0.blogspot.com/2010/05/my-love-hate-with-svn-part-8.html
	http://wiki.eclipse.org/Equinox/p2/Ant_Tasks/Partial_Mirroring/Example

b3 Aggregator Engine
	ssh nickb@build.eclipse.org "cd /home/data/httpd/download.eclipse.org/modeling/emft/b3/updates-3.6; zip -9r ~/downloads/athena/repos/b3.aggregator-repo-0.1.0.r01053_e36.zip * -x \*.gz"
	ssh nickb@build.eclipse.org "cd /home/data/httpd/download.eclipse.org/tools/buckminster/updates-3.6; zip -9r ~/downloads/athena/repos/buckminster-repo-1.3.1.r11579_e36.zip * -x \*.gz"
	scp nickb@build.eclipse.org:~/downloads/athena/repos/b3.agg*.zip .
	scp nickb@build.eclipse.org:~/downloads/athena/repos/buckminster*.zip .

Spring IDE 2.5
# NOTE: as of 2.5.1, creating the runnable zip using repo2runnable fails. So, instead of ant script, do this:
# 1. launch clean Eclipse 3.6.1 Classic or JEE (eg., in ~/eclipse/36clean)
# 2. install ONLY these 7 Spring IDE features into ~/eclipse/36clean:
#    org.springframework.ide.eclipse.feature
#	 org.springframework.ide.eclipse.aop.feature
#	 org.springframework.ide.eclipse.osgi.feature
#	 org.springframework.ide.eclipse.webflow.feature
#	 org.springframework.ide.eclipse.batch.feature
#	 org.springframework.ide.eclipse.integration.feature
#	 org.springframework.ide.eclipse.autowire.feature
# 3. zip up installed features/plugins using
#    cd ~/eclipse/36clean; \
#    zip -r9 ~/tru/download.jboss.org/jbosstools/requirements/helios/spring-ide-2.5.1.201011101000-RELEASE.zip \
#    $(find . -mindepth 3 -maxdepth 3 -name "???.spring*")

# Denis created an update site version, which is spring-ide-Update-2.5.1.201011101000-RELEASE.zip. 
# Used Eclipse:

# <p2.publish.featuresAndBundles source="/home/eskimo/Projects/jbds-build/jbds-build/requirements/springide/spring-ide-2.5.1.201011101000-RELEASE" repository="file:///home/eskimo/Projects/jbds-build/jbds-build/requirements/springide/spring-ide-2.5.1.201011101000-RELEASE" compress="true"/>

TestNG
# Built from ~/tru/product/extras/site/pom.xml, then repackaged to include only the testng plugin + feature (exclude all else): testng-eclipse-5.14.6.20110119.zip

# Denis created an update site version, which is testng-eclipse-Update-5.14.6.20110119_1050.zip
# (see SpringIDE approach above using Eclipse and p2 publisher).

