/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.hibernate4_0.console;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.osgi.util.NLS;
import org.hibernate.annotations.common.util.ReflectHelper;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.JDBCMetaDataConfiguration;
import org.hibernate.cfg.JDBCReaderFactory;
import org.hibernate.cfg.Settings;
import org.hibernate.cfg.reveng.DefaultDatabaseCollector;
import org.hibernate.cfg.reveng.DefaultReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.JDBCReader;
import org.hibernate.cfg.reveng.OverrideRepository;
import org.hibernate.cfg.reveng.ReverseEngineeringSettings;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.console.HibernateConsoleRuntimeException;
import org.hibernate.console.KnownConfigurations;
import org.hibernate.console.execution.ExecutionContext;
import org.hibernate.console.execution.ExecutionContext.Command;
import org.hibernate.console.ext.HibernateException;
import org.hibernate.console.ext.HibernateExtension;
import org.hibernate.console.ext.api.ConsoleDatabaseCollector;
import org.hibernate.eclipse.console.HibernateConsoleMessages;
import org.hibernate.eclipse.console.HibernateConsolePlugin;
import org.hibernate.eclipse.console.ext.CompletionProposalsResult;
import org.hibernate.eclipse.console.ext.ConsoleExtension;
import org.hibernate.eclipse.console.workbench.ProgressListenerMonitor;
import org.hibernate.eclipse.launch.CodeGenerationStrings;
import org.hibernate.eclipse.launch.CodeGenerationUtils;
import org.hibernate.eclipse.launch.PathHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.hibernate.tool.hbm2x.ArtifactCollector;
import org.hibernate.tool.hbm2x.Exporter;
import org.hibernate.tool.ide.completion.HQLCodeAssist;
import org.hibernate.tool.ide.completion.IHQLCodeAssist;
import org.jboss.tools.hibernate4_0.HibernateExtension4_0;

/**
 * @author Dmitry Geraskov
 *
 */
public class ConsoleExtension4_0 implements ConsoleExtension {
	
	private HibernateExtension4_0 hibernateExtension;
	
	public ConsoleExtension4_0(){}
	
	public void setHibernateException(HibernateExtension hibernateExtension){
		this.hibernateExtension = (HibernateExtension4_0) hibernateExtension;
	}

	@Override
	public CompletionProposalsResult hqlCodeComplete(String query, int currentOffset) {
		EclipseHQLCompletionRequestor requestor = new EclipseHQLCompletionRequestor();
		if (!hibernateExtension.hasConfiguration()){
			try {
				hibernateExtension.build();
				hibernateExtension.execute( new ExecutionContext.Command() {
			 		public Object execute() {
			 			if(hibernateExtension.hasConfiguration()) {
			 				hibernateExtension.getConfiguration().buildMappings();
				 		}
			 			return null;
			 		}
				});
			} catch (HibernateException e){
				//FIXME
				//String mess = NLS.bind(HibernateConsoleMessages.CompletionHelper_error_could_not_build_cc, consoleConfiguration.getName());
				//HibernateConsolePlugin.getDefault().logErrorMessage(mess, e);
			}
		}
		IHQLCodeAssist hqlEval = new HQLCodeAssist(hibernateExtension.getConfiguration());
		query = query.replace('\t', ' ');
		hqlEval.codeComplete(query, currentOffset, requestor);
		return new CompletionProposalsResult(requestor.getCompletionProposals(), requestor.getLastErrorMessage());
	}

