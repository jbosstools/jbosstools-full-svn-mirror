#!/bin/bash
script=$0
if [[ ${script%%publish.sh} != $script ]]; then
	script=${script/publish.sh/publish\/publish.sh}
else
	script=$1
	script=${script/publish.sh/publish\/publish.sh}
fi
. ${script}
