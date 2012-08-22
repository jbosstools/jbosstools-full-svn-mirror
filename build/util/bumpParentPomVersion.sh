#!/bin/bash
old=$1
new=$2
if [[ $1 ]] && [[ $2 ]]; then
	for p in $(find.sh . pom\*.xml ${old} target "" -q); do sed -i -e "s#<version>${old}</version>#<version>${new}</version>#g" $p; done; 
else
	echo "Usage: $0 4.0.0.M1-SNAPSHOT 4.0.0.Beta1-SNAPSHOT"; exit 1
fi

