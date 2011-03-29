#!/bin/bash
# Temp workaround to fix missing sites in composite repo

DESTINATION="tools@filemgmt.jboss.org:/downloads_htdocs/tools/builds/staging/_composite_/trunk"
rsync -arzq --delete ${WORKSPACE}/composite/ ${DESTINATION}