Total of 22 req'd tgzs/zips/jars.

BIRT 2.6 -> http://www.eclipse.org/downloads/download.php?file=/birt/downloads/build_list.php 
->
wget http://ftp.osuosl.org/pub/eclipse/birt/downloads/drops/R-R1-2_6_0-201006171315/birt-report-framework-2_6_0.zip
wget http://ftp.osuosl.org/pub/eclipse/birt/downloads/drops/R-R1-2_6_0-201006171315/birt-wtp-integration-sdk-2_6_0.zip

DTP 1.8 -> http://www.eclipse.org/datatools/downloads.php 
wget "http://www.eclipse.org/downloads/download.php?file=/datatools/downloads/drops/N_DTP_1.8/dtp-1.8.0RC2-201005210500.zip" 
http://www.eclipse.org/downloads/download.php?file=/datatools/downloads/1.8/dtp_1.8.0.zip&url=http://d2u376ub0heus3.cloudfront.net/datatools/downloads/1.8/dtp_1.8.0.zip&mirror_id=581

Eclipse 3.6 + ETF -> http://www.eclipse.org/downloads/download.php?file=/eclipse/downloads/ 
->
ftp://ftp.ussg.iu.edu/pub/eclipse/eclipse/downloads/drops/R-3.6-201006080911/
eclipse-SDK-3.6-win32.zip
eclipse-SDK-3.6-linux-gtk-x86_64.tar.gz
eclipse-SDK-3.6-linux-gtk.tar.gz
eclipse-SDK-3.6-macosx-cocoa.tar.gz
eclipse-SDK-3.6-macosx-carbon.tar.gz
eclipse-test-framework-3.6RC2.zip

EMF & XSD 2.6 ->
http://www.eclipse.org/modeling/emf/downloads/?project=emf&showAll=0&showMax=5&sortBy=date 
wget "http://www.eclipse.org/downloads/download.php?file=/modeling/emf/emf/downloads/drops/2.6.0/R201006141136/emf-runtime-2.6.0.zip&r=1" \
 "http://www.eclipse.org/downloads/download.php?file=/modeling/emf/emf/downloads/drops/2.6.0/R201006141136/xsd-runtime-2.6.0.zip&r=1" &

GEF 3.6 ->
http://www.eclipse.org/gef/downloads/?project=&showAll=0&showMax=5&sortBy=date 
wget "http://www.eclipse.org/downloads/download.php?file=/tools/gef/downloads/drops/3.6.1/M201007121555/GEF-runtime-M201007121555.zip&r=1" \
 "http://www.eclipse.org/downloads/download.php?file=/tools/gef/downloads/drops/3.6.1/M201007121555/GEF-zest-M201007121555.zip&r=1" &

Equinox 3.6 -> http://www.eclipse.org/downloads/download.php?file=/equinox/
	http://ftp.osuosl.org/pub/eclipse/equinox/drops/R-3.6-201006080911/
wget http://ftp.osuosl.org/pub/eclipse/equinox/drops/R-3.6-201006080911/org.eclipse.equinox.transforms.hook_1.0.200.v20100503.jar
wget http://ftp.osuosl.org/pub/eclipse/equinox/drops/R-3.6-201006080911/org.eclipse.equinox.transforms.xslt_1.0.200.v20100503.jar

TPTP 4.7 -> 
http://eclipse.org/tptp/home/downloads/?buildId=TPTP-4.7.0RC1-201005171900
wget "http://www.eclipse.org/downloads/download.php?file=/tptp/4.7.0/TPTP-4.7.0/tptp.runtime-TPTP-4.7.0.zip&r=1"

WTP 3.2 ->
wget "http://www.eclipse.org/downloads/download.php?file=/webtools/downloads/drops/R3.2.0/R-3.2.0-20100615235519/wtp-R-3.2.0-20100615235519.zip&r=1"

SWTBot 2.0 e36 ->
http://www.eclipse.org/swtbot/downloads.php ->
wget "http://www.eclipse.org/downloads/download.php?file=/technology/swtbot/helios/dev-build/org.eclipse.swtbot.eclipse.test.junit4-2.0.0.595-dev-e36.zip&r=1" &
wget "http://www.eclipse.org/downloads/download.php?file=/technology/swtbot/helios/dev-build/org.eclipse.swtbot-2.0.0.595-dev-e36.zip&r=1" &
wget "http://www.eclipse.org/downloads/download.php?file=/technology/swtbot/helios/dev-build/org.eclipse.swtbot.eclipse-2.0.0.595-dev-e36.zip&r=1" &
wget "http://www.eclipse.org/downloads/download.php?file=/technology/swtbot/helios/dev-build/org.eclipse.swtbot.eclipse.gef-2.0.0.595-dev-e36.zip&r=1"
wget "http://www.eclipse.org/downloads/download.php?r=1&file=/athena/repos/swtbot-update-site-2.0.0.595-dev-e36.zip&r=1"

------------

5 custom built zips:

Basebuilder from latest tag (http://wiki.eclipse.org/Platform-releng-basebuilder#Current_build_tag_for_3.6_stream_builds_.28Helios.29)
	cvs -d :pserver:anonymous@dev.eclipse.org:/cvsroot/eclipse -q co -d
org.eclipse.releng.basebuilder -r R36_RC1
org.eclipse.releng.basebuilder
	zip -r9 org.eclipse.releng.basebuilder_R36_RC1.zip org.eclipse.releng.basebuilder

m2eclipse-0.10.0.20100209-0800.zip
	run m2eclipse/build.xml

Subvsve079.I201005121900_SVNconn222.I201005121900_SVNKit133.6648_JNA323_ECF310.v201005082345-Update.zip
	run svn/build.xml
	http://divby0.blogspot.com/2010/05/my-love-hate-with-svn-part-8.html
	http://wiki.eclipse.org/Equinox/p2/Ant_Tasks/Partial_Mirroring/Example

b3 Aggregator Engine
	ssh nickb@build.eclipse.org "cd /home/data/httpd/download.eclipse.org/modeling/emft/b3/updates-3.6; zip -9r ~/b3.aggregator-repo-0.1.0.r01001_e36.zip * -x \*.gz"
	ssh nickb@build.eclipse.org "cd /home/data/httpd/download.eclipse.org/tools/buckminster/updates-3.6; zip -9r ~/buckminster-repo-1.3.0.r11439_e36.zip * -x \*.gz"
	scp nickb@build.eclipse.org:~/b3.agg*.zip .
	scp nickb@build.eclipse.org:~/buckminster*.zip .
