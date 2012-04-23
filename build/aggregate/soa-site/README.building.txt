1. First, build components in external repos:

    Eclipse BPEL Editor: http://git.eclipse.org/c/bpel/org.eclipse.bpel.git/

    bpmn2: http://git.eclipse.org/c/bpmn2-modeler/org.eclipse.bpmn2-modeler.git/

    Drools, Guvnor & jBPM5: https://github.com/droolsjbpm/droolsjbpm-tools

    pi4soa: https://pi4soa.svn.sourceforge.net/svnroot/pi4soa

    Savara: https://github.com/savara/savara-tools-eclipse AND https://github.com/scribble/scribble-tools-eclipse

    SwitchYard: https://github.com/jboss-switchyard/tools/

    Teiid Designer: http://anonsvn.jboss.org/repos/tdesigner/trunk/

2. Next, build the various components referenced in site.xml (or pull them from upstream previous builds hosted on http://download.jboss.org/jbosstools/builds/staging/)

    Central Discovery: http://anonsvn.jboss.org/repos/jbosstools/trunk/build/aggregate/soa-site/

    ESB: http://anonsvn.jboss.org/repos/jbosstools/trunk/esb/

    JBoss BPEL Editor: http://anonsvn.jboss.org/repos/jbosstools/trunk/bpel/

    jBPM3 & 4: http://anonsvn.jboss.org/repos/jbosstools/trunk/jbpm/
               http://anonsvn.jboss.org/repos/jbosstools/trunk/flow/

    Modeshape: http://anonsvn.jboss.org/repos/jbosstools/trunk/modeshape/

    Runtime Detection: http://anonsvn.jboss.org/repos/jbosstools/trunk/runtime-soa/

3. Build this folder w/ `mvn clean install`

4. Review the resulting update site in site/target/site/:

	firefox site/target/site/index.html

5. Load the site into Eclipse as a local repo, eg., 

	file:///path/to/site/target/site/

Note that there is a second JBoss Central discovery plugin for use with JBDS. It can be built from source here:

    https://svn.jboss.org/repos/devstudio/trunk/product-soa/
