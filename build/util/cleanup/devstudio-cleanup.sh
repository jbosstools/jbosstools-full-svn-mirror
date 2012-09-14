#!/bin/bash
# This script is run here: http://hudson.qa.jboss.com/hudson/job/devstudio-cleanup/configure
# And archived here: https://svn.jboss.org/repos/devstudio/trunk/releng/org.jboss.ide.eclipse.releng/hudson/devstudio-cleanup.sh
# --------------------------------------------------------------------------------
# clean JBDS builds from fish://dev01.qa.atl2.redhat.com:/qa/services/http/binaries/RHDS/{nightly,release}

root=/qa/services/http/binaries/RHDS/builds
log=/tmp/${0##*/}.log.`date +%Y%m%d-%H%M`.txt

echo "Logfile: $log" | tee -a $log
echo "" | tee -a $log
 
# before
df -h $root | tee -a $log
echo "" | tee -a $log

# Check for $type builds more than $threshhold days old; keep minimum $numkeep builds per branch
clean () 
{
	type=$1 # nightly or release
	numkeep=$2 # number of builds to keep per branch
	threshhold=$3 # age at which a build is available for delete
	echo "Check for $type builds more than $threshhold days old; keep minimum $numkeep builds per branch" | tee -a $log 
	for d in $(find $root/$type -maxdepth 1 -mindepth 1 -type d); do
		newest=$(find $d -maxdepth 1 -mindepth 1 -type d -name "201*" | sort -r | head -$numkeep) # keep these
		all=$(find $d -maxdepth 1 -mindepth 1 -type d -name "201*" | sort -r) # only look to delete things more than $threshhold days old
		for dd in $all; do
			keep=0;
			for n in $newest; do
				if [[ $dd == $n ]]; then
					keep=1
				fi
			done
			if [[ $keep -eq 0 ]]; then
				echo "- $dd... " | tee -a $log
				if [[ $USER == "hudson" ]]; then rm -fr $dd; fi
			else
				echo "+ $dd" | tee -a $log
			fi
		done
	done
	echo "" | tee -a $log	
}

clean nightly 3 3

# after
df -h $root | tee -a $log
echo "" | tee -a $log

#du -sch $root/*  | tee -a $log
