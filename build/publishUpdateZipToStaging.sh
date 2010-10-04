# Use this script to publish a single update site zip via a Hudson job

if [[ $1 ]]; then
	# specify job to publish on commandline
	JOBNAMEREDUX=$1
elif [[ ${JOB_NAME} ]]; then
	# pull name of the job to publish from the name of the publisher job
	JOBNAMEREDUX=${JOB_NAME/-publish}
else
	echo "Must run from Hudson or manually specify job name"
	echo "(for offline testing), eg., $0 jbosstools-teiid-designer"
	exit 1
fi	

if [[ $DESTINATION == "" ]]; then DESTINATION="tools@filemgmt.jboss.org:/downloads_htdocs/tools"; fi

# get zip name from zip.list.txt
path=$(wget -q http://download.jboss.org/jbosstools/builds/staging/${JOBNAMEREDUX}/logs/zip.list.txt -O - | egrep -- "-Update-" | head -1 | sed "s#,\\\\##"); 

#echo "Fetch ${path} as ${JOBNAMEREDUX}-Update.zip"
# to test locally, may need to use --protocol=29 and -P instead of -q
date; rsync -arzq --rsh=ssh ${DESTINATION}/builds/staging/${JOBNAMEREDUX}/${path} ${JOBNAMEREDUX}-Update.zip

#echo "Publish ${path} as ${JOBNAMEREDUX}-Update.zip"
# to test locally, may need to use --protocol=29 and -P instead of -q
date; rsync -arzq --rsh=ssh ${JOBNAMEREDUX}-Update.zip ${DESTINATION}/updates/staging/
