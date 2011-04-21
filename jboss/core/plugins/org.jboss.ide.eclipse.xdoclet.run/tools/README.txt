Building the tasks and subtasks dictionnary
===========================================

The dictionnary is build like the Maven plugin of XDoclet. After XDoclet build,
the folder $XDOCLET_HOME/modules/build/all-src contains all the sources of the
XDoclet tree. The generation of the Maven plugin parses every task and subtask
source code to generate the jelly script, based on annotations.

The dictionnary generation is based on the replacement of the Maven template. For
that, a template is located in "org.jboss.ide.eclipse.xdoclet.run/tools/" under a
jar package named "maven-addendum.jar".

- Put the jar file into the $XDOCLET_HOME/target/lib with (it has a name that
makes it appearing before maven-xdoclet-pluginXXX.jar).
- Run the "ant maven" command in $XDOCLET_HOME. The Maven plugin will be
build with the JBoss-IDE dictionnary template.
- Go into the $XDOCLET_HOME/maven/target and pick up the "plugin.jelly" file.
- Rename it to "reference.xml"
- Replace the "reference.xml" file in the XDoclet runner plugin 
(in the "org.jboss.ide.eclipse.xdoclet.run/resources folder).

Note : this operation screws the XDoclet Maven plugin. To generate a real Maven
plugin, remove the jar that contains the JBoss-IDE template.
