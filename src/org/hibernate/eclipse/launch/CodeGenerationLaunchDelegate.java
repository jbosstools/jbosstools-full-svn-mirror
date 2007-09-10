/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.hibernate.eclipse.launch;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.manipulation.FileBufferOperationRunner;
import org.eclipse.core.filebuffers.manipulation.MultiTextEditWithProgress;
import org.eclipse.core.filebuffers.manipulation.TextFileBufferOperation;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;
import org.eclipse.debug.ui.RefreshTab;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.DocumentRewriteSessionType;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.util.Assert;
import org.eclipse.text.edits.TextEdit;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.JDBCMetaDataConfiguration;
import org.hibernate.cfg.reveng.DefaultReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.OverrideRepository;
import org.hibernate.cfg.reveng.ReverseEngineeringSettings;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.console.HibernateConsoleRuntimeException;
import org.hibernate.console.KnownConfigurations;
import org.hibernate.console.execution.ExecutionContext.Command;
import org.hibernate.eclipse.console.HibernateConsolePlugin;
import org.hibernate.eclipse.console.model.impl.ExporterFactory;
import org.hibernate.tool.hbm2x.ArtifactCollector;
import org.hibernate.tool.hbm2x.Exporter;
import org.hibernate.util.ReflectHelper;
import org.hibernate.util.StringHelper;

