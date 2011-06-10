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

# internal destination mirror, for file:// access (instead of http://)
if [[ $INTRNALDEST == "" ]]; then INTRNALDEST="/home/hudson/static_build_env/jbds/"; fi

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
rm -f ${bl}; wget -q http://hudson.qa.jboss.com/hudson/job/${JOB_NAME}/${BUILD_NUMBER}/consoleText -O ${bl} --timeout=900 --wait=10 --random-wait --tries=10 --retry-connrefused --no-check-certificate

# JBDS-1361 - fetch XML and then sed it into plain text
rl=${STAGINGDIR}/logs/SVN_REVISION
rm -f ${rl}.txt ${rl}.xml; wget -O ${rl}.xml "http://hudson.qa.jboss.com/hudson/job/${JOB_NAME}/api/xml?wrapper=changeSet&depth=1&xpath=//build[1]/changeSet/revision" --timeout=900 --wait=10 --random-wait --tries=30 --retry-connrefused --no-check-certificate --server-response
if [[ $? -gt 0 ]]; then
	rm -f ${rl}.txt ${rl}.xml; wget -O ${rl}.xml "http://hudson.qa.jboss.com/hudson/job/${JOB_NAME}/config.xml" --timeout=900 --wait=10 --random-wait --tries=30 --retry-connrefused --no-check-certificate --server-response
	if [[ $(cat ${rl}.xml | grep "git") ]]; then
		echo "GIT Sources" > ${rl}.txt
		rm -f ${rl}.txt ${rl}.xml
		# Now, track git source revision through hudson api: /job/${JOB_NAME}/${BUILD_NUMBER}/api/xml?xpath=//lastBuiltRevision
		rl=${STAGINGDIR}/logs/GIT_REVISION
		wget -O ${rl}.xml "http://hudson.qa.jboss.com/hudson/job/${JOB_NAME}/${BUILD_NUMBER}/api/xml?xpath=//lastBuiltRevision" --timeout=900 --wait=10 --random-wait --tries=30 --retry-connrefused --no-check-certificate --server-response
		sed -e "s#<lastBuiltRevision><SHA1>\([a-f0-9]\+\)</SHA1><branch><SHA1>\([a-f0-9]\+\)</SHA1><name>\([^<>]\+\)</name></branch></lastBuiltRevision>#\3\@\1#g" ${rl}.xml | sed -e "s#<[^<>]\+>##g" > ${rl}.txt
	else
		echo "UNKNOWN" > ${rl}.txt
	fi
else
	sed -e "s#<module>\(http[^<>]\+\)</module><revision>\([0-9]\+\)</revision>#\1\@\2\n#g" ${rl}.xml | sed -e "s#<[^<>]\+>##g" > ${rl}.txt 
fi

METAFILE="${BUILD_ID}-H${BUILD_NUMBER}.txt"
touch ${STAGINGDIR}/logs/${METAFILE}
METAFILE=build.properties

echo "JOB_NAME = ${JOB_NAME}" >> ${STAGINGDIR}/logs/${METAFILE}
echo "BUILD_NUMBER = ${BUILD_NUMBER}" >> ${STAGINGDIR}/logs/${METAFILE}
echo "BUILD_ID = ${BUILD_ID}" >> ${STAGINGDIR}/logs/${METAFILE}
echo "WORKSPACE = ${WORKSPACE}" >> ${STAGINGDIR}/logs/${METAFILE}
echo "HUDSON_SLAVE = $(uname -a)" >> ${STAGINGDIR}/logs/${METAFILE}
echo "RELEASE = ${RELEASE}" >> ${STAGINGDIR}/logs/${METAFILE}
echo "ZIPSUFFIX = ${ZIPSUFFIX}" >> ${STAGINGDIR}/logs/${METAFILE}

#echo "$z ..."
if [[ $z != "" ]] && [[ -f $z ]] ; then
	# unzip into workspace for publishing as unpacked site
	mkdir -p ${STAGINGDIR}/all/repo
	unzip -u -o -q -d ${STAGINGDIR}/all/repo $z

	# generate MD5 sum for zip (file contains only the hash, not the hash + filename)
        for m in $(md5sum ${z}); do if [[ $m != ${z} ]]; then echo $m > ${z}.MD5; fi; done

	# copy into workspace for access by bucky aggregator (same name every time)
	rsync -aq $z ${STAGINGDIR}/all/${SNAPNAME}
	rsync -aq ${z}.MD5 ${STAGINGDIR}/all/${SNAPNAME}.MD5
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
        
		rsync -aq $z ${STAGINGDIR}/${y}${SUFFNAME}
		rsync -aq ${z}.MD5 ${STAGINGDIR}/${y}${SUFFNAME}.MD5
	fi
