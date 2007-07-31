To get project to compile:
==========================
*Check out the project
*Create a generated/ directory (next to bin/ and src/)
*Modify the build-xml-objects.sh/bat file
	JBOSS_AOP_ECLIPSE should point to the directory above the one containing this file
	JWSDP_HOME should point to the latest Java Web Services Developer Pack
*Run the build-xml-objects.sh/bat script
*To get your Eclipse build working you need to:
a) Make the JRE System library point to a JDK 1.4.2 distribution

	