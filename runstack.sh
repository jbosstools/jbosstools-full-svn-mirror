#!/bin/bash

#root of svn tree
workingdir=~/workspace36/jbosstools-modular_build

#use commandline args as list of components to build
if [[ $# -lt 1 ]]; then
	echo "Usage: $0 component1 component2 ..."
	echo "Eg. $0 tests common jmx archives as"
	echo "Eg. $0 jst jsf vpe struts seam"
	exit 1
fi

components="$*"

# run builds w/o running tests
for d in $components; do 
	cd $workingdir; ./runtests.sh ${d}/ clean install -Dmaven.test.skip
done

# collect compilation results and failures
for d in $components; do 
	cd $workingdir; echo "==== $d ===="
	egrep -v "org\.|com\." $d/buildlog.latest.txt | egrep "SUCCESS|FAIL"
	egrep -A1 "Cannot complete the request|depends on|satisfy dependency|Missing requirement|requires '.+'" $d/buildlog.latest.txt
	echo ""
done