	/* (non-Javadoc)
	 * @see org.hibernate.eclipse.console.ext.ConsoleExtension#launchExporters(org.hibernate.eclipse.console.ext.ILaunchConfiguration, java.lang.String, org.hibernate.eclipse.console.ext.ILaunch, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void launchExporters(ILaunchConfiguration configuration,
			String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
		Assert.isNotNull(configuration);
		Assert.isNotNull(monitor);
		ExporterAttributes attributes = new ExporterAttributes(configuration);
		List<ExporterFactory> exporterFactories = attributes.getExporterFactories();
		for (Iterator<ExporterFactory> iter = exporterFactories.iterator(); iter.hasNext();) {
			ExporterFactory exFactory = iter.next();
			if (!exFactory.isEnabled(configuration)) {
				iter.remove();
			}
		}
		
		try {
		    Set<String> outputDirectories = new HashSet<String>();
		    ExporterFactory[] exporters = exporterFactories.toArray( new ExporterFactory[exporterFactories.size()] );
            ArtifactCollector collector = runExporters(attributes, exporters, outputDirectories, monitor);

            for (String path : outputDirectories) {
            	CodeGenerationUtils.refreshOutputDir(path);
			}

			//RefreshTab.refreshResources(configuration, monitor);

			// code formatting needs to happen *after* refresh to make sure eclipse will format the uptodate files!
            if(collector!=null) {
            	//formatGeneratedCode( monitor, collector );
			}


		} catch(Exception e) {
			throw new CoreException(HibernateConsolePlugin.throwableToStatus(e, 666));
		} catch(NoClassDefFoundError e) {
			throw new CoreException(HibernateConsolePlugin.throwableToStatus(new HibernateConsoleRuntimeException(HibernateConsoleMessages.CodeGenerationLaunchDelegate_received_noclassdeffounderror,e), 666));
		} finally {
			monitor.done();
		}
	}

	private ArtifactCollector runExporters (final ExporterAttributes attributes, final ExporterFactory[] exporterFactories, final Set<String> outputDirectories, final IProgressMonitor monitor)
			   throws CoreException
		    {

				 	monitor.beginTask(HibernateConsoleMessages.CodeGenerationLaunchDelegate_generating_code_for + attributes.getConsoleConfigurationName(), exporterFactories.length + 1);

					if (monitor.isCanceled())
						return null;

					ConsoleConfiguration cc = KnownConfigurations.getInstance().find(attributes.getConsoleConfigurationName());
					if (attributes.isReverseEngineer()) {
						monitor.subTask(HibernateConsoleMessages.CodeGenerationLaunchDelegate_reading_jdbc_metadata);
					}
					final Configuration cfg = buildConfiguration(attributes, (HibernateExtension4_0) cc.getHibernateExtension(), ResourcesPlugin.getWorkspace().getRoot());

					monitor.worked(1);

					if (monitor.isCanceled())
						return null;

					return (ArtifactCollector) hibernateExtension.execute(new Command() {

						public Object execute() {
							ArtifactCollector artifactCollector = new ArtifactCollector();

		                    // Global properties
			                Properties props = new Properties();
			                props.put(CodeGenerationStrings.EJB3, "" + attributes.isEJB3Enabled()); //$NON-NLS-1$
		                    props.put(CodeGenerationStrings.JDK5, "" + attributes.isJDK5Enabled()); //$NON-NLS-1$

		                    for (int i = 0; i < exporterFactories.length; i++)
		                    {
		                       monitor.subTask(exporterFactories[i].getExporterDefinition().getDescription());

		                       Properties globalProperties = new Properties();
		                       globalProperties.putAll(props);

		                       Exporter exporter;
							try {
								//FIXME - the exporters are not the same here!
								//may be need to create it using the data from exporter definition???
								exporter = exporterFactories[i].createConfiguredExporter(cfg, attributes.getOutputPath(), attributes.getTemplatePath(), globalProperties, outputDirectories, artifactCollector);
							} catch (CoreException e) {
								throw new HibernateConsoleRuntimeException(HibernateConsoleMessages.CodeGenerationLaunchDelegate_error_while_setting_up + exporterFactories[i].getExporterDefinition(), e);
							}

							try {
		                       exporter.start();
							} catch(HibernateException he) {
								throw new HibernateConsoleRuntimeException(HibernateConsoleMessages.CodeGenerationLaunchDelegate_error_while_running + exporterFactories[i].getExporterDefinition().getDescription(), he);
							}
		                       monitor.worked(1);
		                    }
							return artifactCollector;
						}

					});


				}
	
	
	private Configuration buildConfiguration(final ExporterAttributes attributes, HibernateExtension4_0 cc, IWorkspaceRoot root) {
		final boolean reveng = attributes.isReverseEngineer();
		final String reverseEngineeringStrategy = attributes.getRevengStrategy();
		final boolean preferBasicCompositeids = attributes.isPreferBasicCompositeIds();
		final IResource revengres = PathHelper.findMember( root, attributes.getRevengSettings());
		
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
					ReverseEngineeringStrategy res = new DefaultReverseEngineeringStrategy();

					OverrideRepository repository = null;
					
					if(revengres!=null) {
						File file = PathHelper.getLocation( revengres ).toFile();
						repository = new OverrideRepository();
						repository.addFile(file);						
					}
					
					if (repository != null){
						res = repository.getReverseEngineeringStrategy(res);
					}

					if(reverseEngineeringStrategy!=null && reverseEngineeringStrategy.trim().length()>0) {
						res = loadreverseEngineeringStrategy(reverseEngineeringStrategy, res);
					}

					ReverseEngineeringSettings qqsettings = new ReverseEngineeringSettings(res)
					.setDefaultPackageName(attributes.getPackageName())
					.setDetectManyToMany( attributes.detectManyToMany() )
					//.setDetectOneToOne( attributes.detectOneToOne() )
					.setDetectOptimisticLock( attributes.detectOptimisticLock() );

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
	@SuppressWarnings("unchecked")
	private ReverseEngineeringStrategy loadreverseEngineeringStrategy(final String className, ReverseEngineeringStrategy delegate) {
        try {
            Class<ReverseEngineeringStrategy> clazz = ReflectHelper.classForName(className);
			Constructor<ReverseEngineeringStrategy> constructor = clazz.getConstructor(new Class[] { ReverseEngineeringStrategy.class });
            return constructor.newInstance(new Object[] { delegate });
        }
        catch (NoSuchMethodException e) {
			try {
				Class<?> clazz = ReflectHelper.classForName(className);
				ReverseEngineeringStrategy rev = (ReverseEngineeringStrategy) clazz.newInstance();
				return rev;
			}
			catch (Exception eq) {
				String out = NLS.bind(HibernateConsoleMessages.CodeGenerationLaunchDelegate_could_not_create_or_find_with_default_noarg_constructor, className);
				throw new HibernateConsoleRuntimeException(out, eq);
			}
		}
        catch (Exception e) {
			String out = NLS.bind(HibernateConsoleMessages.CodeGenerationLaunchDelegate_could_not_create_or_find_with_one_argument_delegate_constructor, className);
			throw new HibernateConsoleRuntimeException(out, e);
		}
    }
	
	@Override
	public ConsoleDatabaseCollector readDatabaseSchema(final IProgressMonitor monitor) {
		return new ConsoleDatabaseCollectorImpl(readDatabaseSchemaInternal(monitor, new DefaultReverseEngineeringStrategy()));
	}
	
	protected DefaultDatabaseCollector readDatabaseSchemaInternal(final IProgressMonitor monitor, final ReverseEngineeringStrategy strategy) {
		final Configuration configuration = hibernateExtension.buildWith(null, false);
		return (DefaultDatabaseCollector) hibernateExtension.execute(new ExecutionContext.Command() {

			public Object execute() {
				ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
					.applySettings(configuration.getProperties())
					.buildServiceRegistry();
				DefaultDatabaseCollector db = null;
				Settings settings = hibernateExtension.getSettings(configuration, serviceRegistry);
				try {
					JDBCReader reader = JDBCReaderFactory.newJDBCReader(configuration.getProperties(), settings, strategy, serviceRegistry);
					db = new DefaultDatabaseCollector(reader.getMetaDataDialect());
					reader.readDatabaseSchema(db, settings.getDefaultCatalogName(), settings.getDefaultSchemaName(), new ProgressListenerMonitor(monitor));
				} catch (HibernateException he) {
					throw he;
				} catch (UnsupportedOperationException he) {
					throw new HibernateException(he);
				}
				finally {
					if (serviceRegistry instanceof ServiceRegistryImplementor) {
						((ServiceRegistryImplementor)serviceRegistry).destroy();
					}
				}
				return db;
			}
		});
	}

}
