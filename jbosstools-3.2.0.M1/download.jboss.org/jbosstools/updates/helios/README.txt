To rsync a mirror from eclipse.org to here, do the following:

1. ssh to qa01, then sudo to hudson:

$ ssh nboldt@qa01.qa.atl2.redhat.com
$ sudo su - hudson

2. rsync build.eclipse.org to reports.qa:

$ rsync -aP --delete nickb@build.eclipse.org:~/downloads/releases/helios/* /qa/services/http/binaries/RHDS/updates/helios/

3. rsync reports.qa to download.jboss.org:
 
$ rsync -aP --delete /qa/services/http/binaries/RHDS/updates/helios/* jbossqa@filemgmt.jboss.org:/htdocs/jbosstools/updates/helios/