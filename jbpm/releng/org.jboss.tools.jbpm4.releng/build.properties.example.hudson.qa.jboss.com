## BEGIN PROJECT BUILD PROPERTIES ##

# this property allows ant-contrib and pde-svn-plugin to be fetched and installed automatically for you
thirdPartyDownloadLicenseAcceptance="I accept"

projectid=jbosstools.jbpm4
zipPrefix=jbpm4
incubation=
buildType=N
version=4.0.0

mainFeatureToBuildID=org.jboss.tools.jbpm4.sdk.feature
testFeatureToBuildID=org.jboss.tools.jbpm4.tests.sdk.feature

build.steps=buildUpdate,buildTests,generateDigests,test,publish,cleanup

JAVA14_HOME=${JAVA_HOME}
JAVA50_HOME=${JAVA_HOME}
JAVA60_HOME=${JAVA_HOME}

dependencyURLs=\
http://repository.jboss.org/eclipse/galileo/GEF-runtime-3.5.1.zip,\
http://repository.jboss.org/eclipse/galileo/emf-runtime-2.5.0.zip,\
http://repository.jboss.org/eclipse/galileo/xsd-runtime-2.5.0.zip,\
http://repository.jboss.org/eclipse/galileo/wtp-R-3.1.1-20090917225226.zip
#http://repository.jboss.org/eclipse/galileo/eclipse-SDK-3.5.1-linux-gtk-x86_64.tar.gz
#http://repository.jboss.org/eclipse/galileo/eclipse-SDK-3.5.1-linux-gtk.tar.gz

repositoryURLs=\
http://download.jboss.org/jbosstools/updates/nightly/trunk/,\
http://download.eclipse.org/releases/galileo/,\
http://repository.jboss.org/eclipse/galileo/repos/eclipse-Update-R-3.5.1-200909170800.zip,\
http://hudson.qa.jboss.com/hudson/view/DevStudio/job/jbosstools-cbi-jbpm3/lastSuccessfulBuild/artifact/build/N-SNAPSHOT/jbpm3-Update-N-SNAPSHOT.zip
IUsToInstall=org.eclipse.sdk.feature.group+org.eclipse.sdk.ide+org.eclipse.core.net+org.eclipse.equinox.common+org.eclipse.core.runtime+org.eclipse.debug.core+org.eclipse.rcp.feature.group+\
org.jboss.tools.jbpm.common.feature.feature.group,org.jboss.tools.jbpm.common.source.feature.feature.group

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
projNamespace=org.jboss.tools.jbpm4
projRelengName=org.jboss.tools.jbpm4.releng

## END PROJECT BUILD PROPERTIES ##
