#!/bin/sh


#### This is a temporary change to see if we can make UI unit tests work w/ XVFB


# /usr/X11R6/bin/Xvfb :99 -auth /etc/Xvfb.xauth &
# export DISPLAY=:99

##########################################################

TARGET=$1
shift
ANT=$ANT_HOME/bin/ant

if [ "$ANT_HOME" = "" ]; then
	echo ANT_HOME is not set, please set ANT_HOME before calling this script
	exit
fi

$ANT $@ -lib ../lib -f common/buildRequirements.xml cleanRequirements

if [ "$TARGET" = "product" ]; then
	$ANT $@ -lib ../lib -f product/productBuild.xml nightly | tee build.log

	if [ "$?" = "0" ]; then
		$ANT $@ -lib ../lib -f product/buildResults.xml publish.log
	else
		echo "\t[JBossIDE-Build ERROR] There was an error running the build"
		exit -1
	fi
	
else
	$ANT $@ -lib ../lib -f builder-wrap.xml nightly -Dbuilder=$TARGET -DcvsTag=HEAD
fi