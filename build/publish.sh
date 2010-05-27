#!/bin/bash
# Hudson script used to publish Tycho-built p2 update sites

# define target zip filename date and hudson build id marked for clarity and publication
ZIPNAME=${JOB_NAME}-Update-H${BUILD_NUMBER}-${BUILD_ID}.zip

# define target zip filename for inclusion in uberbuilder's bucky aggregator
SNAPNAME=${JOB_NAME}-Update-SNAPSHOT.zip

rm -fr ${WORKSPACE}/site; mkdir -p ${WORKSPACE}/site/${JOB_NAME}
if [[ -f ${WORKSPACE}/*/site/target/site.zip ]]; then 
	# copy into workspace for archiving
	rsync -aq ${WORKSPACE}/*/site/target/site.zip ${WORKSPACE}/site/${JOB_NAME}/${ZIPNAME}
	# copy into workspace for access by bucky aggregator
	rsync -aq ${WORKSPACE}/*/site/target/site.zip ${WORKSPACE}/site/${SNAPNAME}
fi

# if zips exist produced & renamed by ant script, copy them too
if [[ -f ${WORKSPACE}/*/*/site/target/*Update*.zip ]]; then
	rsync -aq ${WORKSPACE}/*/*/site/target/*Update*.zip ${WORKSPACE}/site/${JOB_NAME}/
fi

# publish to download.jboss.org
if [[ $DESTINATION == "" ]]; then DESTINATION="tools@filemgmt.jboss.org:/downloads_htdocs/tools"; fi
if [[ -d ${WORKSPACE}/site/${JOB_NAME} ]]; then
	rsync -arzq --delete ${WORKSPACE}/site/${JOB_NAME} $DESTINATION/builds/nightly/3.2.helios/
fi
if [[ -d ${WORKSPACE}/site/${SNAPNAME} ]]; then
	rsync -arzq --delete ${WORKSPACE}/site/${SNAPNAME} $DESTINATION/builds/nightly/3.2.helios/
fi