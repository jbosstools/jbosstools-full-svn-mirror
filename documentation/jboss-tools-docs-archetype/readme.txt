jboss-tools-docs-archetype
The "jboss-tools-docs-archetype" archetype helps create a skeleton for a new guide.   To use it you need to take a few simple steps:
1.	Build the archetype in the current folder with "mvn install"
2.	Navigate to the folder with the new plugin for which you want to make a new guide.
3.	Create the docs folder. If the folder already exists just proceed to the next step. 
4.	CD to docs folder
5. Run mvn "archetype:generate -DarchetypeGroupId=org.jboss.tools -DarchetypeArtifactId=jboss-tools-docs-archetype -DarchetypeVersion=1.0-SNAPSHOT -DartifactId=XXX_Guide -DgroupId=org.jboss.tools"
6.	That's it. A new guide is created. You can build it (CD to XXX_Guide directory and run "mvn install" ) to see that everything's ok. 
