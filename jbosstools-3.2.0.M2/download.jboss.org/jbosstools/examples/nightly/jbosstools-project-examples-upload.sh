#!/bin/bash

# This script is run here: http://hudson.qa.jboss.com/hudson/view/DevStudio/job/jbosstools-project-examples-upload
# And archived here: https://anonsvn.jboss.org/repos/jbosstools/trunk/download.jboss.org/jbosstools/examples/nightly/jbosstools-project-examples-upload.sh
# --------------------------------------------------------------------------------
#starttime=`date +%s`
#uname -a

#if [[ $DESTINATION == "" ]]; then 
	DESTINATION="tools@filemgmt.jboss.org:/downloads_htdocs/tools"
#fi
#echo "Destination to publish: $DESTINATION"

rsync -aPr --delete $WORKSPACE/sources/* $DESTINATION/examples/nightly/

#endtime=`date +%s`; elapsed=$(echo "scale=2; ($endtime - $starttime)/60" | bc -l); # calculate diff of seconds as minutes using bc
#echo "Elapsed time: ${elapsed}mins."
