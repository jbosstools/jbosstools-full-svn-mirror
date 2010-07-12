#!/bin/bash
# Hudson script used to publish Tycho-built p2 update sites
# NOTE: sources MUST be checked out into ${WORKSPACE}/sources 

# releases get named differently than snapshots
if [[ ${RELEASE} == "Yes" ]]; then
	ZIPSUFFIX="${BUILD_ID}-H${BUILD_NUMBER}"
else
	ZIPSUFFIX="SNAPSHOT"
fi

# define target update zip filename
SNAPNAME="${JOB_NAME}-Update-${ZIPSUFFIX}.zip"
# define target sources zip filename
SRCSNAME="${JOB_NAME}-Sources-${ZIPSUFFIX}.zip"
# define suffix to use for additional update sites
SUFFNAME="-Update-${ZIPSUFFIX}.zip"

if [[ $DESTINATION == "" ]]; then DESTINATION="tools@filemgmt.jboss.org:/downloads_htdocs/tools/builds/nightly/3.2.helios"; fi

# cleanup from last time
rm -fr ${WORKSPACE}/site; mkdir -p ${WORKSPACE}/site/${JOB_NAME}

# check for aggregate zip or overall zip
z=""
if [[ -d ${WORKSPACE}/sources/aggregate/site/target ]]; then
	siteZip=${WORKSPACE}/sources/aggregate/site/target/site_assembly.zip
	if [[ ! -f ${WORKSPACE}/sources/aggregate/site/target/site_assembly.zip ]]; then
		siteZip=${WORKSPACE}/sources/aggregate/site/target/site.zip
	fi
	z=$siteZip
elif [[ -d ${WORKSPACE}/sources/site/target ]]; then
	siteZip=${WORKSPACE}/sources/site/target/site_assembly.zip
	if [[ ! -f ${WORKSPACE}/sources/site/target/site_assembly.zip ]]; then
		siteZip=${WORKSPACE}/sources/site/target/site.zip
	fi
	z=$siteZip
fi

if [[ $z != "" ]] && [[ -f $z ]] ; then
	#echo "$z ..."
	# note the job name, build number, and build ID of the latest snapshot zip
	echo "JOB_NAME = ${JOB_NAME}" > ${WORKSPACE}/site/${JOB_NAME}/JOB_NAME.txt
	echo "BUILD_NUMBER = ${BUILD_NUMBER}" > ${WORKSPACE}/site/${JOB_NAME}/BUILD_NUMBER.txt
	echo "BUILD_ID = ${BUILD_ID}" > ${WORKSPACE}/site/${JOB_NAME}/BUILD_ID.txt

	# unzip into workspace for publishing as unpacked site
	unzip -u -o -q -d ${WORKSPACE}/site/${JOB_NAME}/ $z

	# copy into workspace for access by bucky aggregator (same name every time)
	rsync -aq $z ${WORKSPACE}/site/${SNAPNAME}
fi
z=""

# if component zips exist, copy them too; first site.zip, then site_assembly.zip
for z in $(find ${WORKSPACE}/sources/*/site/target -type f -name "site*.zip" | sort -r); do 
	y=${z%%/site/target/*}; y=${y##*/}
	if [[ $y != "aggregate" ]]; then # prevent duplicate nested sites
		#echo "[$y] $z ..."
		# unzip into workspace for publishing as unpacked site
		mkdir -p ${WORKSPACE}/site/${JOB_NAME}/$y
		unzip -u -o -q -d ${WORKSPACE}/site/${JOB_NAME}/$y $z
		# copy into workspace for access by bucky aggregator (same name every time)
		rsync -aq $z ${WORKSPACE}/site/${JOB_NAME}/${y}${SUFFNAME}
	fi
done

# if zips exist produced & renamed by ant script, copy them too
if [[ ! -f ${WORKSPACE}/site/${SNAPNAME} ]]; then
	for z in $(find ${WORKSPACE} -maxdepth 5 -mindepth 3 -name "*Update*.zip"); do 
		#echo "$z ..."
		unzip -u -o -q -d ${WORKSPACE}/site/${JOB_NAME}/ $z
		rsync -aq $z ${WORKSPACE}/site/${SNAPNAME}
	done