done

# if zips exist produced & renamed by ant script, copy them too
if [[ ! -f ${STAGINGDIR}/all/${SNAPNAME} ]]; then
	for z in $(find ${WORKSPACE} -maxdepth 5 -mindepth 3 -name "*Update*.zip" | sort | tail -1); do 
		#echo "$z ..."
		if [[ -f $z ]]; then
			mkdir -p ${STAGINGDIR}/all
			unzip -u -o -q -d ${STAGINGDIR}/all/ $z

                	# generate MD5 sum for zip (file contains only the hash, not the hash + filename)
	                for m in $(md5sum ${z}); do if [[ $m != ${z} ]]; then echo $m > ${z}.MD5; fi; done

			rsync -aq $z ${STAGINGDIR}/all/${SNAPNAME}
			rsync -aq ${z}.MD5 ${STAGINGDIR}/all/${SNAPNAME}.MD5
		fi
	done
fi

# create sources zip
pushd ${WORKSPACE}/sources
mkdir -p ${STAGINGDIR}/all
if [[ ${JOB_NAME/.aggregate} != ${JOB_NAME} ]] && [[ -d ${WORKSPACE}/sources/aggregate/site/zips ]]; then
	srczipname=${SRCSNAME/-Sources-/-Additional-Sources-}
else
	srczipname=${SRCSNAME}
fi
zip ${STAGINGDIR}/all/${srczipname} -q -r * -x hudson_workspace\* -x documentation\* -x download.jboss.org\* -x requirements\* \
  -x workingset\* -x labs\* -x build\* -x \*test\* -x \*target\* -x \*.class -x \*.svn\* -x \*classes\* -x \*bin\* -x \*.zip \
  -x \*docs\* -x \*reference\* -x \*releng\* -x \*.git\* -x \*/lib/\*.jar
popd
z=${STAGINGDIR}/all/${srczipname}; for m in $(md5sum ${z}); do if [[ $m != ${z} ]]; then echo $m > ${z}.MD5; fi; done

mkdir -p ${STAGINGDIR}/logs

