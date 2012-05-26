Setting up Workingsets:

This directory contains 2 files which both can be used
for easily setting up working sets in Eclipse for JBoss Tools.

Both ways assumes you have already checked out JBoss tools plugins
in your workspace.

After you used one of the below methods you can in Package Explorer set Top Level Elements to Working Sets
to get the plugins grouped according to the workingset.

Method #1 (command-line)
========================
This replaces any existing workingset definition in your workspace (take a backup if you don't feel safe doing this ;)

cp workingsets.xml [workspace-with-jbosstoolsplugins]/.metadata/.plugins/org.eclipse.ui.workbench

Method #2 (use Eclipse import wizard)
=====================================
This will use AnyEdit plugin which provides an import wizard for Workingsets which should
merge with your existing workingsets.

1) Install AnyEdit from this update site: http://andrei.gmxhome.de/eclipse/
   (More info at: http://andrei.gmxhome.de/anyedit/links.html)

2) File > Import > General > Workingsets

3) Select jbosstools.wst

4) Press Finish



