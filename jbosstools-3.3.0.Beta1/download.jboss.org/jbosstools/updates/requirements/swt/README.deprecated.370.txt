:: DEPRECATED ::

This has been obsoleted by the release of Eclipse 3.7.1. Marking deprecated as per https://issues.jboss.org/browse/JBIDE-9853 (just in case we need it again later).

-------

see also http://wiki.eclipse.org/Equinox/p2/Publisher#Features_And_Bundles_Publisher_Application

build like this:

REPO=/home/nboldt/eclipse/workspace-jboss/jbosstools_trunk/download.jboss.org/jbosstools/updates/requirements/swt/3.7.0.v3735b-201106220-1614/; \
rm -f ${REPO}/content.* ${REPO}/artifacts.*
java -jar /home/nboldt/eclipse/eclipse37/plugins/org.eclipse.equinox.launcher_*.jar -application \
	org.eclipse.equinox.p2.publisher.FeaturesAndBundlesPublisher -application org.eclipse.equinox.p2.publisher.FeaturesAndBundlesPublisher \
	-metadataRepository file://${REPO}/repo -artifactRepository file://${REPO}/repo -source ${REPO} -configs "*,*,*"
mv -f ${REPO}/repo/*.xml ${REPO}
rm -fr ${REPO}/repo

