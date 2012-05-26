Project Examples Readme
Last Update: bfitzpat - June 9, 2010 

The "nightly" folder contains project example files we want to test or aren't 
quite ready to push to the root yet.

The process currently is as follows (from Nick Boldt):

1) A developer updates one of the project examples files (in 
http://anonsvn.jboss.org/repos/jbosstools/workspace/examples or elsewhere).

2) The developer requests that the changed file be updated on the published 
examples site (http://download.jboss.org/jbosstools/examples/).

3) Nick or the developer copies the file (or files) to 
http://anonsvn.jboss.org/repos/jbosstools/trunk/download.jboss.org/jbosstools/examples/ or
http://anonsvn.jboss.org/repos/jbosstools/trunk/download.jboss.org/jbosstools/examples/nightly.

4) Nick then either manually moves the file(s) (currently) or the automated Hudson job 
(coming) takes the files from the area in step 3 to the final destination - 
http://download.jboss.org/jbosstools/examples/.

