#!/bin/bash
# Hudson script used to publish Tycho-built p2 update sites
# NOTE: sources MUST be checked out into ${WORKSPACE}/sources 

# to use timestamp when naming dirs instead of ${BUILD_ID}-H${BUILD_NUMBER}, use:
# BUILD_ID=2010-08-31_19-16-10; timestamp=$(echo $BUILD_ID | tr -d "_-"); timestamp=${timestamp:0:12}; echo $timestamp; # 201008311916

# where to create the stuff to publish
STAGINGDIR=${WORKSPACE}/results/${JOB_NAME}

# https://jira.jboss.org/browse/JBIDE-6956 "jbosstools-3.2.0.M2" is too verbose, use "3.2.0.M2" instead
JOBNAMEREDUX=${JOB_NAME/.aggregate}; JOBNAMEREDUX=${JOBNAMEREDUX/jbosstools-}

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

# for JBDS, use DESTINATION=/qa/services/http/binaries/RHDS
if [[ $DESTINATION == "" ]]; then DESTINATION="tools@filemgmt.jboss.org:/downloads_htdocs/tools"; fi

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

# note the job name, build number, SVN rev, and build ID of the latest snapshot zip
mkdir -p ${STAGINGDIR}/logs
bl=${STAGINGDIR}/logs/BUILDLOG.txt
wget -q http://hudson.qa.jboss.com/hudson/job/${JOB_NAME}/${BUILD_NUMBER}/consoleText -O ${bl}

# JBDS-1361 - instead of SVN_REVISION, scrape ${bl} (BUILDLOG.txt)
# Updating http://anonsvn.jboss.org/repos/tdesigner/branches/7.1
#  ...
# At revision 1063
# Updating http://anonsvn.jboss.org/repos/jbosstools/trunk/build
#  ...
# At revision 25503
#  -- or -- 
# Checking out https://svn.jboss.org/repos/jbosstools/branches/jbosstools-3.2.0.Beta1
#  ...
# At revision 25538
rl=${STAGINGDIR}/logs/SVN_REVISION.txt

# convert input above to:
# http://anonsvn.jboss.org/repos/tdesigner/branches/7.1@1063
# http://anonsvn.jboss.org/repos/jbosstools/trunk/build@25503
# https://svn.jboss.org/repos/jbosstools/branches/jbosstools-3.2.0.Beta1@25538
sed -ne "/Updating \(http.\+\)\|Checking out \(http.\+\)\|At revision \([0-9]\+\)/ p" ${bl} | \
   sed -e "/At revision/ s/At revision /\@/" | sed -e N -e '/http/ s/\n//' | \
   sed -e "/Checking out\|Updating/,+1 s/\(Checking out \|Updating \)\(.\+\)/\2/g" > ${rl}

METAFILE="${BUILD_ID}-H${BUILD_NUMBER}.txt"
if [[ ${SVN_REVISION} ]]; then
	METAFILE="${BUILD_ID}-H${BUILD_NUMBER}-r${SVN_REVISION}.txt"
	echo "SVN_REVISION = ${SVN_REVISION}" > ${STAGINGDIR}/logs/${METAFILE}
else
	echo -n "" > ${STAGINGDIR}/logs/${METAFILE}
fi
echo "JOB_NAME = ${JOB_NAME}" >> ${STAGINGDIR}/logs/${METAFILE}
echo "BUILD_NUMBER = ${BUILD_NUMBER}" >> ${STAGINGDIR}/logs/${METAFILE}
echo "BUILD_ID = ${BUILD_ID}" >> ${STAGINGDIR}/logs/${METAFILE}
echo "WORKSPACE = ${WORKSPACE}" >> ${STAGINGDIR}/logs/${METAFILE}
echo "HUDSON_SLAVE = $(uname -a)" >> ${STAGINGDIR}/logs/${METAFILE}

#echo "$z ..."
if [[ $z != "" ]] && [[ -f $z ]] ; then
	# unzip into workspace for publishing as unpacked site
	mkdir -p ${STAGINGDIR}/all/repo
	unzip -u -o -q -d ${STAGINGDIR}/all/repo $z

	# generate MD5 sum for zip (file contains only the hash, not the hash + filename)
        for m in $(md5sum ${z}); do if [[ $m != ${z} ]]; then echo $m > ${z}.MD5; fi; done

	# copy into workspace for access by bucky aggregator (same name every time)
	rsync -aq $z ${z}.MD5 ${STAGINGDIR}/all/${SNAPNAME}
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

		# generate MD5 sum for zip (file contains only the hash, not the hash + filename)
	        for m in $(md5sum ${z}); do if [[ $m != ${z} ]]; then echo $m > ${z}.MD5; fi; done
        
		rsync -aq $z ${z}.MD5 ${STAGINGDIR}/${y}${SUFFNAME}
	fi
done

