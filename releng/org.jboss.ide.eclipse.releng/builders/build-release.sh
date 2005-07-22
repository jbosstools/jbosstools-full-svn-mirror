#!/bin/sh

TARGET=shift
RELEASE=shift
ANT=$ANT_HOME/bin/ant

if [ "$ANT_HOME" = "" ]; then
	echo ANT_HOME is not set, please set ANT_HOME before calling this script
	exit
fi

$ANT $@ -lib ../lib -f common/buildRequirements.xml cleanRequirements

if [ "$TARGET" = "product" ]; then
	$ANT $@ -lib ../lib -f product/productBuild.xml release -DreleaseNumber=$RELEASE
else
	$ANT $@ -lib ../lib -f builder-wrap.xml release -Dbuilder=$TARGET -DreleaseNumber=$RELEASE
fi