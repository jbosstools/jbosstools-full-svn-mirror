@echo off

:: plugin-only install example - two plugins
c:\eclipse\36clean\eclipse\eclipse.exe -consolelog -nosplash -data c:\tmp ^
      -application org.eclipse.ant.core.antRunner ^
      -f installJBossTools.xml ^
	-DsourceZip=c:\eclipse\36clean\jbosstools-3.2.0.M2.aggregate-Update-2010-09-08_17-17-54-H243.zip ^
	-DtargetDir=c:\eclipse\36clean\eclipse ^
	-Dinstall=org.jboss.tools.jmx.core,org.jboss.tools.jmx.ui
	
:: simple install example - one feature
:: note that if the file is org.jboss.tools.jmx.feature_*.jar, the feature to install is org.jboss.tools.jmx.feature.feature.group
c:\eclipse\36clean\eclipse\eclipse.exe -consolelog -nosplash -data c:\tmp ^
      -application org.eclipse.ant.core.antRunner ^
      -f installJBossTools.xml ^
	-DsourceZip=c:\eclipse\36clean\jbosstools-3.2.0.M2.aggregate-Update-2010-09-08_17-17-54-H243.zip ^
	-DtargetDir=c:\eclipse\36clean\eclipse ^
	-Dinstall=org.jboss.tools.jmx.feature.feature.group

:: full install example - all available features
c:\eclipse\36clean\eclipse\eclipse.exe -consolelog -nosplash -data c:\tmp ^
      -application org.eclipse.ant.core.antRunner ^
      -f installJBossTools.xml ^
	-DsourceZip=c:\eclipse\36clean\jbosstools-3.2.0.M2.aggregate-Update-2010-09-08_17-17-54-H243.zip ^
	-DtargetDir=c:\eclipse\36clean\eclipse ^
	-DotherRepos=http://download.jboss.org/jbosstools/updates/target-platform/latest/,^
http://download.eclipse.org/releases/helios/,^
http://download.eclipse.org/birt/update-site/2.6/,^
http://m2eclipse.sonatype.org/sites/m2e/,^
http://m2eclipse.sonatype.org/sites/m2e-extras/,^
http://dl.google.com/eclipse/plugin/3.6/

:: more features; note that some require addition of -DotherRepos to resolve missing dependencies (ie., BIRT, Maven)
::   -Dinstall=org.drools.eclipse.feature.feature.group,org.drools.eclipse.task.feature.feature.group,org.guvnor.tools.feature.feature.group,\
:: org.hibernate.eclipse.feature.feature.group,org.jboss.ide.eclipse.archives.feature.feature.group,org.jboss.ide.eclipse.as.feature.feature.group,\
:: org.jboss.ide.eclipse.freemarker.feature.feature.group,\
:: org.jboss.tools.birt.feature.feature.group,org.jboss.tools.bpel.feature.feature.group,org.jboss.tools.esb.feature.feature.group,\
:: org.jboss.tools.flow.common.feature.feature.group,org.jboss.tools.flow.jpdl4.feature.feature.group,org.jboss.tools.jbpm.common.feature.feature.group,\
:: org.jboss.tools.jbpm.convert.feature.feature.group,org.jboss.tools.jbpm4.feature.feature.group,org.jboss.tools.jmx.feature.feature.group,\
:: org.jboss.tools.maven.feature.feature.group,org.jboss.tools.maven.seam.feature.feature.group,\
:: org.jboss.tools.portlet.feature.feature.group,org.jboss.tools.profiler.feature.feature.group,\
:: org.jboss.tools.project.examples.feature.feature.group,org.jboss.tools.richfaces.feature.feature.group,org.jboss.tools.seam.feature.feature.group,\
:: org.jboss.tools.smooks.feature.feature.group,org.jboss.tools.struts.feature.feature.group,org.jboss.tools.tptp.feature.feature.group,\
:: org.jboss.tools.ws.feature.feature.group,org.jboss.tools.xulrunner.feature.feature.group,org.jbpm.gd.jpdl.feature.feature.group
