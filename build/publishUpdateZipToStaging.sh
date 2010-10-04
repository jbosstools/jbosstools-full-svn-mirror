# Use this script to publisher a single update site zip via a Hudson job

# get zip name from zip.list.txt
path=$(wget -q http://download.jboss.org/jbosstools/builds/staging/${JOB_NAME}/logs/zip.list.txt -O - | egrep -- "-Update-" | head -1 | sed "s#,\\\\##"); 

# get zip to local
rsync -arzq tools@filemgmt.jboss.org:/downloads_htdocs/tools/builds/staging/${JOB_NAME}/${path} .

# push renamed zip to remote
cd ${path%/*}; rsync -arzq ${path##*/} tools@filemgmt.jboss.org:/downloads_htdocs/tools/updates/staging/${JOB_NAME}-Update.zip