# collect component zips from upstream aggregated build jobs
if [[ ${JOB_NAME/.aggregate} != ${JOB_NAME} ]] && [[ -d ${WORKSPACE}/sources/aggregate/site/zips ]]; then
	mkdir -p ${STAGINGDIR}/components
	for z in $(find ${WORKSPACE}/sources/aggregate/site/zips -name "*Update*.zip"); do
		# generate MD5 sum for zip (file contains only the hash, not the hash + filename)
		for m in $(md5sum ${z}); do if [[ $m != ${z} ]]; then echo $m > ${z}.MD5; fi; done
		mv $z ${z}.MD5 ${STAGINGDIR}/components
	done

	mkdir -p ${STAGINGDIR}/all/sources	
	# unpack component source zips like jbosstools-pi4soa-3.1_trunk-Sources-SNAPSHOT.zip or jbosstools-3.2_trunk.component--ws-Sources-SNAPSHOT.zip
	for z in $(find ${WORKSPACE}/sources/aggregate/site/zips -name "*Sources*.zip"); do
		zn=${z%*-Sources*.zip}; zn=${zn#*--}; zn=${zn##*/}; zn=${zn#jbosstools-}; 
		# zn=${zn%_trunk}; zn=${zn%_stable_branch};
		mkdir -p ${STAGINGDIR}/all/sources/${zn}/
		unzip -qq -o -d ${STAGINGDIR}/all/sources/${zn}/ $z
	done
	# add component sources into sources zip
	pushd ${STAGINGDIR}/all/sources
	zip ${STAGINGDIR}/all/${SRCSNAME} -q -r * -x hudson_workspace\* -x documentation\* -x download.jboss.org\* -x requirements\* \
	  -x workingset\* -x labs\* -x build\* -x \*test\* -x \*target\* -x \*.class -x \*.svn\* -x \*classes\* -x \*bin\* -x \*.zip \
	  -x \*docs\* -x \*reference\* -x \*releng\* -x \*.git\* -x \*/lib/\*.jar
	popd
	rm -fr ${STAGINGDIR}/all/sources
	
	z=${STAGINGDIR}/all/${SRCSNAME}; for m in $(md5sum ${z}); do if [[ $m != ${z} ]]; then echo $m > ${z}.MD5; fi; done

	# JBIDE-7444 get aggregate metadata xml properties file
	if [[ -f ${WORKSPACE}/sources/aggregate/site/zips/build.properties.all.xml ]]; then
		rsync -aq ${WORKSPACE}/sources/aggregate/site/zips/build.properties.all.xml ${STAGINGDIR}/logs/
	fi
fi

# generate list of zips in this job
METAFILE=zip.list.txt
echo "ALL_ZIPS = \\" >> ${STAGINGDIR}/logs/${METAFILE}
for z in $(find ${STAGINGDIR} -name "*Update*.zip") $(find ${STAGINGDIR} -name "*Sources*.zip"); do
	# list zips in staging dir
	echo "${z##${STAGINGDIR}/},\\"  >> ${STAGINGDIR}/logs/${METAFILE}
done
echo ""  >> ${STAGINGDIR}/logs/${METAFILE}

# generate md5sums in a single file 
md5sumsFile=${STAGINGDIR}/logs/md5sums.txt
echo "# Update Site Zips" > ${md5sumsFile}
echo "# ----------------" >> ${md5sumsFile}
md5sum $(find . -name "*Update*.zip" | egrep -v "aggregate-Sources|nightly-Update") >> ${md5sumsFile}
echo "  " >> ${md5sumsFile}
echo "# Source Zips" >> ${md5sumsFile}
echo "# -----------" >> ${md5sumsFile}
md5sum $(find . -name "*Source*.zip" | egrep -v "aggregate-Sources|nightly-Update") >> ${md5sumsFile}
echo " " >> ${md5sumsFile}

mkdir -p ${STAGINGDIR}/logs
ANT_PARAMS=" -DZIPSUFFIX=${ZIPSUFFIX} -DJOB_NAME=${JOB_NAME} -Dinput.dir=${STAGINGDIR} -Doutput.dir=${STAGINGDIR}/logs -DWORKSPACE=${WORKSPACE}"
for buildxml in ${WORKSPACE}/build/results/build.xml ${WORKSPACE}/sources/build/results/build.xml ${WORKSPACE}/sources/results/build.xml; do
	if [[ -f ${buildxml} ]]; then
		ANT_SCRIPT=${buildxml}
		RESULTS_DIR=${buildxml/\/build.xml/}
	fi
done
if [[ ${ANT_SCRIPT} ]] && [[ -f ${ANT_SCRIPT} ]]; then ant -f ${ANT_SCRIPT} ${ANT_PARAMS}; fi

# copy buildResults.css, buildResults.html to ${STAGINGDIR}/logs
if [[ ${RESULTS_DIR} ]] && [[ -d ${RESULTS_DIR} ]]; then
	for f in buildResults.html buildResults.css; do
		if [[ -f ${RESULTS_DIR}/${f} ]]; then rsync -arzq ${RESULTS_DIR}/${f} ${STAGINGDIR}/logs/; fi
	done
fi

# purge duplicate zip files in logs/zips/all/*.zip
if [[ -d ${STAGINGDIR}/logs/zips ]]; then rm -f $(find ${STAGINGDIR}/logs/zips -type f -name "*.zip"); fi

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

		# and create/replace a snapshot dir outside Hudson which is file:// accessible
		date; rsync -arzq --delete ${STAGINGDIR} $INTRNALDEST/builds/staging/

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

# purge org.jboss.tools metadata from local m2 repo (assumes job is configured with -Dmaven.repo.local=${WORKSPACE}/m2-repo)
if [[ -d ${WORKSPACE}/m2-repo/org/jboss/tools ]]; then
	rm -rf ${WORKSPACE}/m2-repo/org/jboss/tools
fi

# publish updated log
bl=${STAGINGDIR}/logs/BUILDLOG.txt
rm -f ${bl}; wget -q http://hudson.qa.jboss.com/hudson/job/${JOB_NAME}/${BUILD_NUMBER}/consoleText -O ${bl} --timeout=900 --wait=10 --random-wait --tries=10 --retry-connrefused --no-check-certificate
date; rsync -arzq --delete ${STAGINGDIR}/logs $DESTINATION/builds/staging/${JOB_NAME}/
date; rsync -arzq --delete ${STAGINGDIR}/logs $INTRNALDEST/builds/staging/${JOB_NAME}/

