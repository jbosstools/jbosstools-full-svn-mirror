1. First, build the various components referenced in site.xml (or pull them from upstream previous builds hosted on http://download.jboss.org/jbosstools/builds/staging/)

Components are here:

    esb: http://anonsvn.jboss.org/repos/jbosstools/trunk/esb/

    smooks: http://anonsvn.jboss.org/repos/jbosstools/trunk/smooks/

    jbpm: http://anonsvn.jboss.org/repos/jbosstools/trunk/jbpm AND http://anonsvn.jboss.org/repos/jbosstools/trunk/flow/

    modeshape: http://anonsvn.jboss.org/repos/jbosstools/trunk/modeshape/

    pi4soa: https://pi4soa.svn.sourceforge.net/svnroot/pi4soa

    savara: https://github.com/savara/savara-tools-eclipse AND https://github.com/scribble/scribble-tools-eclipse

    drools & guvnor: https://github.com/droolsjbpm/droolsjbpm-tools

    Teiid: http://anonsvn.jboss.org/repos/tdesigner/trunk/

    bpmn2: http://git.eclipse.org/c/bpmn2-modeler/org.eclipse.bpmn2-modeler.git/

    BPEL: http://git.eclipse.org/c/bpel/org.eclipse.bpel.git/ AND http://anonsvn.jboss.org/repos/jbosstools/trunk/bpel/

2. Next, ensure the parent pom in ../../parent/pom.xml has been built w/ `mvn clean install` (this may not be required as the next step should resolve/build it automatically)

3. Build this folder w/ `mvn clean install`

4. Review the resulting update site in site/target/site/:

	firefox site/target/site/index.html

5. Load the site into Eclipse as a local repo, eg., 

	file:///path/to/site/target/site/