# if zips exist produced & renamed by ant script, copy them too
if [[ ! -f ${STAGINGDIR}/all/${SNAPNAME} ]]; then
	for z in $(find ${WORKSPACE} -maxdepth 5 -mindepth 3 -name "*Update*.zip" | sort | tail -1); do 
		#echo "$z ..."
		mkdir -p ${STAGINGDIR}/all
		unzip -u -o -q -d ${STAGINGDIR}/all/ $z

                # generate MD5 sum for zip (file contains only the hash, not the hash + filename)
                for m in $(md5sum ${z}); do if [[ $m != ${z} ]]; then echo $m > ${z}.MD5; fi; done

		rsync -aq $z ${z}.MD5 ${STAGINGDIR}/all/${SNAPNAME}
	done
fi

# create sources zip
pushd ${WORKSPACE}/sources
mkdir -p ${STAGINGDIR}/all
zip ${STAGINGDIR}/all/${SRCSNAME} -q -r * -x documentation\* -x download.jboss.org\* -x requirements\* \
  -x workingset\* -x labs\* -x build\* -x \*test\* -x \*target\* -x \*.class -x \*.svn\* -x \*classes\* -x \*bin\* -x \*.zip \
  -x \*docs\* -x \*reference\* -x \*releng\*
popd

# collect component zips from upstream aggregated build jobs
if [[ ${JOB_NAME/.aggregate} != ${JOB_NAME} ]] && [[ -d ${WORKSPACE}/sources/aggregate/site/zips ]]; then
	mkdir -p ${STAGINGDIR}/components
	for z in $(find ${WORKSPACE}/sources/aggregate/site/zips -name "*Update*.zip") $(find ${WORKSPACE}/sources/aggregate/site/zips -name "*Sources*.zip"); do
		# generate MD5 sum for zip (file contains only the hash, not the hash + filename)
                for m in $(md5sum ${z}); do if [[ $m != ${z} ]]; then echo $m > ${z}.MD5; fi; done

		mv $z ${z}.MD5 ${STAGINGDIR}/components
	done
fi

# generate list of zips in this job
METAFILE=zip.list.txt
mkdir -p ${STAGINGDIR}/logs
echo "ALL_ZIPS = \\" >> ${STAGINGDIR}/logs/${METAFILE}
for z in $(find ${STAGINGDIR} -name "*Update*.zip") $(find ${STAGINGDIR} -name "*Sources*.zip"); do
	# list zips in staging dir
	echo "${z##${STAGINGDIR}/},\\"  >> ${STAGINGDIR}/logs/${METAFILE}
done
echo ""  >> ${STAGINGDIR}/logs/${METAFILE}

# generate HTML snippet, download-snippet.html, for inclusion on jboss.org
#if [[ ${RELEASE} == "Yes" ]]; then
	mkdir -p ${STAGINGDIR}/logs
	ANT_PARAMS=" -DZIPSUFFIX=${ZIPSUFFIX} -DJOB_NAME=${JOB_NAME} -Dinput.dir=${STAGINGDIR} -Doutput.dir=${STAGINGDIR}/logs -DWORKSPACE=${WORKSPACE}"
	if [[ ${JOB_NAME/.aggregate} != ${JOB_NAME} ]]; then # reuse snippet from upstream build
		ANT_PARAMS="${ANT_PARAMS} -Dtemplate.file=http://download.jboss.org/jbosstools/builds/staging/${JOB_NAME/.aggregate/.continuous}/logs/download-snippet.html"
	fi
	for buildxml in ${WORKSPACE}/build/results/build.xml ${WORKSPACE}/sources/build/results/build.xml ${WORKSPACE}/sources/results/build.xml; do
		if [[ -f ${buildxml} ]]; then
			ANT_SCRIPT=${buildxml}
		fi
	done
	ant -f ${ANT_SCRIPT} ${ANT_PARAMS}
#fi

# ${bl} is full build log; see above
mkdir -p ${STAGINGDIR}/logs
# filter out Maven test failures
fl=${STAGINGDIR}/logs/FAIL_LOG.txt
# ignore warning lines and checksum failures
sed -ne "/\[WARNING\]\|CHECKSUM FAILED/ ! p" ${bl} | sed -ne "/<<< FAI/,+9 p" | sed -e "/AILURE/,+9 s/\(.\+AILURE.\+\)/\n----------\n\n\1/g" > ${fl}
sed -ne "/\[WARNING\]\|CHECKSUM FAILED/ ! p" ${bl} | sed -ne "/ FAI/ p" | sed -e "/AILURE \[/ s/\(.\+AILURE \[.\+\)/\n----------\n\n\1/g" >> ${fl}
sed -ne "/\[WARNING\]\|CHECKSUM FAILED/ ! p" ${bl} | sed -ne "/ SKI/ p" | sed -e "/KIPPED \[/ s/\(.\+KIPPED \[.\+\)/\n----------\n\n\1/g" >> ${fl}
fc=$(sed -ne "/FAI\|LURE/ p" ${fl} | wc -l)
if [[ $fc != "0" ]]; then
	echo "" >> ${fl}; echo -n "FAI" >> ${fl}; echo -n "LURES FOUND: "$fc >> ${fl};
