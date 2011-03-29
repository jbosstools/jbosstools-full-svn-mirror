#!/bin/bash

if [[ "$#" -eq 0 ]]; then
	echo "Usage: $0 http://path/to/file/to/download.xml"
	exit 1;
fi

filename="$1"; filename=${filename##*/}
wget -N "$1" 
svn ci -m "$0 $1"

# Push to sftp://tools@filemgmt.jboss.org/downloads_htdocs/tools/examples/
scp $filename tools@filemgmt.jboss.org:/downloads_htdocs/tools/examples/

#konqueror --profile "JBT jbossqa@filemgmt index-files"

echo ""
echo "Be sure to update index.html if file is new."
echo ""

