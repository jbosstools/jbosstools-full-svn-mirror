# Use this script to publish 1 or more component update site zips from an aggregate via a Hudson job

if [[ $1 ]]; then
	pattern="$1"	
else
	echo "Must run from Hudson or manually specify job name"
	echo "(for offline testing), eg., $0 \"drools|teiid|usage|jbpm\" jbosstools-3.2.0.Beta2.aggregate"
	exit 1
fi

if [[ $2 ]]; then
	# specify job to publish on commandline
	JOBNAMEREDUX=$2
elif [[ ${JOB_NAME} ]]; then
	# pull name of the job to publish from the name of the publisher job
	JOBNAMEREDUX=${JOB_NAME/-publish}
else
	echo "Must run from Hudson or manually specify job name"
	echo "(for offline testing), eg., $0 jbosstools-teiid-designer"
	exit 1
fi

if [[ $DESTINATION == "" ]]; then DESTINATION="tools@filemgmt.jboss.org:/downloads_htdocs/tools"; fi

# get zip names from zip.list.txt
paths=$(wget -q http://download.jboss.org/jbosstools/builds/staging/${JOBNAMEREDUX}/logs/zip.list.txt -O - | egrep -- "-Update-" | egrep "${pattern}" | sed "s#,\\\\##"); 

for path in $paths; do
	targetZip=${path##*/};targetZip=${targetZip%%-Update-*}-Update.zip; 
	#echo ""; echo inputPath=$path; echo targetZip=${targetZip}
	 
	#echo "Fetch ${path} as ${targetZip}"
	# to test locally, may need to use --protocol=29 and -P instead of -q
	date; rsync -arzq --rsh=ssh ${DESTINATION}/builds/staging/${JOBNAMEREDUX}/${path} ${targetZip}
	
	#echo "Publish ${path} as ${targetZip}"
	# to test locally, may need to use --protocol=29 and -P instead of -q
	date; rsync -arzq --rsh=ssh ${targetZip} ${DESTINATION}/updates/staging/
done