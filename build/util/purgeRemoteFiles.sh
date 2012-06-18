#!/bin/bash

# first need a list of files to purge, like this:
#
#tools/updates/requirements/testng/.gitignore
#tools/updates/requirements/swtbot/.gitignore

# can get such a file like this:
# rsync -Pr --rsh=ssh --protocol=28 $TOOLS -f "+ */" -f "+ .git*" -f "- *" | tee ~/tmp/dotsvn.files.list.txt 
# then clean out file metadata (sed -i "s#.+ tools/#tools/#g" file)
# then purge all but the files you want (eg., .gitignore)

DESTINATION=tools@filemgmt.jboss.org:/downloads_htdocs/
inputfile=/home/nboldt/tmp/dotsvn.files.list.grepped.txt
if [[ $1 ]]; then inputfile=$1; fi

for l in $(cat ${inputfile}); do 
	echo "$l ..."; 
	# make it editable
	echo "chmod 777 $l" | sftp ${DESTINATION}
	# remove the now-empty file
	echo "rm $l" | sftp ${DESTINATION}
done
