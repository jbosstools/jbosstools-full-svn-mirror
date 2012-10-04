#!/bin/bash

# to enable debugging set to "echo"; to disable set to ":"
debug=":"

# fetch Eclipse
eclipse=~/eclipse/42clean/eclipse/eclipse

installWTPRelengTools=0;
if [[ ${installWTPRelengTools} -eq 1 ]]; then
	# fetch wtp.releng tool; want more verbose output? use -consolelog -debug -console
	${eclipse} -application org.eclipse.equinox.p2.director -noSplash \
		-repository http://download.eclipse.org/webtools/releng/repository/ \
		-installIUs org.eclipse.wtp.releng.tools.feature.feature.group
fi

# set up properties
BUILD_HOME=${HOME}/testbuildhome
SOURCE_REPO=/tmp/testartifactrepo/

TARGET_PLATFORM=juno
TARGET_FOLDER=4.0.0.Beta1

# get artifacts jar to work on
unzip -oq ~/truu/product/site/target/site_assembly.zip -d ${SOURCE_REPO}

# set up script params; '&' should NOT be unescaped; p2 api (or underlying xml) will escape it. 
# to add p2MirrorsURL, use something like "-Dp2MirrorsURL=http://www.eclipse.org/downloads/download.php?format=xml&file=/project/releases/repository/"
features=""
features="${features}org.hibernate.eclipse.feature,org.jboss.ide.eclipse.archives.feature,org.jboss.ide.eclipse.as.feature,org.jboss.ide.eclipse.freemarker.feature,"
features="${features}org.jboss.tools.cdi.deltaspike.feature,org.jboss.tools.cdi.feature,org.jboss.tools.cdi.seam.feature,org.jboss.tools.common.jdt.feature,"
features="${features}org.jboss.tools.common.mylyn.feature,org.jboss.tools.community.central.feature,org.jboss.tools.community.project.examples.feature,org.jboss.tools.forge.feature,"
features="${features}org.jboss.tools.jmx.feature,org.jboss.tools.jsf.feature,org.jboss.tools.jst.feature,org.jboss.tools.maven.cdi.feature,org.jboss.tools.maven.feature,"
features="${features}org.jboss.tools.maven.hibernate.feature,org.jboss.tools.maven.jaxrs.feature,org.jboss.tools.maven.jbosspackaging.feature,org.jboss.tools.maven.jdt.feature,"
features="${features}org.jboss.tools.maven.jpa.feature,org.jboss.tools.maven.jsf.feature,org.jboss.tools.maven.portlet.feature,org.jboss.tools.maven.profiles.feature,"
features="${features}org.jboss.tools.maven.project.examples.feature,org.jboss.tools.maven.seam.feature,org.jboss.tools.maven.sourcelookup.feature,"
features="${features}org.jboss.tools.openshift.egit.integration.feature,org.jboss.tools.openshift.express.feature,org.jboss.tools.portlet.feature,"
features="${features}org.jboss.tools.project.examples.feature,org.jboss.tools.richfaces.feature,org.jboss.tools.runtime.core.feature,org.jboss.tools.runtime.seam.detector.feature,"
features="${features}org.jboss.tools.seam.feature,org.jboss.tools.usage.feature,org.jboss.tools.vpe.browsersim.feature,org.jboss.tools.vpe.feature,org.jboss.tools.ws.feature,"
features="${features}org.jboss.tools.ws.jaxrs.feature"
#features="org.jboss.tools.jmx.feature,org.jboss.ide.eclipse.archives.feature,org.jboss.ide.eclipse.as.serverAdapter.wtp.feature,org.jboss.ide.eclipse.as.feature"

devArgs=" -DartifactRepoDirectory=${SOURCE_REPO} -DmetadataRepoDirectory=${SOURCE_REPO} -Dp2StatsURI=https://devstudio.jboss.com/usage/${TARGET_PLATFORM}/${TARGET_FOLDER}/ -DstatsArtifactsSuffix= -DstatsTrackedArtifacts=${features}"

JAVA_6_HOME=/opt/jdk1.6.0
devJRE=${JAVA_6_HOME}/bin/java
APP_NAME=org.eclipse.wtp.releng.tools.addRepoProperties
devworkspace="${BUILD_HOME}"/addRepoPropertiesWorkspace

$debug "   cmd:          " $0
$debug "   APP_NAME:     " $APP_NAME
$debug "   devworkspace: " $devworkspace
$debug "   devJRE:       " $devJRE
$debug "   devArgs:      " $devArgs
$debug
if [[ $debug == "echo" ]]; then $devJRE -version; fi
$debug

if [[ -d ${devworkspace} ]]; then rm -fr ${devworkspace}; fi
mkdir -p ${devworkspace}
# more debug output with -consolelog -debug -console
${eclipse} -nosplash -data $devworkspace --launcher.suppressErrors -application ${APP_NAME} -vm $devJRE -vmargs $devArgs

rm -fr ${BUILD_HOME}

echo "Repo in ${SOURCE_REPO} processed."
