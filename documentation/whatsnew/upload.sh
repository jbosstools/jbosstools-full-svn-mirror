rsync -i -I -c -P -e ssh -C --exclude-from '.rsyncexclude' -avz . tools@filemgmt.jboss.org:/docs_htdocs/tools/whatsnew/
