#!/bin/bash
# Hudson script used to publish Tycho-built p2 update sites
# NOTE: sources MUST be checked out into ${WORKSPACE}/sources 

# where to create the stuff to publish
STAGINGDIR=${WORKSPACE}/results/${JOB_NAME}

# releases get named differently than snapshots
if [[ ${RELEASE} == "Yes" ]]; then
	ZIPSUFFIX="${BUILD_ID}-H${BUILD_NUMBER}"
	STAGINGDIR=${WORKSPACE}/results/${JOB_NAME}-${ZIPSUFFIX}
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
rm -fr ${WORKSPACE}/results; mkdir -p ${STAGINGDIR}

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
	echo "JOB_NAME = ${JOB_NAME}" > ${STAGINGDIR}/JOB_NAME.txt
	echo "BUILD_NUMBER = ${BUILD_NUMBER}" > ${STAGINGDIR}/BUILD_NUMBER.txt
	echo "BUILD_ID = ${BUILD_ID}" > ${STAGINGDIR}/BUILD_ID.txt

	# unzip into workspace for publishing as unpacked site
	mkdir -p ${STAGINGDIR}/all/repo
	unzip -u -o -q -d ${STAGINGDIR}/all/repo $z

	# copy into workspace for access by bucky aggregator (same name every time)
	rsync -aq $z ${STAGINGDIR}/all/${SNAPNAME}
fi
z=""

# if component zips exist, copy them too; first site.zip, then site_assembly.zip
for z in $(find ${WORKSPACE}/sources/*/site/target -type f -name "site*.zip" | sort -r); do 
	y=${z%%/site/target/*}; y=${y##*/}
	if [[ $y != "aggregate" ]]; then # prevent duplicate nested sites
		#echo "[$y] $z ..."
		# unzip into workspace for publishing as unpacked site
		mkdir -p ${STAGINGDIR}/$y
		unzip -u -o -q -d ${STAGINGDIR}/$y $z
		# copy into workspace for access by bucky aggregator (same name every time)
		rsync -aq $z ${STAGINGDIR}/${y}${SUFFNAME}
	fi
done

# if zips exist produced & renamed by ant script, copy them too
if [[ ! -f ${STAGINGDIR}/all/${SNAPNAME} ]]; then
	for z in $(find ${WORKSPACE} -maxdepth 5 -mindepth 3 -name "*Update*.zip" | sort | tail -1); do 
		#echo "$z ..."
		mkdir -p ${STAGINGDIR}/all
		unzip -u -o -q -d ${STAGINGDIR}/all/ $z
		rsync -aq $z ${STAGINGDIR}/all/${SNAPNAME}
	done
fi

# create sources zip
pushd ${WORKSPACE}/sources
mkdir -p ${STAGINGDIR}/all
zip ${STAGINGDIR}/all/${SRCSNAME} -q -r * -x documentation\* -x download.jboss.org\* -x requirements\* \
  -x workingset\* -x labs\* -x build\* -x \*test\* -x \*target\* -x \*.class -x \*.svn\* -x \*classes\* -x \*bin\* -x \*.zip \
  -x \*docs\* -x \*reference\* -x \*releng\*
popd

# generate HTML snippet for inclusion on jboss.org
if [[ ${RELEASE} == "Yes" ]]; then
	mkdir -p ${STAGINGDIR}
	ANT_PARAMS="-DZIPSUFFIX=${ZIPSUFFIX} -DJOB_NAME=${JOB_NAME} -Doutput.dir=${STAGINGDIR}"
	if [[ -f ${WORKSPACE}/build/results/build.xml ]]; then
		ant -f ${WORKSPACE}/build/results/build.xml ${ANT_PARAMS}
	elif [[ -f ${WORKSPACE}/sources/build/results/build.xml ]]; then
		ant -f ${WORKSPACE}/sources/build/results/build.xml ${ANT_PARAMS}
	fi
fi

# get full build log and filter out Maven test failures
bl=${STAGINGDIR}/BUILDLOG.txt
wget -q http://hudson.qa.jboss.com/hudson/job/${JOB_NAME}/${BUILD_NUMBER}/consoleText -O ${bl}
fl=${STAGINGDIR}/FAIL_LOG.txt
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
el=${STAGINGDIR}/ERRORLOG.txt
sed -ne "/<<< ERR/,+9 p" ${bl} | sed -e "/RROR/,+9 s/\(.\+RROR.\+\)/\n----------\n\n\1/g" > ${el}
sed -ne "/\[ERR/,+2 p" ${bl} | sed -e "/ROR\] Fai/,+2 s/\(.\+ROR\] Fai.\+\)/\n----------\n\n\1/g" >> ${el}
ec=$(sed -ne "/ERR\|RROR/ p" ${el} | wc -l) 
if [[ $ec != "0" ]]; then
	echo "" >> ${el}; echo -n "ERR" >> ${el}; echo "ORS FOUND: "$ec >> ${el};
fi

# publish to download.jboss.org, unless errors found - avoid destroying last-good update site
if [[ $ec == "0" ]] && [[ $fc == "0" ]]; then
date
	# publish update site dir
	if [[ -d ${STAGINGDIR} ]]; then
		rsync -arzq --delete ${STAGINGDIR} $DESTINATION/
	fi
	# publish update site zip
	if [[ -f ${WORKSPACE}/results/${SNAPNAME} ]]; then
		rsync -arzq --delete ${WORKSPACE}/results/${SNAPNAME} $DESTINATION/
	fi
fi

date
rsync -arzq ${STAGINGDIR}/*LOG.txt $DESTINATION/${JOB_NAME}/
date

