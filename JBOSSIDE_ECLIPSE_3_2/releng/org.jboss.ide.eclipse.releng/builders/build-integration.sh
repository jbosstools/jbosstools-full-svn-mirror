#!/bin/sh

TARGET=$1
shift

if [ "$1" = "-tags" ]; then
	shift
	CVSTAGPROPERTIES=$1
	shift
fi

ANT=$ANT_HOME/bin/ant

echo target=$TARGET

if [ "$ANT_HOME" = "" ]; then
	echo ANT_HOME is not set, please set ANT_HOME before calling this script
	exit -1
fi

$ANT $@ -lib ../lib -f common/buildRequirements.xml cleanRequirements

if [ "$TARGET" = "product" ]; then
	if [ "$CVSTAGPROPERTIES" = "" ]; then
		$ANT $@ -lib ../lib -f product/productBuild.xml integration > build.log
	else
		$ANT $@ -lib ../lib -propertyfile $CVSTAGPROPERTIES -f product/productBuild.xml integration | tee build.log
	fi
	
	
	if [ "$?" = "0" ]; then
		if [ "$CVSTAGPROPERTIES" = "" ]; then
			$ANT $@ -lib ../lib -f product/buildResults.xml publish.log
		else
			$ANT $@ -lib ../lib -propertyfile $CVSTAGPROPERTIES -f product/buildResults.xml publish.log
		fi
	else
		echo "\t[JBossIDE-Build ERROR] There was an error running the build"
		exit -1
	fi
	
else
	if [ "$CVSTAGPROPERTIES" = "" ]; then
		$ANT $@ -lib ../lib -f builder-wrap.xml integration -Dbuilder=$TARGET
	else
		$ANT $@ -lib ../lib -propertyfile $CVSTAGPROPERTIES -f builder-wrap.xml integration -Dbuilder=$TARGET
	fi
fi
