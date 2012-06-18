#!/bin/bash

# first need a list of folders to purge, like this:
#
#tools/builds/staging/_composite_/soa-tooling/3.3.0.Nightly/.svn
#tools/builds/staging/_composite_/soa-tooling/3.3.0.Beta1/.svn
#tools/builds/staging/_composite_/soa-tooling/3.3.0.Beta2/.svn

# can get such a file like this:
# rsync -Pr --rsh=ssh --protocol=28 $TOOLS -f "+ */" -f "+ .svn" -f "+ .git*" -f "- *" | tee ~/tmp/dotsvn.files.list.txt 
# then clean out file metadata (sed -i "s#.+ tools/#tools/#g" file)
# then purge all but the parent dirs you want (.svn, but not .svn/tmp) 


DESTINATION=tools@filemgmt.jboss.org:/downloads_htdocs/
inputfile=/home/nboldt/tmp/dotsvn.folders.list.grepped.txt
if [[ $1 ]]; then inputfile=$1; fi

tmpdir=/tmp/purgeRemoteFolders.sh.empty
mkdir ${tmpdir}
pushd ${tmpdir} >/dev/null

for l in $(cat ${inputfile}); do 
	echo "$l ..."; 
	# make it editable
	echo "chmod 777 $l" | sftp ${DESTINATION}
	# empty the target folder
	rsync -rPz --rsh=ssh --protocol=28 --delete ${tmpdir}/ ${DESTINATION}/$l/
	# remove the now-empty dir
	echo "rmdir $l" | sftp ${DESTINATION}
done

popd >/dev/null
rm -fr ${tmpdir}
