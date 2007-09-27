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
package org.jboss.ide.eclipse.firstrun.wizard.pages;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

/**
 * This class represents a page in the firstRun wizard.
 * 
 * There are three types of pages.
 *   1) A page helping a user upgrade a plugin from one version to another.
 *   2) An introduction / conclusion page to be shown when there is at least one page of type 1
 *   3) A page to be shown only when zero pages of type 2 occur.
 *   
 * 
 *  For example: 
 *     1) BulkIntroPage          (  !hasPossibleChanges, shouldShow, !isDefaultPage  ) 
 *     2) BulkExitPage           (  !hasPossibleChanges, shouldShow, !isDefaultPage  )
 *     3) PackagingUpgradePage   (   hasPossibleChanges, ???, !isDefaultPage  )
 *     4) DefaultWelcomePage     (  ???, ??? isDefaultPage  ) 
 *     
 *  If PackagingUpgradePage.shouldShow() returns true, 
 *    BulkIntroPage, PackagingUpgradePage, and BulkExitPage will all be shown. 
 *    DefaultWelcomePage may or may not be shown depending on the value of shouldShow
 *  
 *  If PackagingUpgradePage.shouldShow() returns false, 
 *     ONLY DefaultWelcomePage will be shown.
 *     
 *     
 * @author rob.stryker@jboss.com
 */
public abstract class AbstractFirstRunPage extends WizardPage {

    protected AbstractFirstRunPage(String pageName, String title,
            ImageDescriptor titleImage) {
    	super(pageName, title, titleImage);
    }

    /* Lifecycle methods */
    public abstract void initialize();
	public abstract void createControl(Composite parent);
	public abstract void performFinishWithProgress(IProgressMonitor monitor);
	
	/**
	 * Indicates whether this page will make some change or upgrade. 
	 * Intro / finish pages should return false.
	 * 
	 * Other pages with upgradable content should return whether this workspace 
	 * has any possibility to upgrade. 
	 * @return
	 */
	public boolean hasPossibleChanges() {
		return true;
	}

	
	/**
	 * Should this page be shown even if it contains no upgrade information?
	 * This method allows an intro / finish page to be shown even though it
	 * will make no changes.
	 * 
	 * A page for packaging, for example, even if the workspace contains no 
	 * upgradable configurations, could still return true simply to inform the 
	 * user about the changes to the packaging plug-in. 
	 * 
	 * @return
	 */
	public boolean shouldShow() {
		return true;
	}
	
	
	/**
	 * This page is a page to show when zero pages MUST be shown.
	 * @return
	 */
	public boolean isDefaultPage() {
		return false;
	}
}
