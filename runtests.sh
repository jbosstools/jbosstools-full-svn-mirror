#!/bin/bash

# set corect path to mvn on your machine:
mvn3=/opt/maven3/bin/mvn

if [[ $# -lt 1 ]]; then
	# some useful flags:
	# -pl   - list of projects to build
	# -o    - offline mode (don't search remote repos)
	# -Dmaven.test.skip - compile but do not run tests
        # --fail-at-end     - fail build after ALL tests have run, not at first failure
	echo "Usage: $0 workingdir flags targets"
	echo "Eg: $0 as/ -Dmaven.test.skip clean install"
	echo "Eg: $0 bpel/tests/ -o --fail-at-end install"
	exit 1;
fi

#echo "[runtests] $0 started on: `date +%H\:%M\:%S`";

# environment variables
PATH=.:/bin:/usr/bin:/usr/bin/X11:/usr/local/bin:/usr/X11R6/bin:`pwd`/../linux;export PATH

#export USERNAME=`whoami`
#echo "[runtests] Run as $USERNAME";
#echo "[runtests] With PATH = $PATH";

# fix for org.eclipse.swt.SWTError: No more handles [gtk_init_check() failed]
# fix for Failed to invoke suite():org.eclipse.swt.SWTError: No more handles [gtk_init_check() failed]
export CVS_RSH=ssh
#ulimit -c unlimited; # set corefile size to unlimited; not allowed on build.eclipse

#echo "[runtests] Set JAVA_HIGH_ZIPFDS=500 & LANG=C";
#export JAVA_HIGH_ZIPFDS=500
#export LANG=C

usedPorts=""; maxPort=40
for port in $(ps aux | egrep '[Xvnc|Xvfb]\ :' | egrep " :[0-9]+" | sed "s/\(\^\|.\+\)\(Xvfb\|Xvnc\) :\([0-9]\+\)\(.\+\|$\)/\3/g" | sort); do
        if [[ $(echo $port | egrep "^:[0-9]+$") ]]; then
                usedPorts=$usedPorts" "${port:1};
                thisPort=${port:1}; (( thisPort -= 0 ));
                if [[ $maxPort -lt $thisPort ]]; then maxPort=$thisPort; fi
                #echo "[$usedPorts], $thisPort, $maxPort"
        fi
done
(( xport = maxPort + 1 ));
#echo "Existing DISPLAY ports include: $usedPorts."
#echo "Use DISPLAY port :$xport"

xCmd=""
xvncExists=$(which Xvnc); xvncExists=${xvncExists##*no Xvnc *}
if [ $xvncExists ]; then 
	xCmd="Xvnc :${xport} -geometry 1024x768 -depth 24 -ac"
else
	xvfbExists=$(which Xvfb); xvfbExists=${xvfbExists##*no Xvfb *}
		if [ $xvfbExists ]; then
  			xCmd="Xvfb :${xport} -screen 0 1024x768x24 -ac"
	else
		echo "[runtests] WARNING! This script requires Xvfb or Xvnc. "
		echo "[runtests] Without some way to run tests in a different display port, UI tests will run in front of you and you may accidentally interact with them."
	fi
fi

if [[ $xCmd ]]; then 
	#echo "[runtests] Using X server: '${xCmd}'"
	${xCmd} &
	export DISPLAY=localhost:${xport}.0
	xhost +
else
	echo "[runtests] Warning! UI tests will run in the current UI display port (usually :0). Please avoid accidentally interacting with them."
fi	


# run tests
echo "[runtests] [`date +%H\:%M\:%S`] Launching Tycho..."
dir=$1; shift;

cd $dir; $mvn3 2>&1 $* | tee buildlog.latest.txt

echo "[runtests] [`date +%H\:%M\:%S`] Test run completed. "

# xwd -silent -display :${xport} -root -out /tmp/snap.xwd; # save a snapshot

############################# END RUN TESTS #############################

# drop X server process threads used by tests
if [[ -r /tmp/.X${xport}-lock ]]; then kill `cat /tmp/.X${xport}-lock`; fi
if [[ -f /tmp/.X${xport}-lock ]]; then rm -fr /tmp/.X${xport}-lock; fi

#echo "[runtests] ${0##*/} done: `date +%H\:%M\:%S`"
