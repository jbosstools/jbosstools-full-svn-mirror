#!/bin/sh

TARGET=$1
shift
ANT=$ANT_HOME/bin/ant

echo target=$TARGET

if [ "$ANT_HOME" = "" ]; then
	echo ANT_HOME is not set, please set ANT_HOME before calling this script
	exit -1
fi

$ANT $@ -lib ../lib -f common/buildRequirements.xml cleanRequirements

if [ "$TARGET" = "product" ]; then
	$ANT $@ -lib ../lib -f product/productBuild.xml integration > build.log
	
	if [ "$?" = "0" ]; then
		$ANT $@ -lib ../lib -f product/buildResults.xml publish.log
	else
		echo "\t[JBossIDE-Build ERROR] There was an error running the build"
	fi
	
else
	$ANT $@ -lib ../lib -f builder-wrap.xml integration -Dbuilder=$TARGET
fi
