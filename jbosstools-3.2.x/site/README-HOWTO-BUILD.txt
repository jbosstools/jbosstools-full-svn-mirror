To build this update site using Tycho:

  cd .../trunk
  mvn3 clean install

To (re)build this update site using Tycho and just the pom.xml + site.xml (assuming you've previously built the contained components):

  cd .../trunk/site
  mvn3 clean install

--------------------

To build this update site using Ant and category.*.xml files:

  cd /tmp; \
  svn co http://anonsvn.jboss.org/repos/jbosstools/branches/modular_build/build/; \
  cd build; \
  ant

To build just the update site (assuming you've previously built the components):

  cd /tmp/build;
  ant create.overall.update.site -Dbuild.if.sources.unchanged=true

