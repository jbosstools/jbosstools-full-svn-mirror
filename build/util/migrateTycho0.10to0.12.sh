## Script that does the necessary changes to migrate from Tycho <=0.11 to Tycho >= 0.12
## Output will contain number of changes and file name.
##
## Usage:
## 11to12.sh pom.xml
##
## For multi-module projects you can do:
## find . -name pom.xml -exec sh 11to12.sh {} \;
##

POM=$1

cp $POM $POM.11
perl -pi -w -e 's/org.sonatype.tycho/org.eclipse.tycho/g;' $POM
perl -pi -w -e 's/maven-osgi-test-plugin/tycho-surefire-plugin/g;' $POM
perl -pi -w -e 's/maven-osgi-source-plugin/tycho-source-plugin/g;' $POM
perl -pi -w -e 's/maven-osgi-compiler-plugin/tycho-compiler-plugin/g;' $POM
perl -pi -w -e 's/maven-osgi-packaging-plugin/tycho-packaging-plugin/g;' $POM
perl -pi -w -e 's/maven-tycho-plugin/tycho-pomgenerator-plugin/g;' $POM
CHANGES=`comm -3 $POM $POM.11 | wc -l`

if [ $CHANGES == 0 ]; then
rm $POM.11
else
echo $CHANGES changes in $POM
    rm $POM.11
fi
