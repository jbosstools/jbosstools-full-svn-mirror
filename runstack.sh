#!/bin/bash

#root of svn tree
workingdir=~/workspace36/jbosstools-modular_build

#use commandline args as list of components to build
if [[ $# -lt 1 ]]; then
	echo "Usage: $0 component1 component2 ..."
	echo "Eg. $0 tests common jmx archives as"
	echo "Eg. $0 jst jsf vpe struts seam -Dmaven.test.skip"
	exit 1
fi

flags=""
components=""
while [ "$#" -gt 0 ]; do
        case $1 in
                '-'*) flags="$flags $1"; shift 1;;
                *) components="$components $1"; shift 1;;
        esac
done

# run builds w/o running tests
for d in $components; do 
	# build features, plugins, and tests, but do not RUN tests
	#cd $workingdir; ./runtests.sh ${d}/ clean install -Dmaven.test.skip

	# build features, plugins, and tests, then run ALL tests (don't stop after first failed test)
	cd $workingdir; ./runtests.sh ${d}/ clean install --fail-at-end $flags
done

# collect compilation results and failures
for d in $components; do 
	cd $workingdir; echo "==== $d ===="
	egrep -v "org\.|com\." $d/buildlog.latest.txt | egrep "SUCCESS"
	egrep "FAILURE|SKIPPED" $d/buildlog.latest.txt
	egrep -A1 "Cannot complete the request|depends on|satisfy dependency|Missing requirement|requires '.+'" $d/buildlog.latest.txt
	echo ""
done

