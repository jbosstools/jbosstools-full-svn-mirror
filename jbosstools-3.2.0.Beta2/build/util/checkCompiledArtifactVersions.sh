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

versionlist=/tmp/versionlist; rm -fr $versionlist

for d in tools.common tools.flow jbpm jmx archives .as bpel smooks freemarker profiler portlet modeshape xulrunner jst vpe jsf drools esb tptp ws cdi struts hibernatetools seam examples birt maven; do 
	#echo " == $d =="
	for a in $(find $basedir -maxdepth 2 -name "*${d}*"); do
		b=$a; b=${b##*/}; b=${b%.*}; b=${b%.jar};
		a=${b%%_*}; a=${a//./_} # org_jboss_tools_maven_seam_feature
		v=${b#*_}; v=${v:0:5}
		echo "${a}=${v}" | tee -a $versionlist
	done
	#echo " -- $d --"
done
echo ""
echo "Baseline artifact version information stored in ${versionlist}."
