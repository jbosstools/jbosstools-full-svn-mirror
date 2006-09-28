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
package org.jboss.ide.eclipse.firstrun;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.jboss.ide.eclipse.core.CorePlugin;
import org.jboss.ide.eclipse.firstrun.wizard.FirstRunWizard;

public class FirstRunStartup implements IStartup {

   public void earlyStartup() {

      try {
         Display.getDefault().asyncExec(new Runnable() {
            public void run() {
               try {
            	   // legacyMethod();
                   IPreferenceStore store = FirstRunPlugin.getDefault().getPreferenceStore();

            	   String currentVersion = CorePlugin.getCurrentVersion();
            	   String workspaceLatest = store.getString(FirstRunPlugin.FIRST_RUN_PROPERTY_LATEST_VERSION);
            	   
            	   // TODO: uncomment
            	   // short circuit if already done
            	   int compare = CorePlugin.compare(currentVersion, workspaceLatest);
            	   if( workspaceLatest != null && compare <= 0) return; 
            	   
            	   if( workspaceLatest == null ) {
            		   // this isn't set... are we at least at 1.6?
            		   boolean at16 = false;
            	       if (store.contains(FirstRunPlugin.FIRST_RUN_PROPERTY)) {
            	    	   at16 = store.getBoolean(FirstRunPlugin.FIRST_RUN_PROPERTY);
            	       }
            	       
            	       if( at16 ) {
            	    	   workspaceLatest = "1.6.0.GA";
            	       } else {
            	    	   // we're either pre-1.6, or its a new workspace
            	    	   workspaceLatest = FirstRunPlugin.NEW_WORKSPACE;
            	       }
            	   }
            	   
            	   showWizard(workspaceLatest);
            	   store.setValue(FirstRunPlugin.FIRST_RUN_PROPERTY_LATEST_VERSION, currentVersion);
               } catch (Exception e) {
                  e.printStackTrace();
               }
            }
         });
      }
      catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   
   private void showWizard(String workspaceLatest) {
       FirstRunWizard wizard = new FirstRunWizard(workspaceLatest);
       
       // short circuit if no pages
       if( wizard.numPages() == 0 ) return;
       
       WizardDialog dialog = new WizardDialog(null, wizard);
       dialog.open();
   }
}
