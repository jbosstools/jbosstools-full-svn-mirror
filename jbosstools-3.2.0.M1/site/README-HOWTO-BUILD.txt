To build this update site:

  cd /tmp; \
  svn co http://anonsvn.jboss.org/repos/jbosstools/branches/modular_build/build/; \
  cd build; \
  ant

To build just the update site (assuming you've previously built the components):

  cd /tmp/build;
  ant create.overall.update.site -Dbuild.if.sources.unchanged=true


