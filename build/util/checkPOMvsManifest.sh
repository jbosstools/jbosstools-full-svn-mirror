#!/bin/bash

verbose=false
debug=false

if [[ $1 ]] && [[ -d $1 ]]; then
	basedir=$1
else
	basedir=.
fi

if [[ $2 == "-v" ]]; then verbose=true; fi
if [[ $2 == "-d" ]]; then debug=true; fi

files=$(find . -maxdepth 5 -type f -name MANIFEST.MF | egrep -v "target|sdk")" "$(find . -maxdepth 4 -type f -name "feature.xml" | egrep -v "target|sdk")
for f in $files; do
	d=${f/META-INF\/MANIFEST.MF/}; #assume plugin
	if [[ $d == $f ]]; then #else feature
		d=${f/feature.xml/}
		artVersionLine=$(cat $f | head -7 | egrep "version=\"[0-9]\.[0-9]\.[0-9].*\".*" | egrep "[0-9]\.[0-9]\.[0-9]" | tail -1)
		artVersion=${artVersionLine##*version=\"}
		#z=${artVersion##.qualifier*}; echo "[$z]"
		if [[ ${artVersion##*.qualifier*} == "" ]]; then # has a qualifier
			true
		else
			av=${artVersion%%\"*}
			echo "[WARNING] No .qualifer in [$av] ${d}feature.xml"
			av=""
			#echo "artVersion[2] = $artVersion"
		fi
		artVersion=${artVersion%%.qualifier*}
		artVersion=${artVersion%%\"*}
		artVersionLine="   "$artVersionLine
	else
		artVersionLine=$(cat $f | egrep "Bundle-Version: " | egrep "[0-9]\.[0-9]\.[0-9]" | tr "\r\t\n" "\n" )
		artVersion=${artVersionLine##*: }
		#z=${artVersion##.qualifier*}; echo "[$z]"
		if [[ ${artVersion##*.qualifier*} == "" ]]; then # has a qualifier
                        true
                else
                        av=${artVersion%%\"*}
                        echo "[WARNING] No .qualifier in [$av] ${d}"
                        av=""
                        #echo "artVersion[2] = $artVersion"
                fi
		artVersion=${artVersion%%.qualifier*}
		artVersionLine="  "$artVersionLine
	fi

	if [[ -f $d/pom.xml ]]; then
		pomVersionLine=$(cat $d/pom.xml | sed '/<parent>/,/<\/parent>/ d' | sed '/<dependency>/,/<\/dependency>/ d' | sed '/<parent>/,+4 d' | sed "s/\t//" | egrep "<version>" | egrep "[0-9]\.[0-9]\.[0-9]" | head -1)
		pomVersion=${pomVersionLine%%</version>*}
		if [[ ${pomVersion##*-SNAPSHOT*} == "" ]]; then # has a -SNAPSHOT suffix
			true
		else
			pv=${pomVersion#*<version>}
			echo "[WARNING] No -SNAPSHOT in [$pv] ${d}pom.xml"
			pv=""
		fi
		pomVersion=${pomVersion%%-SNAPSHOT*}
		pomVersion=${pomVersion#*<version>}
		pomVersionLine="         "$pomVersionLine

		if [[ $artVersion != $pomVersion ]]; then 
			echo "[ERROR] $d"
			echo "[ERROR] Artifact version [$artVersion] != pom version [$pomVersion]" | egrep "[0-9]\.[0-9]\.[0-9]"; 
			echo "[ERROR] $artVersionLine"
			echo "[ERROR] $pomVersionLine"
			echo ""
		elif [[ $debug == true ]]; then
			echo "[DEBUG] $d"
			echo "[DEBUG] Artifact version [$artVersion] != pom version [$pomVersion]" | egrep "[0-9]\.[0-9]\.[0-9]"; 
			echo "[DEBUG] $artVersionLine"
			echo "[DEBUG] $pomVersionLine"
			echo ""
		elif [[ $verbose == true ]]; then
			echo "[INFO] [$artVersion] = [$pomVersion] $d "
		else
			true
		fi
	else
		echo "[WARNING] No pom.xml in $d"
	fi
done

