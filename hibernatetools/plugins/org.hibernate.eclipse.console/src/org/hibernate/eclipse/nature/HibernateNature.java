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
package org.hibernate.eclipse.nature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.hibernate.cfg.JDBCMetaDataConfiguration;
import org.hibernate.cfg.reveng.TableIdentifier;
import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.console.KnownConfigurations;
import org.hibernate.console.execution.ExecutionContext.Command;
import org.hibernate.eclipse.builder.HibernateBuilder;
import org.hibernate.eclipse.console.HibernateConsolePlugin;
import org.hibernate.mapping.Table;
import org.osgi.service.prefs.Preferences;

public class HibernateNature implements IProjectNature {

	final public static String ID = "org.hibernate.eclipse.console.hibernateNature";
	
	private IProject project;

	public void configure() throws CoreException {
		HibernateConsolePlugin.getDefault().log("Configuring " + project + " as a Hibernate project");
		
		IProjectDescription desc = project.getDescription();
		   ICommand[] commands = desc.getBuildSpec();
		   boolean found = false;

		   for (int i = 0; i < commands.length; ++i) {
		      if (commands[i].getBuilderName().equals(HibernateBuilder.BUILDER_ID) ) {
		         found = true;
		         break;
		      }
		   }
		   if (!found) { 
		      //add builder to project
		      ICommand command = desc.newCommand();
		      command.setBuilderName(HibernateBuilder.BUILDER_ID);
		      ArrayList list = new ArrayList();
		      list.addAll(Arrays.asList(commands) );
		      list.add(command);
		      desc.setBuildSpec( (ICommand[])list.toArray(new ICommand[]{}) );
		      project.setDescription(desc, new NullProgressMonitor() );
		   }
	}

	public void deconfigure() throws CoreException {
		HibernateConsolePlugin.getDefault().log("Deconfiguring " + project + " as a Hibernate project");
	}

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {		
		this.project = project;
	}

	public ConsoleConfiguration getDefaultConsoleConfiguration() {
			String cfg = getDefaultConsoleConfigurationName();
			ConsoleConfiguration configuration = KnownConfigurations.getInstance().find(cfg);
			return configuration;								
	}
	
	public String getDefaultConsoleConfigurationName() {
		IJavaProject prj = JavaCore.create(project);
		IScopeContext scope = new ProjectScope(prj.getProject() );
		
		Preferences node = scope.getNode("org.hibernate.eclipse.console");
		
		if(node!=null) {
			String cfg = node.get("default.configuration", prj.getProject().getName() );
			return cfg;						
		} else {
			return null;
		}
	}
	
	List tables = null;

	private ReadDatabaseMetaData job;
	
	public List getTables() {
		ConsoleConfiguration ccfg = getDefaultConsoleConfiguration();
		if(ccfg==null) return Collections.EMPTY_LIST;
		
		if(tables!=null) {
			return tables;
		} else {
			if(job==null) {
				job = new ReadDatabaseMetaData(ccfg);
				job.setPriority(Job.DECORATE);
				job.schedule();
			} else if(job.getState()==Job.NONE) {
				job.schedule();
			}
			return Collections.EMPTY_LIST;
		}
	}
	
	public class ReadDatabaseMetaData extends Job {
		
		private ConsoleConfiguration ccfg;

		public ReadDatabaseMetaData(ConsoleConfiguration ccfg) {
			super("Reading database metadata for " + getProject().getName() );
			this.ccfg = ccfg;
		}
		
		protected IStatus run(IProgressMonitor monitor) {
			final JDBCMetaDataConfiguration jcfg = (JDBCMetaDataConfiguration) ccfg.buildWith(new JDBCMetaDataConfiguration(), false);
			monitor.beginTask("Reading database metadata", IProgressMonitor.UNKNOWN);
			try {
				ccfg.execute(new Command() {
					public Object execute() {
						jcfg.readFromJDBC();
						return null;
					}
				});
				
				
				List result = new ArrayList();
				Iterator tabs = jcfg.getTableMappings();
				
				while (tabs.hasNext() ) {
					Table table = (Table) tabs.next();
					monitor.subTask(table.getName() );
					result.add(table);
				}
				
				tables = result;
				monitor.done();
				return Status.OK_STATUS;
			} catch(Throwable t) {
				return new Status(IStatus.ERROR, HibernateConsolePlugin.ID, 1, "Error while performing background reading of database schema", t); 
			}
		}			

	}

	public List getMatchingTables(String tableName) {
		List result = new ArrayList();
		Iterator tableMappings = getTables().iterator();
		while (tableMappings.hasNext() ) {
			Table table = (Table) tableMappings.next();
			if(table.getName().toUpperCase().startsWith(tableName.toUpperCase()) ) {
				result.add(table);
			}
		}
		return result;
	}

	public Table getTable(TableIdentifier nearestTableName) { 
		// TODO: can be made MUCH more efficient with proper indexing of the tables.
		// TODO: handle catalog/schema properly
		Iterator tableMappings = getTables().iterator();
		while (tableMappings.hasNext() ) {
			Table table = (Table) tableMappings.next();
			if(nearestTableName.getName().equals(table.getName())) {
				return table;
			}
		}
		return null;
	}
	
	/** return HibernateNature or null for a project **/
	public static HibernateNature getHibernateNature(IJavaProject project) {
		try {
			if(project!=null && project.getProject().hasNature(HibernateNature.ID)) {
				final HibernateNature nature = (HibernateNature) project.getProject().getNature(HibernateNature.ID);
				return nature;
			}
		}
		catch (CoreException e) {
			HibernateConsolePlugin.getDefault().logErrorMessage( "Exception when trying to locate Hibernate Nature", e );
		}		
		return null;
	}	

}
