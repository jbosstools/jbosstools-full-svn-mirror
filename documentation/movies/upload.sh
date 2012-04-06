rsync -i -I -c -P -C --protocol=29 --exclude-from '.rsyncexclude' --include=core -avz . tools@filemgmt.jboss.org:/docs_htdocs/tools/movies/
