#!/bin/bash
# Hudson script used to publish Tycho-built p2 update sites
# NOTE: sources MUST be checked out into ${WORKSPACE}/sources 

# to use timestamp when naming dirs instead of ${BUILD_ID}-H${BUILD_NUMBER}, use:
# BUILD_ID=2010-08-31_19-16-10; timestamp=$(echo $BUILD_ID | tr -d "_-"); timestamp=${timestamp:0:12}; echo $timestamp; # 201008311916

#set up tmpdir
tmpdir=`mktemp -d`
mkdir -p $tmpdir

# where to create the stuff to publish
STAGINGDIR=${WORKSPACE}/results/${JOB_NAME}

# for trunk, use "trunk" or "trunk/soa" instead of generated path from job name
PUBLISHPATHSUFFIX=""; if [[ $1 ]]; then PUBLISHPATHSUFFIX="$1"; fi

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
if [[ -d     ${WORKSPACE}/sources/aggregate/site/target ]]; then
	if [[ -f ${WORKSPACE}/sources/aggregate/site/target/site_assembly.zip ]]; then
	 siteZip=${WORKSPACE}/sources/aggregate/site/target/site_assembly.zip
	else
	 siteZip=${WORKSPACE}/sources/aggregate/site/target/repository.zip
	fi
	z=$siteZip
elif [[ -d   ${WORKSPACE}/sources/aggregate/site/site/target ]]; then
	if [[ -f ${WORKSPACE}/sources/aggregate/site/site/target/site_assembly.zip ]]; then
	 siteZip=${WORKSPACE}/sources/aggregate/site/site/target/site_assembly.zip
	else
	 siteZip=${WORKSPACE}/sources/aggregate/site/site/target/repository.zip
	fi
	z=$siteZip
elif [[ -d   ${WORKSPACE}/sources/site/target ]]; then
	if [[ -f ${WORKSPACE}/sources/site/target/site_assembly.zip ]]; then
	 siteZip=${WORKSPACE}/sources/site/target/site_assembly.zip
	else
	 siteZip=${WORKSPACE}/sources/site/target/repository.zip
	 # JBIDE-10923
	 pushd ${WORKSPACE}/sources/site/target/repository >/dev/null
	 zip -r $siteZip .
	 popd >/dev/null
	fi
	z=$siteZip
fi

# note the job name, build number, SVN rev, and build ID of the latest snapshot zip
mkdir -p ${STAGINGDIR}/logs
bl=${STAGINGDIR}/logs/BUILDLOG.txt
rm -f ${bl}; wget -q http://hudson.qa.jboss.com/hudson/job/${JOB_NAME}/${BUILD_NUMBER}/consoleText -O ${bl} --timeout=900 --wait=10 --random-wait --tries=10 --retry-connrefused --no-check-certificate

# JBDS-1361 - fetch XML and then sed it into plain text
wgetParams="--timeout=900 --wait=10 --random-wait --tries=30 --retry-connrefused --no-check-certificate --server-response"
rl=${STAGINGDIR}/logs/REVISION
if [[ $(find ${WORKSPACE} -mindepth 2 -maxdepth 3 -name ".git") ]]; then
	# Track git source revision through hudson api: /job/${JOB_NAME}/${BUILD_NUMBER}/api/xml?xpath=(//lastBuiltRevision)[1]
	rl=${STAGINGDIR}/logs/GIT_REVISION
	rm -f ${rl}.txt ${rl}.xml; wget -O ${rl}.xml "http://hudson.qa.jboss.com/hudson/job/${JOB_NAME}/${BUILD_NUMBER}/api/xml?xpath=%28//lastBuiltRevision%29[1]" ${wgetParams}
	sed -e "s#<lastBuiltRevision><SHA1>\([a-f0-9]\+\)</SHA1><branch><SHA1>\([a-f0-9]\+\)</SHA1><name>\([^<>]\+\)</name></branch></lastBuiltRevision>#\3\@\1#g" ${rl}.xml | sed -e "s#<[^<>]\+>##g" > ${rl}.txt
