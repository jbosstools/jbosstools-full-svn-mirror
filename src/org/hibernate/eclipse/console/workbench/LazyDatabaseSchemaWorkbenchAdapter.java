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
package org.hibernate.eclipse.console.workbench;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.JDBCReaderFactory;
import org.hibernate.cfg.Settings;
import org.hibernate.cfg.reveng.DefaultDatabaseCollector;
import org.hibernate.cfg.reveng.JDBCReader;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.console.ImageConstants;
import org.hibernate.console.execution.ExecutionContext;
import org.hibernate.eclipse.console.HibernateConsolePlugin;
import org.hibernate.eclipse.console.utils.EclipseImages;

public class LazyDatabaseSchemaWorkbenchAdapter extends BasicWorkbenchAdapter {
	
	public Object[] getChildren(Object o) {
		return getChildren(o, new NullProgressMonitor());
	}
	
	public Object[] getChildren(Object o, final IProgressMonitor monitor) {
		LazyDatabaseSchema dbs = getLazyDatabaseSchema( o );
		final DefaultDatabaseCollector db = new DefaultDatabaseCollector();
		
		ConsoleConfiguration consoleConfiguration = dbs.getConsoleConfiguration();
		try{
			readDatabaseSchema(monitor, db, consoleConfiguration, dbs.getReverseEngineeringStrategy());
			
			List result = new ArrayList();
			
			Iterator qualifierEntries = db.getQualifierEntries();
			while ( qualifierEntries.hasNext() ) {
				Map.Entry entry = (Map.Entry) qualifierEntries.next();
				result.add(new TableContainer((String) entry.getKey(),(List)entry.getValue()));
			}
			return toArray(result.iterator(), TableContainer.class, new Comparator() {
			
				public int compare(Object arg0, Object arg1) {
					
					return ((TableContainer)arg0).getName().compareTo(((TableContainer)arg1).getName());
				}
			
			});
		} catch (HibernateException e){
			HibernateConsolePlugin.getDefault().logErrorMessage("Problems while reading database schema", e);			
			return new Object[]{"<Reading schema error: " + e.getMessage() + ">"};
		}
		
	}

	private LazyDatabaseSchema getLazyDatabaseSchema(Object o) {
		return (LazyDatabaseSchema) o;
	}

	public ImageDescriptor getImageDescriptor(Object object) {
		return EclipseImages.getImageDescriptor(ImageConstants.TABLE);
	}

	public String getLabel(Object o) {
		return "Database";
	}

	public Object getParent(Object o) {
		return getLazyDatabaseSchema(o).getConsoleConfiguration();
	}
	
	protected void readDatabaseSchema(final IProgressMonitor monitor, final DefaultDatabaseCollector db, ConsoleConfiguration consoleConfiguration, final ReverseEngineeringStrategy strategy) {
		final Configuration configuration = consoleConfiguration.buildWith(null, false);
		
		consoleConfiguration.getExecutionContext().execute(new ExecutionContext.Command() {
			
			public Object execute() {
				Settings settings = configuration.buildSettings();
				ConnectionProvider connectionProvider = null;
				try {
					connectionProvider = settings.getConnectionProvider();
				
					JDBCReader reader = JDBCReaderFactory.newJDBCReader(configuration.getProperties(), settings, strategy);
					reader.readDatabaseSchema(db, settings.getDefaultCatalogName(), settings.getDefaultSchemaName(), new ProgressListenerMonitor(monitor));
				} catch(HibernateException he) {
					HibernateConsolePlugin.getDefault().logErrorMessage("Problem while reading database schema", he);
					return new Object[] { "<Schema not available>"};
				}
			    finally {
					if (connectionProvider!=null) {
						connectionProvider.close();
					}				
				}
							
				return null;
			}
		});
	}

}
