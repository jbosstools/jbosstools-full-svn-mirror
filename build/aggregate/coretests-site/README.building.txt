1. First, build the various components referenced in site.xml (or pull them from upstream previous builds hosted on http://download.jboss.org/jbosstools/builds/staging/)

Components are here:

    http://anonsvn.jboss.org/repos/jbosstools/trunk/

2. Next, ensure the parent pom in ../../parent/pom.xml has been built w/ `mvn clean install` (this may not be required as the next step should resolve/build it automatically)

3. Build this folder w/ `mvn clean install`

4. Review the resulting update site in site/target/site/:

	firefox site/target/site/index.html

5. Load the site into Eclipse as a local repo, eg., 

	file:///path/to/site/target/site/