public class CodeGenerationLaunchDelegate extends
		LaunchConfigurationDelegate {

	private static final class FormatGeneratedCode extends TextFileBufferOperation {
		private FormatGeneratedCode(String name) {
			super( name );
		}

		protected DocumentRewriteSessionType getDocumentRewriteSessionType() {
			return DocumentRewriteSessionType.SEQUENTIAL;
		}

		protected MultiTextEditWithProgress computeTextEdit(
				ITextFileBuffer textFileBuffer, IProgressMonitor progressMonitor)
		throws CoreException, OperationCanceledException {
			
			IResource bufferRes = ResourcesPlugin.getWorkspace().getRoot().findMember(textFileBuffer.getLocation());
			Map options = null;
			if(bufferRes!=null) {
				IJavaProject project = JavaCore.create(bufferRes.getProject());
				options = project.getOptions(true);																	
			}
			
			CodeFormatter codeFormatter = ToolFactory.createCodeFormatter(options);
			
			IDocument document = textFileBuffer.getDocument();
			String string = document.get();
			TextEdit edit = codeFormatter.format(CodeFormatter.K_COMPILATION_UNIT, string, 0, string.length(), 0, null);
			MultiTextEditWithProgress multiTextEditWithProgress = new MultiTextEditWithProgress(getOperationName());
			if(edit==null) {
				//HibernateConsolePlugin.getDefault().log("empty format for " + textFileBuffer.getLocation().toOSString());
			} else {														
				multiTextEditWithProgress.addChild(edit);
			}
			return multiTextEditWithProgress;
		}
	}

    
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		Assert.isNotNull(configuration);
		Assert.isNotNull(monitor);
		try {
		    ExporterAttributes attributes = new ExporterAttributes(configuration);
            
		    List exporterFactories = attributes.getExporterFactories();
		    for (Iterator iter = exporterFactories.iterator(); iter.hasNext();) {
				ExporterFactory exFactory = (ExporterFactory) iter.next();
				if(!exFactory.isEnabled(configuration)) {
					iter.remove();
				}
			}
            
		    ExporterFactory[] exporters = (ExporterFactory[]) exporterFactories.toArray( new ExporterFactory[exporterFactories.size()] );
            ArtifactCollector collector = runExporters(attributes, exporters, monitor);
			refreshOutputDir( attributes.getOutputPath() );

			if(collector==null) {
				return;
			}
			
			formatGeneratedCode( monitor, collector );
			
			RefreshTab.refreshResources(configuration, monitor);
			
		} catch(Exception e) {
			throw new CoreException(HibernateConsolePlugin.throwableToStatus(e, 666)); 
		} catch(NoClassDefFoundError e) {
			throw new CoreException(HibernateConsolePlugin.throwableToStatus(new HibernateConsoleRuntimeException("Received a NoClassDefFoundError, probably the console configuration classpath is incomplete or contains conflicting versions of the same class",e), 666));
		} finally {
			monitor.done();
		} 
		
	}

	private void formatGeneratedCode(IProgressMonitor monitor, ArtifactCollector collector) {
		final TextFileBufferOperation operation = new FormatGeneratedCode( "java-artifact-format" );

		File[] javaFiles = collector.getFiles("java");
		if(javaFiles.length>0) {
			IPath[] locations = new IPath[javaFiles.length];
			
			for (int i = 0; i < javaFiles.length; i++) {
				File file = javaFiles[i];
				locations[i] = new Path(file.getPath());
			}
			
			FileBufferOperationRunner runner= new FileBufferOperationRunner(FileBuffers.getTextFileBufferManager(), HibernateConsolePlugin.getShell());
			try {
				runner.execute(locations, operation, monitor);
			}
			catch (OperationCanceledException e) {
				HibernateConsolePlugin.getDefault().logErrorMessage("java format cancelled", e);
			}
			catch (CoreException e) {
				HibernateConsolePlugin.getDefault().logErrorMessage("exception during java format", e);
			} catch (Throwable e) { // full guard since the above operation seem to be able to fail with IllegalArugmentException and SWT Invalid thread access while users are editing. 
				HibernateConsolePlugin.getDefault().logErrorMessage("exception during java format", e);
			}
		}
	}

	private void refreshOutputDir(String outputdir) {
		IResource bufferRes = findMember(ResourcesPlugin.getWorkspace().getRoot(), outputdir);
		
		if (bufferRes != null && bufferRes.isAccessible()) {
			try {
				bufferRes.refreshLocal(IResource.DEPTH_INFINITE, null);
			} catch (CoreException e) {
				//ignore, maybe merge into possible existing status.
			}
		}
	}
	
	private Path pathOrNull(String p) {
		if(p==null || p.trim().length()==0) {
			return null;
		} else {
			return new Path(p);
		}
	}

	private ArtifactCollector runExporters (final ExporterAttributes attributes, final ExporterFactory[] exporters, final IProgressMonitor monitor)
	   throws CoreException
    {
			
		 	monitor.beginTask("Generating code for " + attributes.getConsoleConfigurationName(), exporters.length + 1);
		
			if (monitor.isCanceled())
				return null;
			
			
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			final IResource resource = findMember( root, attributes.getOutputPath() );
	        final IResource templateres = findMember(root, attributes.getTemplatePath());
	        String templatePath = null;
	        
	        if (new File(attributes.getTemplatePath()).exists())
	        {
	        	templatePath = attributes.getTemplatePath();
	        }
			
			/*if (!resource.exists() || !(resource instanceof IContainer) ) {
				throwCoreException("Output directory \"" + configName + "\" does not exist.");
			}*/
			/*IContainer container = (IContainer) resource;*/

			ConsoleConfiguration cc = KnownConfigurations.getInstance().find(attributes.getConsoleConfigurationName());
			if (attributes.isReverseEngineer()) {
				monitor.subTask("reading jdbc metadata");
			}
			final Configuration cfg = buildConfiguration(attributes, cc, root);
			
			monitor.worked(1);
			
			if (monitor.isCanceled())
				return null;
			
			final String finalTemplatePath = templatePath;
			return (ArtifactCollector) cc.execute(new Command() {
				private ArtifactCollector artifactCollector = new ArtifactCollector();

				public Object execute() {
					File outputdir = getLocation( resource ).toFile(); 
					
	                String[] templatePaths = new String[0];
	        
	                if(templateres!=null) {
	                    templatePaths = new String[] { getLocation( templateres ).toOSString() }; // TODO: this should not be..should it ?
	                } else if (finalTemplatePath != null) {
	                	templatePaths = new String[] { finalTemplatePath };
	                }
	                
                    // Global properties
	                Properties props = new Properties();
	                props.put("ejb3", ""+attributes.isEJB3Enabled());
                    props.put("jdk5", ""+attributes.isJDK5Enabled());
                    
                    for (int i = 0; i < exporters.length; i++)
                    {
                       monitor.subTask(exporters[i].getExporterDefinition().getDescription());
                       
                       Properties exporterProperties = new Properties();
                       exporterProperties.putAll(props);
                       exporterProperties.putAll(exporters[i].getProperties());
                       
                       Exporter exporter = exporters[i].getExporterDefinition().createExporterInstance();
                       
                       configureExporter (cfg, outputdir, templatePaths, exporterProperties, exporter);
                       
                       exporter.start();
                       monitor.worked(1);
                    }
					return getArtififactCollector();
				}

				private void configureExporter(final Configuration cfg, File outputdir, String[] templatePaths, Properties props, Exporter exporter) {
					exporter.setProperties(props);
					exporter.setOutputDirectory(outputdir);
					exporter.setConfiguration(cfg);
					exporter.setTemplatePath(templatePaths);
					exporter.setArtifactCollector(getArtififactCollector());
				}

				private ArtifactCollector getArtififactCollector() {
					return artifactCollector ;
				}
			});
			
			
		}

	private IResource findMember(IWorkspaceRoot root, String path) {
		Path pathOrNull = pathOrNull(path);
		if(pathOrNull==null) return null;
		return root.findMember(pathOrNull);
	}

	private IPath getLocation(final IResource resource) {		
		if (resource.getRawLocation() == null) { 
			return resource.getLocation(); 
		} 
		else return resource.getRawLocation();  
	}

	private Configuration buildConfiguration(final ExporterAttributes attributes, ConsoleConfiguration cc, IWorkspaceRoot root) {
		final boolean reveng = attributes.isReverseEngineer();		
		final String reverseEngineeringStrategy = attributes.getRevengStrategy();
		final boolean preferBasicCompositeids = attributes.isPreferBasicCompositeIds();
		final IResource revengres = findMember( root, attributes.getRevengSettings());
		
		if(reveng) {
			Configuration configuration = null;
			if(cc.hasConfiguration()) {
				configuration = cc.getConfiguration();
			} else {
				configuration = cc.buildWith( null, false );
			}
			
			final JDBCMetaDataConfiguration cfg = new JDBCMetaDataConfiguration();
			Properties properties = configuration.getProperties();
			cfg.setProperties( properties );
			cc.buildWith(cfg,false);
			
			cfg.setPreferBasicCompositeIds(preferBasicCompositeids);
            
			cc.execute(new Command() { // need to execute in the consoleconfiguration to let it handle classpath stuff!

				public Object execute() {
			
					
					//todo: factor this setup of revengstrategy to core				
					DefaultReverseEngineeringStrategy configurableNamingStrategy = new DefaultReverseEngineeringStrategy();
					//configurableNamingStrategy.setSettings(qqsettings);
					
					ReverseEngineeringStrategy res = configurableNamingStrategy;
					if(revengres!=null) {
						/*Configuration configuration = cc.buildWith(new Configuration(), false);*/				
						/*Settings settings = cc.getSettings(configuration);*/
						File file = getLocation( revengres ).toFile();
						OverrideRepository repository = new OverrideRepository();///*settings.getDefaultCatalogName(),settings.getDefaultSchemaName()*/);
						repository.addFile(file);
						res = repository.getReverseEngineeringStrategy(res);
					}
										 
					if(reverseEngineeringStrategy!=null && reverseEngineeringStrategy.trim().length()>0) {
						res = loadreverseEngineeringStrategy(reverseEngineeringStrategy, res);						
					}
					
					ReverseEngineeringSettings qqsettings = new ReverseEngineeringSettings(res)
					.setDefaultPackageName(attributes.getPackageName())
					.setDetectManyToMany( attributes.detectManyToMany() )
					.setDetectOptimisticLock( attributes.detectOptimisticLock() );

					configurableNamingStrategy.setSettings( qqsettings );
					res.setSettings(qqsettings);
					
					cfg.setReverseEngineeringStrategy( res );
					
					cfg.readFromJDBC();
                    cfg.buildMappings();
					return null;
				}
			});	
			
			return cfg;
		} else {
			cc.build();
			final Configuration configuration = cc.getConfiguration();
			
			cc.execute(new Command() {
				public Object execute() {
					
					configuration.buildMappings();
					return configuration;		
				}
			});
			return configuration;
		}
	}

	// TODO: merge with revstrategy load in JDBCConfigurationTask
	private ReverseEngineeringStrategy loadreverseEngineeringStrategy(final String className, ReverseEngineeringStrategy delegate) {
        try {
            Class clazz = ReflectHelper.classForName(className);			
			Constructor constructor = clazz.getConstructor(new Class[] { ReverseEngineeringStrategy.class });
            return (ReverseEngineeringStrategy) constructor.newInstance(new Object[] { delegate }); 
        } 
        catch (NoSuchMethodException e) {
			try {
				Class clazz = ReflectHelper.classForName(className);						
				ReverseEngineeringStrategy rev = (ReverseEngineeringStrategy) clazz.newInstance();
				return rev;
			} 
			catch (Exception eq) {
				throw new HibernateConsoleRuntimeException("Could not create or find " + className + " with default no-arg constructor", eq);
			}
		} 
        catch (Exception e) {
			throw new HibernateConsoleRuntimeException("Could not create or find " + className + " with one argument delegate constructor", e);
		} 
    }
	
	public boolean preLaunchCheck(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor) throws CoreException {
		ExporterAttributes attributes = new ExporterAttributes(configuration);
		
		String configName = attributes.getConsoleConfigurationName();
		if(StringHelper.isEmpty( configName )) {
			abort("Console configuration name is empty in " + configuration.getName(), null, ICodeGenerationLaunchConstants.ERR_UNSPECIFIED_CONSOLE_CONFIGURATION);
		}
		
		if(KnownConfigurations.getInstance().find( configName )==null) {
			abort("Console configuration " + configName + " not found in " + configuration.getName(), null, ICodeGenerationLaunchConstants.ERR_CONSOLE_CONFIGURATION_NOTFOUND);
		}
		
		if(StringHelper.isEmpty(attributes.getOutputPath())) {
			abort("Output has to be specified in " + configuration.getName(), null, ICodeGenerationLaunchConstants.ERR_OUTPUT_PATH_NOTFOUND);
		}
		
		return super.preLaunchCheck( configuration, mode, monitor );
	}
	
	protected void abort(String message, Throwable exception, int code)
	throws CoreException {
		throw new CoreException(new Status(IStatus.ERROR, HibernateConsolePlugin.getDefault().ID, code, message, exception));
	}
}
