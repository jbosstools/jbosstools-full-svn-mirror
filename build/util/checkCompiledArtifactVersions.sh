#!/bin/bash

# establish a baseline -- for JBT 3.0.0, we released plugins of the following versions:
#org.jboss.ide.eclipse.as.feature=2.1.0
#org.jboss.tools.eclipse.as.tptp=1.1.0
#org.jboss.ide.eclipse.as.wtp.core=2.2.0
#
#org.jboss.tools.bpel.feature=1.0.0
#org.eclipse.bpel.common.ui=0.5.0
# ...

if [[ $1 ]] && [[ -d $1 ]]; then
	basedir=$1
else
	basedir=.
fi

# point at an update site to produce list of versions

versionlist=/tmp/versionlist; rm -fr $versionlist

for d in tools.common jmx archives .as freemarker profiler portlet xulrunner jst vpe jsf tptp ws cdi struts hibernate seam examples birt maven; do 
	#echo " == $d =="
	for a in $(find $basedir -maxdepth 3 -name "*${d}*" | sort); do
		b=$a; b=${b##*/}; b=${b%.*}; b=${b%.jar};
		a=${b%%_*}; a=${a//./_} # org_jboss_tools_maven_seam_feature
		v=${b#*_}; v=${v:0:5}
		echo "${a}=${v}" >> $versionlist
		#echo "${a}=${v} ($b)"
	done
	#echo " -- $d --"
done
echo ""
echo "Baseline artifact version information stored in ${versionlist}"
