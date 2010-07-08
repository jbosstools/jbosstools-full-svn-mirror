#!/bin/bash

if [[ $1 ]] && [[ -d $1 ]]; then
	basedir=$1
else
	basedir=.
fi

files=$(find . -maxdepth 5 -type f -name MANIFEST.MF | egrep -v "target|sdk")" "$(find . -maxdepth 4 -type f -name "feature.xml" | egrep -v "target|sdk")
for f in $files; do
	d=${f/META-INF\/MANIFEST.MF/}; #assume plugin
	if [[ $d == $f ]]; then #else feature
		d=${f/feature.xml/}
		artVersionLine=$(cat $f | egrep "version=\"[0-9]\.[0-9]\.[0-9]\.qualifier" | egrep "[0-9]\.[0-9]\.[0-9]")
		if [[ ! $artVersionLine ]]; then # not a snapshot version
			artVersionLine=$(cat $f | head -7 | egrep "version=\"[0-9]\.[0-9]\.[0-9].*\".*" | egrep "[0-9]\.[0-9]\.[0-9]" | tail -1)
			artVersion=${artVersionLine##*version=\"}
			artVersion=${artVersion%%\"*}
			#echo "[WARNING] [$altVersion] ${d}feature.xml not .qualifier version."
		else
			artVersion=${artVersionLine##*version=\"}
			artVersion=${artVersion%%.qualifier*}
		fi
		artVersionLine="   "$altVersionLine
	else
		artVersionLine=$(cat $f | egrep "Bundle-Version: " | egrep "[0-9]\.[0-9]\.[0-9]")
		artVersion=${artVersionLine##*: }
		artVersion=${artVersion%%.qualifier}
		artVersionLine="  "$artVersionLine
	fi

	if [[ -f $d/pom.xml ]]; then
		pomVersionLine=$(cat $d/pom.xml | sed "s/\t//" | egrep ".0-SNAPSHOT" | egrep "[0-9]\.[0-9]\.[0-9]")
		if [[ ! $pomVersionLine ]]; then # not a snapshot version
			pomVersionLine=$(cat $d/pom.xml | sed "s/\t//" | egrep "<version>" | egrep "[0-9]\.[0-9]\.[0-9]" | tail -1)
			pomVersion=${pomVersionLine%%</version>*}
			pomVersion=${pomVersion#*<version>}
			#echo "[WARNING] [$pomVersion] ${d}pom.xml not SNAPSHOT version."
		else
			pomVersion=${pomVersionLine%%-SNAPSHOT*}
			pomVersion=${pomVersion#*<version>}
		fi
		pomVersionLine="         "$pomVersionLine

		if [[ $artVersion != $pomVersion ]]; then 
			echo "[ERROR] $d"
			echo "[ERROR] Artifact version [$artVersion] != pom version [$pomVersion]" | egrep "[0-9]\.[0-9]\.[0-9]"; 
			echo "[ERROR] $artVersionLine"
			echo "[ERROR] $pomVersionLine"
			echo ""
		else
			#echo "[INFO] [$artVersion] = [$pomVersion] $d "
			true
		fi
	else
		echo "[WARNING] $d contains no pom.xml"
	fi
done

