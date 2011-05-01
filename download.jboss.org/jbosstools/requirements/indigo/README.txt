1 custom built zip:

Basebuilder from latest tag (http://wiki.eclipse.org/Platform-releng-basebuilder#Current_build_tag_for_3.7_stream_builds_.28Indigo.29)

cvs -d :pserver:anonymous@dev.eclipse.org:/cvsroot/eclipse -q ex -d org.eclipse.releng.basebuilder -r R37_M6  org.eclipse.releng.basebuilder; zip -r9 org.eclipse.releng.basebuilder_R37_M6.zip org.eclipse.releng.basebuilder

