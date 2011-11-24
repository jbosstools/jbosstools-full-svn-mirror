#!/bin/bash
tmpfile=/tmp/svn.ignore.txt
echo "target
buildlog.latest.txt
bin
build
" > $tmpfile

dir="."; if [[ $1 ]]; then dir=$1; fi

for p in com org net; do
	for f in $(find $dir -mindepth 2 -type d -name "${p}.*" | sort); do 
		pushd $f 2>&1 >/dev/null
		echo $f
		svn up --accept 'theirs-full'
		svn propset svn:ignore --file $tmpfile .
		popd 2>&1 >/dev/null
		echo ""
	done
done
rm -fr $tmpfile
svn diff $dir

echo ""
echo "==================================================================="
echo ""
echo "Pending changes:"
svn stat $dir

echo ""
echo "To commit changes, type:
  cd $dir; svn ci -m \"svn:ignore\""
echo ""

