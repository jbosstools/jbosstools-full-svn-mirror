#!/bin/bash

if [[ $1 ]] && [[ -d $1 ]]; then
	basedir=$1
else
	basedir=.
fi

if [[ $2 ]] && [[ -f $2 ]]; then
	versionlist=$2
elif [[ -f /tmp/versionlist ]]; then
	versionlist=/tmp/versionlist
else
	echo "Must run ${0/checkSVNArtifactVersions.sh/checkCompiledArtifactVersions.sh} against baseline (directory w/ plugin and feature jars)"
	exit 1;
fi

newsvnbasedir=https://svn.jboss.org/repos/jbosstools/trunk
oldsvnbasedir=https://svn.jboss.org/repos/jbosstools/tags/jbosstools-3.1.0.GA

# get changelist as svn diff between trunk and last stable tag
changelist=/tmp/changelist
if [[ ! -f $changelist ]]; then
	svn diff --summarize --new=${newsvnbasedir} --old=${oldsvnbasedir} > $changelist
fi

# parse changelist to show plugin/feature versions
dirs="$(grep -v docs/reference $changelist | grep -v /documentation/ | grep -v GA/.*/docs/ | sed 's/^.*https:.*3.1.0.GA//g' | perl -pe 's|(/.*?/.*?/.*?)/.*|\1|' | sort | uniq)"

features=0
plugins=0
for f in $dirs; do 
	if [[ -f ${basedir}${f}/META-INF/MANIFEST.MF ]]; then
		(( plugins++ ));
	fi
	if [[ -f ${basedir}${f}/feature.xml ]]; then
		(( features++ ));
	fi
done
echo "Found $features features and $plugins plugins to check."

. ${versionlist}

for f in $dirs; do 
	if [[ -f ${basedir}${f}/META-INF/MANIFEST.MF ]] || [[ -f ${basedir}${f}/feature.xml ]]; then
		echo ""
		echo "== ${basedir}${f} ==";
		echo ""
		if [[ -f ${basedir}${f}/feature.xml ]]; then
			dos2unix -q ${basedir}${f}/feature.xml
			v=$(head -16 ${basedir}${f}/feature.xml | grep -v "<?xml" | grep -v "version=\"0.0.0\"" | grep "version=" | head -1 | tr -d "a-zA-Z=\"> 	"); v=${v%.};
			a=${f##*/}  # org.jboss.tools.maven.seam.feature
			b=${a//./_} # org_jboss_tools_maven_seam_feature

			echo "diff svn artifact:"
			svn diff --summarize --new=${newsvnbasedir}${f} --old=${oldsvnbasedir}${f} | perl -pe "s#${oldsvnbasedir}${f}#${a}#g"
			echo ""

			echo "diff svn manifest:"
			svn diff --new=${newsvnbasedir}${f}/feature.xml --old=${oldsvnbasedir}${f}/feature.xml | grep -v "<?xml" | grep -v "version=\"0.0.0\"" | grep "version="
			echo ""

			echo "diff rel baseline:"
			if [[ ${!b} ]]; then
				echo "- ${!b} :: $a"
			else
				echo "- ?.?.? :: $a"
			fi
			echo "+ $v :: $a"
		elif [[ -f ${basedir}${f}/META-INF/MANIFEST.MF ]]; then
			dos2unix -q ${basedir}${f}/META-INF/MANIFEST.MF
			v=$(cat ${basedir}${f}/META-INF/MANIFEST.MF | grep Bundle-Version | perl -pe "s#Bundle-Version: ##g"); v=${v:0:5}
			a=${f##*/}  # org.jboss.tools.maven.seam.feature
			b=${a//./_} # org_jboss_tools_maven_seam_feature

			echo "diff svn artifact:"
			svn diff --summarize --new=${newsvnbasedir}${f} --old=${oldsvnbasedir}${f} | perl -pe "s#${oldsvnbasedir}${f}#${a}#g"
			echo ""

			echo "diff svn manifest:"
			svn diff --new=${newsvnbasedir}${f}/META-INF/MANIFEST.MF --old=${oldsvnbasedir}${f}/META-INF/MANIFEST.MF | grep Bundle-Version
			echo ""

			echo "diff rel baseline:"
			if [[ ${!b} ]]; then
				echo "- ${!b} :: $a"
			else
				echo "- ?.?.? :: $a"
			fi
			echo "+ $v :: $a"
		fi
	fi
done