fi 
fc=$(sed -ne "/KIPPED/ p" ${fl} | wc -l)
if [[ $fc != "0" ]]; then
	echo "" >> ${fl}; echo -n "SKI" >> ${fl}; echo -n "PS FOUND: "$fc >> ${fl};
fi 
el=${STAGINGDIR}/logs/ERRORLOG.txt
# ignore warning lines and checksum failures
sed -ne "/\[WARNING\]\|CHECKSUM FAILED/ ! p" ${bl} | sed -ne "/<<< ERR/,+9 p" | sed -e "/RROR/,+9 s/\(.\+RROR.\+\)/\n----------\n\n\1/g" > ${el}
sed -ne "/\[WARNING\]\|CHECKSUM FAILED/ ! p" ${bl} | sed -ne "/\[ERR/,+2 p"   | sed -e "/ROR\] Fai/,+2 s/\(.\+ROR\] Fai.\+\)/\n----------\n\n\1/g" >> ${el}
ec=$(sed -ne "/ERR\|RROR/ p" ${el} | wc -l) 
if [[ $ec != "0" ]]; then
	echo "" >> ${el}; echo -n "ERR" >> ${el}; echo "ORS FOUND: "$ec >> ${el};
fi

# publish to download.jboss.org, unless errors found - avoid destroying last-good update site
if [[ $ec == "0" ]] && [[ $fc == "0" ]]; then
	# publish build dir (including update sites/zips/logs/metadata
	if [[ -d ${STAGINGDIR} ]]; then
		
		# if an aggregate build, put output elsewhere on disk
		if [[ ${JOB_NAME/.aggregate} != ${JOB_NAME} ]]; then
			echo "<meta http-equiv=\"refresh\" content=\"0;url=${BUILD_ID}-H${BUILD_NUMBER}/\">" > /tmp/latestBuild.html
			if [[ $1 == "trunk" ]]; then
				date; rsync -arzq --delete ${STAGINGDIR}/* $DESTINATION/builds/nightly/trunk/${BUILD_ID}-H${BUILD_NUMBER}/
				date; rsync -arzq --delete /tmp/latestBuild.html $DESTINATION/builds/nightly/trunk/
			else
				date; rsync -arzq --delete /tmp/latestBuild.html $DESTINATION/builds/nightly/${JOBNAMEREDUX}/ 
				date; rsync -arzq --delete ${STAGINGDIR}/* $DESTINATION/builds/nightly/${JOBNAMEREDUX}/${BUILD_ID}-H${BUILD_NUMBER}/
			fi
			rm -f /tmp/latestBuild.html
		#else
			# COMMENTED OUT as this uses too much disk space
			# if a release build, create a named dir
			#if [[ ${RELEASE} == "Yes" ]]; then
			#	date; rsync -arzq --delete ${STAGINGDIR}/* $DESTINATION/builds/staging/${JOB_NAME}-${ZIPSUFFIX}/
			#fi
		fi

		# and create/replace a snapshot dir w/ static URL
		date; rsync -arzq --delete ${STAGINGDIR} $DESTINATION/builds/staging/
	fi

	# extra publish step for aggregate update sites ONLY
	if [[ ${JOB_NAME/.aggregate} != ${JOB_NAME} ]]; then
		if [[ $1 == "trunk" ]]; then 
			date; rsync -arzq --delete ${STAGINGDIR}/all/repo/* $DESTINATION/updates/nightly/trunk/
		else
			date; rsync -arzq --delete ${STAGINGDIR}/all/repo/* $DESTINATION/updates/nightly/${JOBNAMEREDUX}/
		fi
	fi
fi
date

# generate md5sums in a single file 
if [[ ${RELEASE} == "Yes" ]]; then
	md5sumsFile=${STAGINGDIR}/all/${JOB_NAME}-md5sums-${BUILD_ID}-H${BUILD_NUMBER}.txt
	echo "# Update Site Zips" > ${md5sumsFile}
	echo "# ----------------" >> ${md5sumsFile}
	md5sum $(find . -name "*Update*.zip" | egrep -v "aggregate-Sources|nightly-Update") >> ${md5sumsFile}
	echo "  " >> ${md5sumsFile}
	echo "# Source Zips" >> ${md5sumsFile}
	echo "# -----------" >> ${md5sumsFile}
	md5sum $(find . -name "*Source*.zip" | egrep -v "aggregate-Sources|nightly-Update") >> ${md5sumsFile}
	echo " " >> ${md5sumsFile}
fi

# purge org.jboss.tools metadata from local m2 repo (assumes job is configured with -Dmaven.repo.local=${WORKSPACE}/m2-repo)
if [[ -d ${WORKSPACE}/m2-repo/org/jboss/tools ]]; then
	rm -rf ${WORKSPACE}/m2-repo/org/jboss/tools
fi
