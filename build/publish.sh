#!/bin/bash
script=$0
if [[ ${script%%publish.sh} != $script ]]; then
	script=${script/publish.sh/publish\/publish.sh}
else
	script=`cat $0 | grep ".sh"`
	script=${script/publish.sh/publish\/publish.sh}
	script=${script/. /}
fi
. ${script}
