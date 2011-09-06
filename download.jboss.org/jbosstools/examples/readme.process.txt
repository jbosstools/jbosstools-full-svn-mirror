Project Examples Readme

Last Update: nboldt, 2011-0906

The process currently is as follows (from Nick Boldt):

To push files into http://download.jboss.org/jbosstools/examples/:

1) Commit changes to https://svn.jboss.org/repos/jbosstools/trunk/download.jboss.org/jbosstools/examples/

2) Wait up to 6 hrs, or manually kick this job:

3) http://hudson.qa.jboss.com/hudson/view/DevStudio/view/DevStudio_All/job/jbosstools-download.jboss.org-rsync-from-svn/

-----

Note: the "nightly" folder contains project example files we want to test or aren't quite ready to push to the root yet.


