#!/bin/bash
# Hudson script used to publish Tycho-built p2 update sites

# define target zip filename
ZIPNAME=${JOB_NAME}-Update-H${BUILD_NUMBER}-${BUILD_ID}.zip

# copy into workspace for archiving
rm -fr ${WORKSPACE}/site; mkdir -p ${WORKSPACE}/site/${JOB_NAME}
rsync -aq ${WORKSPACE}/*/site/target/site.zip ${WORKSPACE}/site/${JOB_NAME}/${ZIPNAME}

# publish to download.jboss.org
if [[ $DESTINATION == "" ]]; then DESTINATION="tools@filemgmt.jboss.org:/downloads_htdocs/tools"; fi
rsync -arzq --delete ${WORKSPACE}/site/${JOB_NAME} $DESTINATION/builds/nightly/3.2.helios/