elif [[ $(find ${WORKSPACE} -mindepth 2 -maxdepth 3 -name ".svn") ]]; then
	# Track svn source revision through hudson api: /job/${JOB_NAME}/api/xml?wrapper=changeSet&depth=1&xpath=//build[1]/changeSet/revision
	rl=${STAGINGDIR}/logs/SVN_REVISION
	rm -f ${rl}.txt ${rl}.xml; wget -O ${rl}.xml "http://hudson.qa.jboss.com/hudson/job/${JOB_NAME}/api/xml?wrapper=changeSet&depth=1&xpath=//build[1]/changeSet/revision" ${wgetParams}
	if [[ $? -eq 0 ]]; then
		sed -e "s#<module>\(http[^<>]\+\)</module><revision>\([0-9]\+\)</revision>#\1\@\2\n#g" ${rl}.xml | sed -e "s#<[^<>]\+>##g" > ${rl}.txt 
	else
		echo "UNKNOWN SVN REVISION(S)" > ${rl}.txt
	fi
else
	# not git or svn... unsupported
	echo "UNKNOWN REVISION(S)" > ${rl}.txt
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
y=${STAGINGDIR}/logs/${METAFILE}; for m in $(md5sum ${y}); do if [[ $m != ${y} ]]; then echo $m > ${y}.MD5; fi; done

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

# if component zips exist, copy repository.zip (or site_assembly.zip) too
for z in $(find ${WORKSPACE}/sources/*/site/target -type f -name "repository.zip" -o -name "site_assembly.zip"); do 
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

# if installer jars exist (should be 2 installers, 2 md5sums)
for z in $(find ${WORKSPACE}/sources/product/installer/target -type f -name "jbdevstudio-product*-universal*.jar*"); do 
	mkdir -p ${STAGINGDIR}/installer/
	rsync -aq $z ${STAGINGDIR}/installer/
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

foundSourcesZip=0
# for now, put the JBDS sources into the /installer/ folder
for z in $(find ${WORKSPACE}/sources/product/sources/target -type f -name "jbdevstudio-product-sources-*.zip"); do
	for m in $(md5sum ${z}); do if [[ $m != ${z} ]]; then echo $m > ${z}.MD5; fi; done
	mkdir -p ${STAGINGDIR}/installer/
	rsync -aq $z ${z}.MD5 ${STAGINGDIR}/installer/
	# [fix for DEPRECATED PDE installer build] provide symlink so that the .product build can find the sources zip using a generic name, where SRCSNAME = ${JOB_NAME}-Sources-${ZIPSUFFIX}.zip
	#mkdir -p ${STAGINGDIR}/all; pushd ${STAGINGDIR}/all >/dev/null
	#ln -s ../installer/${z##*/} ${SRCSNAME}
	#ln -s ../installer/${z##*/}.MD5 ${SRCSNAME}.MD5
	#popd >/dev/null
	foundSourcesZip=1
done
if [[ $foundSourcesZip -eq 0 ]]; then
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
fi

# JBDS-1992 create results page in installer/ folder, including update site zip, sources zip, and installers
if [[ -d ${WORKSPACE}/sources/product/results/target ]]; then
	mkdir -p ${STAGINGDIR}/installer/
	rsync -aq ${WORKSPACE}/sources/product/results/target/* ${STAGINGDIR}/installer/
fi

mkdir -p ${STAGINGDIR}/logs

# collect component zips from upstream aggregated build jobs
if [[ ${JOB_NAME/.aggregate} != ${JOB_NAME} ]] && [[ -d ${WORKSPACE}/sources/aggregate/site/zips ]]; then
	mkdir -p ${STAGINGDIR}/components
	for z in $(find ${WORKSPACE}/sources/aggregate/site/zips -name "*Update*.zip"); do
		# generate MD5 sum for zip (file contains only the hash, not the hash + filename)
		for m in $(md5sum ${z}); do if [[ $m != ${z} ]]; then echo $m > ${z}.MD5; fi; done
		mv $z ${z}.MD5 ${STAGINGDIR}/components
	done
	
	# TODO :: JBIDE-9870 When we have a -Update-Sources- zip, this can be removed
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

# JBIDE-9870 check if there's a sources update site and rename it if found (note, bottests-site/site/sources won't work; use bottests-site/souces)
for z in $(find ${WORKSPACE}/sources/aggregate/*/sources/target/ -name "repository.zip" -o -name "site_assembly.zip"); do
	echo "Collect sources from update site in $z"
	mv $z ${STAGINGDIR}/all/${SRCSNAME/-Sources-/-Update-Sources-}
	for m in $(md5sum ${z}); do if [[ $m != ${z} ]]; then echo $m > ${z}.MD5; fi; done 
