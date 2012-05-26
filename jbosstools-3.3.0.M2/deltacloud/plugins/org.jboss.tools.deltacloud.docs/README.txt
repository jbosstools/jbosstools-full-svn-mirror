This plug-in currently has a manually edited html file userguide/deltacloud.html
initially based upon the Wiki documentation found at:
 
http://community.jboss.org/wiki/DeltaCloudTools001UserGuide.

Ideally, this plug-in should use the Wiki to create the help docs via the
build-helper.xml file.  The build-helper.xml file in this project is not
correct for accessing the JBoss wiki, nor likely is the MediaWikiImageFetcher
which is for accessing a mediaWiki page.  JBoss Tools uses Confluence instead.
The build-helper.xml file requires changes so it will work with JBoss Tools and
should be modified to not refer to mediaWiki ant tasks.

The mylyn wikitext confluence plug-ins would need to be checked out locally
to make its ant tasks available and the build-helper.xml file would need to
reference these tasks as appropriate.  Apparently, there are tasks for mediaWiki
that don't have corresponding tasks set up in the Confluence plug-ins.

The rewrite to use mylyn wikitext is a future work item that will be done as
time permits.  For the time-being, edit the userguide/deltacloud.html file and
change the toc.xml file as new headings are added or removed.