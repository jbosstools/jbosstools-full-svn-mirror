#!/bin/sh

TARGET=$1
shift
PRODUCT_VERSION=$1
shift
BUILD_TYPE=$1
shift
ANT=$ANT_HOME/bin/ant

function usage
{
	echo "Usage:"
	echo ""
	echo "  $0 [site-type] [version] (build-type)"
	echo "    site-type:  The update site type to build. [development|stable]"
	echo "    version:    The version to build this update site from."
	echo "    build-type: The type of build this update site is for. [release|integration|nightly] (optional: default 'release')"
	echo ""
	exit
}

if [ "$TARGET" = "" ]; then
	usage
fi

if [ "$PRODUCT_VERSION" = "" ]; then
	usage
fi

if [ "$ANT_HOME" = "" ]; then
	echo ANT_HOME is not set, please set ANT_HOME before calling this script
	exit -1
fi

case "$BUILD_TYPE" in
	[rR]*) buildtype="R";;
	[iI]*) buildtype="I";;
	[nN]*) buildtype="N";;
	*) buildtype="R";;
esac


echo buildType=$buildtype,productVersion=$PRODUCT_VERSION,target=$TARGET

$ANT $@ -lib ../../lib -Dproduct.versionTag=$PRODUCT_VERSION -Dproduct.buildType=$buildtype -f buildSite.xml $TARGET
