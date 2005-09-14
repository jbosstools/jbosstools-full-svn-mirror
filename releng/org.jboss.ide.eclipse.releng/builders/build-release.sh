#!/bin/sh

TARGET=$1
RELEASE=$2
shift
shift
ANT=$ANT_HOME/bin/ant

if [ "$ANT_HOME" = "" ]; then
	echo ANT_HOME is not set, please set ANT_HOME before calling this script
	exit
fi

$ANT $@ -lib ../lib -f common/buildRequirements.xml cleanRequirements

if [ "$TARGET" = "product" ]; then
	$ANT $@ -lib ../lib -f product/productBuild.xml release -DreleaseNumber=$RELEASE > build.log

	if [ "$?" = "0" ]; then
		$ANT $@ -lib ../lib -f product/buildResults.xml publish.log
	else
		echo "\t[JBossIDE-Build ERROR] There was an error running the build"
	fi

else
	$ANT $@ -lib ../lib -f builder-wrap.xml release -Dbuilder=$TARGET -DreleaseNumber=$RELEASE
fi