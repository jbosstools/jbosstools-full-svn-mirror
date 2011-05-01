1 custom built zip:

Basebuilder from latest tag (http://wiki.eclipse.org/Platform-releng-basebuilder#Current_build_tag_for_3.6_stream_builds_.28Helios.29)

cvs -d :pserver:anonymous@dev.eclipse.org:/cvsroot/eclipse -q ex -d org.eclipse.releng.basebuilder -r r36x_v20101125 org.eclipse.releng.basebuilder; zip -r9 org.eclipse.releng.basebuilder_r36x_v20101125.zip org.eclipse.releng.basebuilder

