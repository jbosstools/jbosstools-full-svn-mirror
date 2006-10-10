/*
 * JBoss, a division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
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
package org.jboss.ide.eclipse.firstrun.wizard;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.jboss.ide.eclipse.core.CorePlugin;
import org.jboss.ide.eclipse.firstrun.FirstRunMessages;
import org.jboss.ide.eclipse.firstrun.FirstRunPlugin;
import org.jboss.ide.eclipse.firstrun.wizard.pages.AbstractFirstRunPage;

public class FirstRunWizard extends Wizard {

	private String workspaceLatest;
	private FirstRunWizardPageConfigElement[] pageObjects;
	
	public FirstRunWizard(String workspaceLatest) {
		this.workspaceLatest = workspaceLatest;
		pageObjects = getExtensions();
	}

	public int numPages() {
		return pageObjects.length;
	}

   public boolean performFinish() {
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				int totalWork = pageObjects.length * 1000;
				String mainTaskName = FirstRunMessages.getString("ProgressMonitor.TaskName");
				monitor.beginTask(mainTaskName, totalWork);
				
				for( int i = 0; i < pageObjects.length; i++ ) {
					AbstractFirstRunPage page = pageObjects[i].getPage();
					monitor.setTaskName(mainTaskName); // reset task name
					try {
						SubProgressMonitor sub = new SubProgressMonitor(monitor, 1000);
						page.performFinishWithProgress(sub);
					} catch( Exception e ) {
						// do nothing and let the next page finish
					}
					
					// checking for cancelation
					if( monitor.isCanceled() ) 
						throw new InterruptedException();
				}
			}
		};
		try {
			new ProgressMonitorDialog(new Shell()).run(false, true, op);
		} catch( Exception e) {
			e.printStackTrace();
		}

	   
      return true;
   }

   public boolean canFinish() {
	   for( int i = 0; i < pageObjects.length; i++ ) {
		   if( pageObjects[i].getPage().isPageComplete() == false ) return false;
	   }
	   return true;
   }

   public void addPages() {
	   for( int i = 0; i < pageObjects.length; i++ ) {
		   AbstractFirstRunPage page = pageObjects[i].getPage();
		   page.initialize();
		   addPage(page);
	   }
   }
   
   protected FirstRunWizardPageConfigElement[] getExtensions() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] cf = registry.getConfigurationElementsFor(FirstRunPlugin.PLUGIN_ID, FirstRunPlugin.EXTENSION_WIZARD_PAGE);

		ArrayList tmp = new ArrayList();
		for( int i = 0; i < cf.length; i++ ) {
			FirstRunWizardPageConfigElement frwpce = new FirstRunWizardPageConfigElement(cf[i]);
			tmp.add(frwpce);
		}
		
		
		// Get rid of any that do not match the current workspace / previous workspace combo
		Iterator i = tmp.iterator();
		String currentVersion = CorePlugin.getCurrentVersion();
		while( i.hasNext()) {
			FirstRunWizardPageConfigElement e = (FirstRunWizardPageConfigElement)i.next();
			int previousMatch = 0;
			if( !workspaceLatest.equals(FirstRunPlugin.NEW_WORKSPACE)) 
				previousMatch = CorePlugin.compare(workspaceLatest, e.getFromVersion());
			int currentMatch = CorePlugin.compare(currentVersion, e.getToVersion());
			if( previousMatch != 0 || currentMatch != 0 ) {
				i.remove();
			}
		}
		
		// Now sort them based on weight
		Collections.sort(tmp, new Comparator() {
			public int compare(Object o1, Object o2) {
				if( o1 instanceof FirstRunWizardPageConfigElement 
						&& o2 instanceof FirstRunWizardPageConfigElement ) {
					return ((FirstRunWizardPageConfigElement)o1).getWeight() - ((FirstRunWizardPageConfigElement)o2).getWeight();
				}
				return 0;
			} 
		} );
		
	   return (FirstRunWizardPageConfigElement[]) tmp.toArray(new FirstRunWizardPageConfigElement[tmp.size()]);
   }
   
   private class FirstRunWizardPageConfigElement {
	   private static final String PAGE_KEY = "WizardPage";
	   private static final String FROM_KEY = "fromVersion";
	   private static final String TO_KEY = "toVersion";
	   private static final String WEIGHT_KEY = "weight";
	   private static final String ID_KEY = "id";
	   
	   private String id;
	   private String fromVersion;
	   private String toVersion;
	   private int weight;
	   private AbstractFirstRunPage page;
	   
	   private IConfigurationElement element;
	   
	   public FirstRunWizardPageConfigElement(IConfigurationElement element) {
		   this.element = element;
		   fromVersion = element.getAttribute(FROM_KEY);
		   toVersion = element.getAttribute(TO_KEY);
		   id  = element.getAttribute(ID_KEY);

		   try {
			   String weightString = element.getAttribute(WEIGHT_KEY);
			   if( weightString == null ) weight = 50;
			   else weight = Integer.parseInt(element.getAttribute(WEIGHT_KEY));
		   } catch( NumberFormatException nfe ) {
			   weight = 50;
		   }
	   }
	   
	   public AbstractFirstRunPage getPage() {
		   if( page == null ) {
			   try {
				   page = (AbstractFirstRunPage)element.createExecutableExtension(PAGE_KEY);
			   } catch( CoreException ce) {
				   ce.printStackTrace();
			   }
		   }
		   return page;
	   }

	public String getFromVersion() {
		return fromVersion;
	}

	public String getToVersion() {
		return toVersion;
	}

	public int getWeight() {
		return weight;
	}
   }

}