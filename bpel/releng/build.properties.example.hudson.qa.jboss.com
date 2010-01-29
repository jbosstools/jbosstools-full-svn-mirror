## BEGIN PROJECT BUILD PROPERTIES ##

# this property allows ant-contrib and pde-svn-plugin to be fetched and installed automatically for you
thirdPartyDownloadLicenseAcceptance="I accept"

projectid=jbosstools.bpel
zipPrefix=BPEL
incubation=
buildType=N
version=1.0.0

mainFeatureToBuildID=org.jboss.tools.bpel.sdk.feature
testFeatureToBuildID=org.jboss.tools.bpel.tests.feature

# on windows or mac, use testLocal; on linux use test (requires Xvnc or Xvfb)
build.steps=buildUpdate,buildTests,generateDigests,test,publish,cleanup

JAVA14_HOME=${JAVA_HOME}
JAVA50_HOME=${JAVA_HOME}
JAVA60_HOME=${JAVA_HOME}

# To make the build go faster, use locally cached zips instead of galileo site 
repositoryURLs=\
http://download.jboss.org/jbosstools/updates/nightly/trunk/,\
http://download.eclipse.org/releases/galileo/,\
http://repository.jboss.org/eclipse/galileo/repos/eclipse-Update-R-3.5.1-200909170800.zip,\
http://repository.jboss.org/eclipse/galileo/repos/GEF-Update-3.5.1.zip,\
http://repository.jboss.org/eclipse/galileo/repos/dtp-Updates-1.7-20090908.zip,\
http://repository.jboss.org/eclipse/galileo/repos/emf-xsd-Update-2.5.0.zip
#,\
#http://repository.jboss.org/eclipse/galileo/repos/jst-buildrepo-R-3.1.1-20090917225226.zip,\
#http://repository.jboss.org/eclipse/galileo/repos/wst-buildrepo-R-3.1.1-20090917225226.zip

# required if building bpel.compare.ui: org.eclipse.gmf.runtime.diagram.ui+org.eclipse.emf.compare.match+org.eclipse.emf.compare.diff+org.eclipse.emf.compare.ui+\
IUsToInstall=org.eclipse.sdk.feature.group+org.eclipse.sdk.ide+org.eclipse.core.net+org.eclipse.equinox.common+org.eclipse.core.runtime+org.eclipse.debug.core+org.eclipse.rcp.feature.group+\
org.eclipse.jst.server.generic.core+\
org.eclipse.wst.ws_core.feature.feature.group+org.eclipse.wst.web_ui.feature.feature.group+org.eclipse.wst.ws_wsdl15.feature.feature.group+\
org.eclipse.wst.xml_ui.feature.feature.group+org.eclipse.wst.common_ui.feature.feature.group+org.eclipse.wst.common_core.feature.feature.group+\
org.eclipse.xsd.feature.group+org.eclipse.gef.feature.group+\
org.mozilla.xpcom+org.jboss.tools.vpe.resref+org.jboss.tools.jst.web.ui+org.jboss.ide.eclipse.as.core+org.jboss.ide.eclipse.archives.webtools+org.jboss.tools.jmx.feature.feature.group+\
org.jboss.tools.project.examples

flattenDependencies=true
parallelCompilation=true
generateFeatureVersionSuffix=true
individualSourceBundles=true

# don't suppress cleanup if tests fail
noclean=false

# do not sign or pack jars
#skipPack=true
skipSign=true

domainNamespace=*
projNamespace=org.jboss.tools.bpel
projRelengName=org.jboss.tools.bpel.releng

## END PROJECT BUILD PROPERTIES ##
