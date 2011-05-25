This folder can be used to install locally-built JBoss Tools components combined with the latest available upstream components.

---

For example, if you built locally tests, common, jmx, archives, and as, you could, in order to test installation, point Eclipse at those 5 local URLs:

	file://path/to/trunk/tests/site/target/site/
	file://path/to/trunk/common/site/target/site/
	file://path/to/trunk/jmx/site/target/site/
	file://path/to/trunk/archives/site/target/site/
	file://path/to/trunk/as/site/target/site/

---

Or, you can use this local folder to pull from relative paths (and fall back to remote paths for any component not built locally). For locally-built features to be installed instead of remote ones, they must have been built more recently (newer timestamps).

	file:///path/to/trunk/build/aggregate/local-site/

See also https://issues.jboss.org/browse/JBIDE-8974

