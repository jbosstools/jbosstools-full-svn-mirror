#!/bin/bash

if [[ "$#" -eq 0 ]]; then
	echo "Usage: $0 http://path/to/file/to/download.xml"
	exit 1;
fi

filename="$1"; filename=${filename##*/}
wget -N "$1" 
svn ci -m "getExample.sh automated check-in for $1"

# Push to sftp://jbossqa@filemgmt.jboss.org/htdocs/jbosstools/examples
scp $filename jbossqa@filemgmt.jboss.org:/htdocs/jbosstools/examples/

konqueror --profile "JBT jbossqa@filemgmt index-files"

echo ""
echo "Be sure to update index.html if file is new."
echo ""

