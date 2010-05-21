#!/bin/bash
# Hudson script used to publish Tycho-built p2 update sites

# define target zip filename
ZIPNAME=${JOB_NAME}-Update-H${BUILD_NUMBER}-${BUILD_ID}.zip

# copy into workspace for archiving
rm -fr ${WORKSPACE}/${JOB_NAME}-Update*.zip
rsync -aq ${WORKSPACE}/*/site/target/site.zip ${WORKSPACE}/${ZIPNAME}

# publish to download.jboss.org
if [[ $DESTINATION == "" ]]; then DESTINATION="tools@filemgmt.jboss.org:/downloads_htdocs/tools"; fi
rsync -aq --delete ${WORKSPACE}/${ZIPNAME} $DESTINATION/builds/nightly/3.2.helios/${JOB_NAME}/


