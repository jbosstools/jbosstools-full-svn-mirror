/*
 * Created on 2004-10-29 by max
 * 
 */
package org.hibernate.eclipse.console.actions;

import java.sql.DriverManager;
import java.util.Iterator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.StructuredViewer;
import org.hibernate.HibernateException;
import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.console.HibernateConsoleRuntimeException;
import org.hibernate.eclipse.console.HibernateConsolePlugin;
import org.hibernate.console.node.BaseNode;
import org.hibernate.console.node.ConfigurationNode;

/**
 * @author max
 *
 */
public class BuildSessionFactoryAction extends SessionFactoryBasedAction {

	private final StructuredViewer viewer;

	public BuildSessionFactoryAction(StructuredViewer viewer) {
		super("Build SessionFactory");
		this.viewer = viewer;
		setEnabledWhenNoSessionFactory(true);
	}
	
	public void run() {
		
		try {
			doRun();
		} catch(HibernateException he) {
			ErrorDialog.openError(null, "Hibernate Console error", "Problem while connecting. (" + he + ")", new Status(IStatus.ERROR, HibernateConsolePlugin.ID, IStatus.OK, "Could not connect", he));
		}        		
	}

	/**
	 * 
	 */
	private void doRun() {
		for (Iterator i = getSelectedNonResources().iterator(); i.hasNext();) {
        	try {
            BaseNode node = ((BaseNode) i.next());
            if(node instanceof ConfigurationNode) {
            	ConsoleConfiguration config = node.getConsoleConfiguration();
            	if(config.isSessionFactoryCreated()) {
            		config.reset();
            	} else {
            		//DriverManager.setLogStream(System.out);
            		config.build();
            		config.initSessionFactory();
            	}
            	updateState(config);
            }
			
            viewer.refresh(node); // todo: should we do it here or should the view just react to config being build ?
        	} catch(HibernateConsoleRuntimeException he) {
        		 HibernateConsolePlugin.showError(viewer.getControl().getShell(), "Exception while connecting/starting Hibernate",he);
        	}
        }
	}

	/**
	 * @param config
	 */
	protected boolean updateState(ConsoleConfiguration config) {
		setEnabledWhenNoSessionFactory(!config.isSessionFactoryCreated());
		if(enabledWhenNoSessionFactory) {
			setText("Create SessionFactory");
		} else {
			setText("Close SessionFactory");
		}
		return super.updateState(config);
	}
}
