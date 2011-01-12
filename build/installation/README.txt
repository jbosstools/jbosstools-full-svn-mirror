The scripts in this folder can be used to install JBoss Tools via commandline
into a new or existing Eclipse 3.6 installation.

.cmd is for Windows
.sh is for Mac or Linux

These files are simply wrappers which call Ant and run the .xml script, passing 
in variables for source, target, and what to install (if not everything). They 
contain examples of what you might want to do.

Last tested with Eclipse 3.6.2 (maintenance build) for linux 32-bit (Fedora 12, 
OpenJDK 6) and JBoss Tools 3.2.0.CR1 on Jan 11, 2011. First attempt was incomplete
due to timeout, but on second attempt install succeeded in 9 mins. Script used 
is below for reference. Resulting footprint on disk (including Eclipse): 784M

-- Nick Boldt (nboldt@redhat.com)

--- --- --- --- --- --- --- --- 

workspace=/home/nboldt/eclipse/workspace-clean36; \
target=/home/nboldt/eclipse/36clean; \
eclipse=/home/nboldt/tmp/Eclipse_Bundles/eclipse-SDK-M20110105-0951-linux-gtk.tar.gz; \
echo "Wipe $target/eclipse and $workspace ..."; rm -fr $target/eclipse $workspace; \
echo "Unpack $eclipse ..."; cd $target; tar xzf $eclipse; cd -; \
export GDK_NATIVE_WINDOWS=true; \
$target/eclipse/eclipse -clean -consolelog -nosplash -data $workspace \
	-application org.eclipse.ant.core.antRunner -f installJBossTools.xml \
	-DtargetDir=$target/eclipse \
	-DsourceZip=/tmp/jbosstools-3.2_trunk.aggregate-Update-SNAPSHOT.zip \
	-DotherRepos=\
http://download.jboss.org/jbosstools/updates/target-platform/latest/,\
http://download.eclipse.org/releases/helios/,\
http://download.eclipse.org/birt/update-site/2.6/,\
http://m2eclipse.sonatype.org/sites/m2e/,\
http://m2eclipse.sonatype.org/sites/m2e-extras/,\
http://subclipse.tigris.org/update_1.6.x,\
http://dl.google.com/eclipse/plugin/3.6/ \
	-vm /opt/jdk1.5.0_19/bin/java -vmargs -Xms128M \
	-Xmx256M -XX:PermSize=128M -XX:MaxPermSize=256M \
	2>&1 | tee "$target/eclipse.log.`date`.txt"
 
