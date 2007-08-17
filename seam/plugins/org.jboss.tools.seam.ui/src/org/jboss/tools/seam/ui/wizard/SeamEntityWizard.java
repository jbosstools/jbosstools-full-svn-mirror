/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.seam.ui.wizard;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.INewWizard;

/**
 * @author eskimo
 *
 */
public class SeamEntityWizard extends SeamBaseWizard implements INewWizard {

	/**
	 * 
	 */
	public SeamEntityWizard() {
		super(CREATE_SEAM_ENTITY);
		setWindowTitle("New Seam Entity");
		addPage(new SeamEntityWizardPage1());
	}

	
	// TODO move operations to core plugin
	public static final IUndoableOperation CREATE_SEAM_ENTITY = new SeamBaseOperation("Action creating operation"){
		
		public File getBeanFile(Map<String, Object> vars)  {
			return new File(getSeamFolder(vars),"src/ActionJavaBean.java");
		}
		
		public File getTestClassFile(Map<String, Object> vars) {
			return new File(getSeamFolder(vars),"test/ActionTest.java");
		}
		
		public File getTestngXmlFile(Map<String, Object> vars) {
			return new File(getSeamFolder(vars),"test/testng.xml");
		}
		
		public File getPageXhtml(Map<String, Object> vars) {
			return new File(getSeamFolder(vars),"view/action.xhtml");
		}

		@Override
		public List<String[]> getFileMappings(Map<String, Object> vars) {
			// TODO Auto-generated method stub
			return null;
		}
		
	};

}
