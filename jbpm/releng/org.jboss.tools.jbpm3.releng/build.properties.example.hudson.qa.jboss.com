## BEGIN PROJECT BUILD PROPERTIES ##

# this property allows ant-contrib and pde-svn-plugin to be fetched and installed automatically for you
thirdPartyDownloadLicenseAcceptance="I accept"

projectid=jbosstools.jbpm3
zipPrefix=jbpm3
incubation=
buildType=N
version=3.2.0

mainFeatureToBuildID=org.jboss.tools.jbpm3.sdk.feature
testFeatureToBuildID=org.jboss.tools.jbpm3.tests.feature

build.steps=buildUpdate,buildTests,generateDigests,test,publish,cleanup

JAVA14_HOME=${JAVA_HOME}
JAVA50_HOME=${JAVA_HOME}
JAVA60_HOME=${JAVA_HOME}

dependencyURLs=\
http://repository.jboss.org/eclipse/galileo/GEF-runtime-3.5.1.zip,\
http://repository.jboss.org/eclipse/galileo/emf-runtime-2.5.0.zip,\
http://repository.jboss.org/eclipse/galileo/xsd-runtime-2.5.0.zip,\
http://repository.jboss.org/eclipse/galileo/wtp-R-3.1.1-20090917225226.zip,\
http://repository.jboss.org/eclipse/galileo/eclipse-SDK-3.5.1-linux-gtk-x86_64.tar.gz
#http://repository.jboss.org/eclipse/galileo/eclipse-SDK-3.5.1-linux-gtk.tar.gz

flattenDependencies=true
parallelCompilation=true
generateFeatureVersionSuffix=true
individualSourceBundles=true

# do not sign or pack jars
#skipPack=true
skipSign=true

domainNamespace=*
projNamespace=org.jboss.tools.jbpm3
projRelengName=org.jboss.tools.jbpm3.releng

## END PROJECT BUILD PROPERTIES ##