fi

# get sources zip
if [[ -f ${WORKSPACE}/sources/build/sources/target/sources.zip ]]; then
	rsync -aq ${WORKSPACE}/sources/build/sources/target/sources.zip ${WORKSPACE}/site/${JOB_NAME}/${SRCSNAME}
else
	# create sources zip
	pushd ${WORKSPACE}/sources
	zip ${WORKSPACE}/site/{JOB_NAME}/${SRCSNAME} -q -r * -x documentation\* -x download.jboss.org\* -x requirements\* \
	  -x workingset\* -x labs\* -x build\* -x \*test\* -x \*target\* -x \*.class -x \*.svn\* -x \*classes\* -x \*bin\* -x \*.zip \
	  -x \*docs\* -x \*reference\* -x \*releng\*
	popd
fi

# generate HTML snippet for inclusion on jboss.org
if [[ ${RELEASE} == "Yes" ]]; then
	ant -f ${WORKSPACE}/build/results/build.xml "-DZIPSUFFIX=${ZIPSUFFIX} -DJOB_NAME=${JOB_NAME}"
fi

# get full build log and filter out Maven test failures
bl=${WORKSPACE}/site/${JOB_NAME}/BUILDLOG.txt
wget -q http://hudson.qa.jboss.com/hudson/job/${JOB_NAME}/${BUILD_NUMBER}/consoleText -O ${bl}
fl=${WORKSPACE}/site/${JOB_NAME}/FAIL_LOG.txt
sed -ne "/<<< FAI/,+9 p" ${bl} | sed -e "/AILURE/,+9 s/\(.\+AILURE.\+\)/\n----------\n\n\1/g" > ${fl}
sed -ne "/ FAI/ p" ${bl} | sed -e "/AILURE \[/ s/\(.\+AILURE \[.\+\)/\n----------\n\n\1/g" >> ${fl}
sed -ne "/ SKI/ p" ${bl} | sed -e "/KIPPED \[/ s/\(.\+KIPPED \[.\+\)/\n----------\n\n\1/g" >> ${fl}
fc=$(sed -ne "/FAI\|LURE/ p" ${fl} | wc -l)
if [[ $fc != "0" ]]; then
	echo "" >> ${fl}; echo -n "FAI" >> ${fl}; echo -n "LURES FOUND: "$fc >> ${fl};
fi 
fc=$(sed -ne "/KIPPED/ p" ${fl} | wc -l)
if [[ $fc != "0" ]]; then
	echo "" >> ${fl}; echo -n "SKI" >> ${fl}; echo -n "PS FOUND: "$fc >> ${fl};
fi 
el=${WORKSPACE}/site/${JOB_NAME}/ERRORLOG.txt
sed -ne "/<<< ERR/,+9 p" ${bl} | sed -e "/RROR/,+9 s/\(.\+RROR.\+\)/\n----------\n\n\1/g" > ${el}
sed -ne "/\[ERR/,+2 p" ${bl} | sed -e "/ROR\] Fai/,+2 s/\(.\+ROR\] Fai.\+\)/\n----------\n\n\1/g" >> ${el}
ec=$(sed -ne "/ERR\|RROR/ p" ${el} | wc -l) 
if [[ $ec != "0" ]]; then
	echo "" >> ${el}; echo -n "ERR" >> ${el}; echo "ORS FOUND: "$ec >> ${el};
fi

date
rsync -arzq ${WORKSPACE}/site/${JOB_NAME}/*LOG.txt $DESTINATION/${JOB_NAME}/
date

# publish to download.jboss.org, unless errors found - avoid destroying last-good update site
if [[ $ec == "0" ]] && [[ $fc == "0" ]]; then
date
	# publish update site dir
	if [[ -d ${WORKSPACE}/site/${JOB_NAME} ]]; then
		rsync -arzq --delete ${WORKSPACE}/site/${JOB_NAME} $DESTINATION/
	fi
	# publish update site zip
	if [[ -f ${WORKSPACE}/site/${SNAPNAME} ]]; then
		rsync -arzq --delete ${WORKSPACE}/site/${SNAPNAME} $DESTINATION/
	fi
fi
date
