#!/bin/sh

TARGET=shift
ANT=$ANT_HOME/bin/ant

if [ "$ANT_HOME" = "" ]; then
	echo ANT_HOME is not set, please set ANT_HOME before calling this script
	exit
fi

$ANT $@ -lib ../lib -f common/buildRequirements.xml cleanRequirements

if [ "$TARGET" = "product" ]; then
	$ANT $@ -lib ../lib -f product/productBuild.xml integration
else
	$ANT $@ -lib ../lib -f builder-wrap.xml integration -Dbuilder=$TARGET
fi