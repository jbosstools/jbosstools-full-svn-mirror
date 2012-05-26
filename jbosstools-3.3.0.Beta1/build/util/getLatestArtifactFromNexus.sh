#!/bin/bash
# for a given maven-metadata.xml URL, and a given filename pattern, return the actual URL to that filename
# eg., https://repository.jboss.org/nexus/content/groups/public/org/drools/org.drools.updatesite/5.2.0-SNAPSHOT/maven-metadata.xml org.drools.updatesite-5.2.0-SNAPSHOT-assembly.zip
# where the metadata contains this:
# <?xml version="1.0" encoding="UTF-8"?>
# <metadata modelVersion="1.1.0">
#   <groupId>org.drools</groupId>
#   <artifactId>org.drools.updatesite</artifactId>
#   <version>5.2.0-SNAPSHOT</version>
#   <versioning>
#     <snapshot>
#       <timestamp>20110406.160435</timestamp>
# 
#       <buildNumber>85</buildNumber>
#     </snapshot>
#     <lastUpdated>20110406160435</lastUpdated>
#     <snapshotVersions>
#       <snapshotVersion>
#         <extension>zip</extension>
#         <value>5.2.0-20110406.160435-85</value>
# 
#         <updated>20110406160435</updated>
#       </snapshotVersion>

# if no two artifacts given, throw usage
if [[ $# -lt 2 ]]; then
	echo "Usage: $0 <URL> <filename>"
	echo "Example: $0 https://repository.jboss.org/nexus/content/groups/public/org/drools/org.drools.updatesite/5.2.0-SNAPSHOT/ org.drools.updatesite-5.2.0-SNAPSHOT-assembly.zip"
	exit
fi
URL=$1
filename=$2

#set up tmp dir
tmpdir=`mktemp -d`
mkdir -p $tmpdir
pushd $tmpdir >/dev/null

# get the metadata
wget --no-clobber --no-check-certificate -q ${URL}/maven-metadata.xml

# parse it
cat maven-metadata.xml | egrep "timestamp|buildNumber" > tmp.txt
timestamp=$(cat tmp.txt | grep timestamp | head -1 | sed -e "s#.*<timestamp>\(.\+\)<\/timestamp>.*#\1#g");
buildNumber=$(cat tmp.txt | grep buildNumber | head -1 | sed -e "s#.*<buildNumber>\(.\+\)<\/buildNumber>.*#\1#g");

# return amended URL
SNAPSHOT=${timestamp}-${buildNumber}
filename=${filename/SNAPSHOT/${SNAPSHOT}}

echo "${URL}/${filename}"

# cleanup
popd >/dev/null
rm -fr $tmpdir 