done

# generate list of zips in this job
METAFILE=zip.list.txt
echo "ALL_ZIPS = \\" >> ${STAGINGDIR}/logs/${METAFILE}
for z in $(find ${STAGINGDIR} -name "*Update*.zip") $(find ${STAGINGDIR} -name "*Sources*.zip"); do
	# list zips in staging dir
	echo "${z##${STAGINGDIR}/},\\"  >> ${STAGINGDIR}/logs/${METAFILE}
done
echo ""  >> ${STAGINGDIR}/logs/${METAFILE}

# generate md5sums in a single file 
pushd ${STAGINGDIR} >/dev/null
md5sumsFile=${STAGINGDIR}/logs/md5sums.txt
echo "# Update Site Zips" > ${md5sumsFile}
echo "# ----------------" >> ${md5sumsFile}
md5sum $(find . -name "*Update*.zip" | egrep -v "aggregate-Sources|nightly-Update") >> ${md5sumsFile}
echo "  " >> ${md5sumsFile}
echo "# Source Zips" >> ${md5sumsFile}
echo "# -----------" >> ${md5sumsFile}
md5sum $(find . -iname "*source*.zip" | egrep -v "aggregate-Sources|nightly-Update") >> ${md5sumsFile}
echo " " >> ${md5sumsFile}
popd >/dev/null

mkdir -p ${STAGINGDIR}/logs

