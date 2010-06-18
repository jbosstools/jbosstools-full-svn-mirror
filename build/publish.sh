#!/bin/bash
# Hudson script used to publish Tycho-built p2 update sites

# define target zip filename for inclusion in uberbuilder's bucky aggregator
SNAPNAME=${JOB_NAME}-Update-SNAPSHOT.zip

rm -fr ${WORKSPACE}/site; mkdir -p ${WORKSPACE}/site/${JOB_NAME}
for z in ${WORKSPACE}/sources/site/target/site.zip ${WORKSPACE}/sources/site/target/site_assembly.zip; do
	if [[ -f $z ]]; then
		# note the job name, build number, and build ID of the latest snapshot zip
		echo "JOB_NAME = ${JOB_NAME}" > ${WORKSPACE}/site/${JOB_NAME}/JOB_NAME.txt
		echo "BUILD_NUMBER = ${BUILD_NUMBER}" > ${WORKSPACE}/site/${JOB_NAME}/BUILD_NUMBER.txt
		echo "BUILD_ID = ${BUILD_ID}" > ${WORKSPACE}/site/${JOB_NAME}/BUILD_ID.txt
	
		# unzip into workspace for publishing as unpacked site
		unzip $z -fo -q -d ${WORKSPACE}/site/${JOB_NAME}/
	
		# copy into workspace for access by bucky aggregator (same name every time)
		rsync -aq $z ${WORKSPACE}/site/${SNAPNAME}
	fi
done

# if zips exist produced & renamed by ant script, copy them too
for z in $(find ${WORKSPACE} -maxdepth 5 -mindepth 3 -name "*Update*.zip"); do 
	echo "$z ..."
	unzip $z -fo -q -d ${WORKSPACE}/site/${JOB_NAME}/
	rsync -aq $z ${WORKSPACE}/site/${SNAPNAME}
done

# publish to download.jboss.org
if [[ $DESTINATION == "" ]]; then DESTINATION="tools@filemgmt.jboss.org:/downloads_htdocs/tools"; fi
if [[ -d ${WORKSPACE}/site/${JOB_NAME} ]]; then
	rsync -arzq --delete ${WORKSPACE}/site/${JOB_NAME} $DESTINATION/builds/nightly/3.2.helios/
fi
if [[ -f ${WORKSPACE}/site/${SNAPNAME} ]]; then
	# publish snapshot zip
	rsync -arzq --delete ${WORKSPACE}/site/${SNAPNAME} $DESTINATION/builds/nightly/3.2.helios/
fi
