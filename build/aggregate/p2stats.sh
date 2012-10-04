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
devArgs=" "
devArgs="${devArgs} -DartifactRepoDirectory=${SOURCE_REPO} -DmetadataRepoDirectory=${SOURCE_REPO} "
devArgs="${devArgs} -Dp2StatsURI=https://devstudio.jboss.com/usage/${TARGET_PLATFORM}/${TARGET_FOLDER}/ -DstatsArtifactsSuffix= "
devArgs="${devArgs} -DstatsTrackedArtifacts=org.jboss.tools.jmx.feature,org.jboss.ide.eclipse.archives.feature,org.jboss.ide.eclipse.as.serverAdapter.wtp.feature,org.jboss.ide.eclipse.as.feature"

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

echo "Repo in ${SOURCE_REPO} processed."