# generate results page for an aggregate build only
if [[ ${JOB_NAME/.aggregate} != ${JOB_NAME} ]] && [[ -f ${WORKSPACE}/sources/results/pom.xml ]] && [[ -f ${WORKSPACE}/sources/results/build.xml ]]; then
	pushd ${WORKSPACE}/sources/results >/dev/null
	export JAVA_HOME=$(find /qa/tools/opt -maxdepth 1 -mindepth 1 -type d -name "jdk1.6.0_*" | sort | tail -1)
	export M2_HOME=$(find /qa/tools/opt -maxdepth 1 -mindepth 1 -type d -name "apache-maven-3.0.*" | sort | tail -1)
	${M2_HOME}/bin/mvn -q -B install -DJOB_NAME=${JOB_NAME} -DBUILD_NUMBER=${BUILD_NUMBER} -DBUILD_ID=${BUILD_ID}
	mv target/index.html ${STAGINGDIR}/index.html; rm -fr target
	popd >/dev/null
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
			echo "<meta http-equiv=\"refresh\" content=\"0;url=${BUILD_ID}-H${BUILD_NUMBER}/\">" > $tmpdir/latestBuild.html
			if [[ ${PUBLISHPATHSUFFIX} ]]; then
				date
				# create folders if not already there
				if [[ ${DESTINATION##*@*:*} == "" ]]; then # user@server, do remote op
					seg="."; for d in ${PUBLISHPATHSUFFIX/\// }; do seg=$seg/$d; echo -e "mkdir ${seg:2}" | sftp $DESTINATION/builds/nightly/; done; seg=""
				else
					mkdir -p $DESTINATION/builds/nightly/${PUBLISHPATHSUFFIX}
				fi
				date; rsync -arzq --protocol=28 --delete ${STAGINGDIR}/* $DESTINATION/builds/nightly/${PUBLISHPATHSUFFIX}/${BUILD_ID}-H${BUILD_NUMBER}/
				# sftp only works with user@server, not with local $DESTINATIONS, so use rsync to push symlink instead
				# echo -e "rm latest\nln ${BUILD_ID}-H${BUILD_NUMBER} latest" | sftp ${DESTINATIONREDUX}/builds/nightly/${PUBLISHPATHSUFFIX}/ 
				pushd $tmpdir >/dev/null; ln -s ${BUILD_ID}-H${BUILD_NUMBER} latest; rsync --protocol=28 -l latest ${DESTINATION}/builds/nightly/${PUBLISHPATHSUFFIX}/; rm -f latest; popd >/dev/null
				date; rsync -arzq --protocol=28 --delete $tmpdir/latestBuild.html $DESTINATION/builds/nightly/${PUBLISHPATHSUFFIX}/
			else
				date; rsync -arzq --protocol=28 --delete $tmpdir/latestBuild.html $DESTINATION/builds/nightly/${JOBNAMEREDUX}/ 
				# sftp only works with user@server, not with local $DESTINATIONS, so use rsync to push symlink instead
				# echo -e "rm latest\nln ${BUILD_ID}-H${BUILD_NUMBER} latest" | sftp ${DESTINATIONREDUX}/builds/nightly/${JOBNAMEREDUX}/
				pushd $tmpdir >/dev/null; ln -s ${BUILD_ID}-H${BUILD_NUMBER} latest; rsync --protocol=28 -l latest ${DESTINATION}/builds/nightly/${JOBNAMEREDUX}/; rm -f latest; popd >/dev/null
				date; rsync -arzq --protocol=28 --delete ${STAGINGDIR}/* $DESTINATION/builds/nightly/${JOBNAMEREDUX}/${BUILD_ID}-H${BUILD_NUMBER}/
			fi
			rm -f $tmpdir/latestBuild.html
		#else
			# COMMENTED OUT as this uses too much disk space
			# if a release build, create a named dir
			#if [[ ${RELEASE} == "Yes" ]]; then
			#	date; rsync -arzq --protocol=28 --delete ${STAGINGDIR}/* $DESTINATION/builds/staging/${JOB_NAME}-${ZIPSUFFIX}/
			#fi
		fi

		# and create/replace a snapshot dir w/ static URL
		date; rsync -arzq --protocol=28 --delete ${STAGINGDIR}/* $DESTINATION/builds/staging/${JOB_NAME}.next

		# 1. To recursively purge contents of .../staging.previous/foobar/ folder: 
		#  mkdir -p $tmpdir/foobar; 
		#  rsync -aPrz --delete $tmpdir/foobar tools@filemgmt.jboss.org:/downloads_htdocs/tools/builds/staging.previous/ 
		# 2. To then remove entire .../staging.previous/foobar/ folder: 
		#  echo -e "rmdir foobar" | sftp tools@filemgmt.jboss.org:/downloads_htdocs/tools/builds/staging.previous/
		#  rmdir $tmpdir/foobar

		# JBIDE-8667 move current to previous; move next to current
		if [[ ${DESTINATION##*@*:*} == "" ]]; then # user@server, do remote op
			# create folders if not already there (could be empty)
			echo -e "mkdir ${JOB_NAME}" | sftp $DESTINATION/builds/staging/
			echo -e "mkdir ${JOB_NAME}" | sftp $DESTINATION/builds/staging.previous/
			#echo -e "mkdir ${JOB_NAME}.2" | sftp $DESTINATION/builds/staging.previous/

			# IF using .2 folders, purge contents of /builds/staging.previous/${JOB_NAME}.2 and remove empty dir
			# NOTE: comment out next section - should only purge one staging.previous/* folder
			#mkdir -p $tmpdir/${JOB_NAME}.2
			#rsync -arzq --delete --protocol=28 $tmpdir/${JOB_NAME}.2 $DESTINATION/builds/staging.previous/
			#echo -e "rmdir ${JOB_NAME}.2" | sftp $DESTINATION/builds/staging.previous/
			#rmdir $tmpdir/${JOB_NAME}.2

			# OR, purge contents of /builds/staging.previous/${JOB_NAME} and remove empty dir
			mkdir -p $tmpdir/${JOB_NAME}
			rsync -arzq --protocol=28 --delete $tmpdir/${JOB_NAME} $DESTINATION/builds/staging.previous/
			echo -e "rmdir ${JOB_NAME}" | sftp $DESTINATION/builds/staging.previous/
			rmdir $tmpdir/${JOB_NAME}

			# move contents of /builds/staging.previous/${JOB_NAME} into /builds/staging.previous/${JOB_NAME}.2
			#echo -e "rename ${JOB_NAME} ${JOB_NAME}.2" | sftp $DESTINATION/builds/staging.previous/

			# move contents of /builds/staging/${JOB_NAME} into /builds/staging.previous/${JOB_NAME}
			echo -e "rename ${JOB_NAME} ../staging.previous/${JOB_NAME}" | sftp $DESTINATION/builds/staging/

			# move contents of /builds/staging/${JOB_NAME}.next into /builds/staging/${JOB_NAME}
			echo -e "rename ${JOB_NAME}.next ${JOB_NAME}" | sftp $DESTINATION/builds/staging/
		else # work locally
			# create folders if not already there (could be empty)
			mkdir -p $DESTINATION/builds/staging/${JOB_NAME}
			mkdir -p $DESTINATION/builds/staging.previous/${JOB_NAME}
			#mkdir -p $DESTINATION/builds/staging.previous/${JOB_NAME}.2

			# purge contents of /builds/staging.previous/${JOB_NAME}.2 and remove empty dir
			# NOTE: comment out next section - should only purge one staging.previous/* folder
			#rm -fr $DESTINATION/builds/staging.previous/${JOB_NAME}.2/
			
			# OR, purge contents of /builds/staging.previous/${JOB_NAME} and remove empty dir
			rm -fr $DESTINATION/builds/staging.previous/${JOB_NAME}/

			# move contents of /builds/staging.previous/${JOB_NAME} into /builds/staging.previous/${JOB_NAME}.2
			#mv $DESTINATION/builds/staging.previous/${JOB_NAME} $DESTINATION/builds/staging.previous/${JOB_NAME}.2

			# move contents of /builds/staging/${JOB_NAME} into /builds/staging.previous/${JOB_NAME}
			mv $DESTINATION/builds/staging/${JOB_NAME} $DESTINATION/builds/staging.previous/${JOB_NAME}

			# move contents of /builds/staging/${JOB_NAME}.next into /builds/staging/${JOB_NAME}
			mv $DESTINATION/builds/staging/${JOB_NAME}.next $DESTINATION/builds/staging/${JOB_NAME}
		fi

		# generate 2 ${STAGINGDIR}/all/composite*.xml files which will point at:
			# /builds/staging/${JOB_NAME}/all/repo/
			# /builds/staging.previous/${JOB_NAME}/all/repo/
			# /builds/staging.previous/${JOB_NAME}.2/all/repo/
		now=$(date +%s000)
		mkdir -p ${STAGINGDIR}/all
		echo "<?xml version='1.0' encoding='UTF-8'?>
<?compositeMetadataRepository version='1.0.0'?>
<repository name='JBoss Tools Staging - ${JOB_NAME} Composite' type='org.eclipse.equinox.internal.p2.metadata.repository.CompositeMetadataRepository' version='1.0.0'>
" > ${STAGINGDIR}/all/compositeContent.xml
		echo "<?xml version='1.0' encoding='UTF-8'?>
<?compositeArtifactRepository version='1.0.0'?>
<repository name='JBoss Tools Staging - ${JOB_NAME} Composite' type='org.eclipse.equinox.internal.p2.artifact.repository.CompositeArtifactRepository' version='1.0.0'> " > ${STAGINGDIR}/all/compositeArtifacts.xml
		metadata="<properties size='2'><property name='p2.compressed' value='true'/><property name='p2.timestamp' value='"${now}"'/></properties>
<children size='2'>
<child location='../../../staging/${JOB_NAME}/all/repo/'/>
<child location='../../../staging.previous/${JOB_NAME}/all/repo/'/>
</children>
</repository>
"
		echo $metadata >> ${STAGINGDIR}/all/compositeContent.xml
		echo $metadata >> ${STAGINGDIR}/all/compositeArtifacts.xml
		date; rsync -arzq --protocol=28 ${STAGINGDIR}/all/composite*.xml $DESTINATION/builds/staging/${JOB_NAME}/all/

		# create a snapshot dir outside Hudson which is file:// accessible
		date; rsync -arzq --delete ${STAGINGDIR}/* $INTRNALDEST/builds/staging/${JOB_NAME}.next

		# cycle internal copy of ${JOB_NAME} in staging and staging.previous
		mkdir -p $INTRNALDEST/builds/staging/${JOB_NAME}/
		# purge contents of /builds/staging.previous/${JOB_NAME} and remove empty dir
		rm -fr $INTRNALDEST/builds/staging.previous/${JOB_NAME}/
		# move contents of /builds/staging/${JOB_NAME} into /builds/staging.previous/${JOB_NAME}
		mv $INTRNALDEST/builds/staging/${JOB_NAME} $INTRNALDEST/builds/staging.previous/${JOB_NAME}
		# move contents of /builds/staging/${JOB_NAME}.next into /builds/staging/${JOB_NAME}
		mv $INTRNALDEST/builds/staging/${JOB_NAME}.next $INTRNALDEST/builds/staging/${JOB_NAME}
	fi

	# extra publish step for aggregate update sites ONLY
	if [[ ${JOB_NAME/.aggregate} != ${JOB_NAME} ]]; then
		if [[ ${PUBLISHPATHSUFFIX} ]]; then 
			# create folders if not already there
			if [[ ${DESTINATION##*@*:*} == "" ]]; then # user@server, do remote op
				seg="."; for d in ${PUBLISHPATHSUFFIX/\// }; do seg=$seg/$d; echo -e "mkdir ${seg:2}" | sftp $DESTINATION/updates/nightly/; done; seg=""
			else
				mkdir -p $DESTINATION/updates/nightly/${PUBLISHPATHSUFFIX}
			fi
			date; rsync -arzq --protocol=28 --delete ${STAGINGDIR}/all/repo/* $DESTINATION/updates/nightly/${PUBLISHPATHSUFFIX}/
		else
			date; rsync -arzq --protocol=28 --delete ${STAGINGDIR}/all/repo/* $DESTINATION/updates/nightly/${JOBNAMEREDUX}/
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
date; rsync -arzq --protocol=28 --delete ${STAGINGDIR}/logs $DESTINATION/builds/staging/${JOB_NAME}/
date; rsync -arzq --delete ${STAGINGDIR}/logs $INTRNALDEST/builds/staging/${JOB_NAME}/

# purge tmpdir
rm -fr $tmpdir

if [[ ${JOB_NAME/.aggregate} != ${JOB_NAME} ]]; then
	# regenerate http://download.jboss.org/jbosstools/builds/nightly/*/*/composite*.xml files for up to 5 builds, cleaning anything older than 5 days old
	wget http://anonsvn.jboss.org/repos/jbosstools/trunk/build/util/cleanup/jbosstools-cleanup.sh --no-check-certificate
	chmod +x jbosstools-cleanup.sh
	./jbosstools-cleanup.sh 5 5
	rm -f jbosstools-cleanup.sh
fi

# to avoid looking for files that are still being synched/nfs-copied, wait a bit before trying to run tests (the next step usually)
sleep 15s
