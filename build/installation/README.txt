The scripts in this folder can be used to install JBoss Tools via commandline
into a new or existing Eclipse 3.6 installation.

.cmd is for Windows
.sh is for Mac or Linux

These files are simply wrappers which call Ant and run the .xml script, passing 
in variables for source, target, and what to install (if not everything). They 
contain examples of what you might want to do.

Last tested with Eclipse 3.6 for linux 32-bit (Fedora 12, OpenJDK 6) and JBoss 
Tools 3.2.0.M2 on Sep 10, 2010. First attempt yielded network timeouts, but on
second attempt install succeeded.

-- Nick Boldt (nboldt@redhat.com)
 