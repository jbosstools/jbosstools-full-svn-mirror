/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.hibernate.eclipse.console.test.mappingproject;

import junit.framework.TestCase;

import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.console.KnownConfigurations;
import org.hibernate.console.stubs.ConfigurationStub;
import org.hibernate.console.stubs.PersistentClassStub;
import org.hibernate.eclipse.console.test.ConsoleTestMessages;
import org.hibernate.eclipse.console.test.utils.Utils;
import org.hibernate.eclipse.console.workbench.ConfigurationWorkbenchAdapter;
import org.hibernate.eclipse.console.workbench.ConsoleConfigurationWorkbenchAdapter;
import org.jboss.tools.hibernate.ui.view.OpenDiagramActionDelegate;

/**
 * @author Dmitry Geraskov
 *
 */
public class OpenMappingDiagramTest extends TestCase {

	protected String consoleConfigName = null;
	
	protected IPackageFragment testPackage = null; 

	protected int openEditors = 0;

	public OpenMappingDiagramTest() {
	}

	public OpenMappingDiagramTest(String name) {
		super(name);
	}
	
	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
		consoleConfigName = null;
		testPackage = null;
		closeAllEditors();
	}

	public void testOpenMappingDiagram() {
		KnownConfigurations knownConfigurations = KnownConfigurations.getInstance();
		final ConsoleConfiguration consCFG = knownConfigurations.find(consoleConfigName);
		assertNotNull(consCFG);
		consCFG.reset();
		Object[] configs = null;
		Object[] persClasses = null;
		try {
			configs = new ConsoleConfigurationWorkbenchAdapter().getChildren(consCFG);
			assertTrue(configs[0] instanceof ConfigurationStub);
			persClasses = new ConfigurationWorkbenchAdapter().getChildren(configs[0]);
		} catch (RuntimeException ex) {
			// TODO: RuntimeException ? - find correct solution
			if (ex.getClass().getName().contains("InvalidMappingException")) { //$NON-NLS-1$
				String out = NLS.bind(ConsoleTestMessages.OpenMappingDiagramTest_mapping_diagrams_for_package_cannot_be_opened,
						new Object[]{testPackage.getElementName(), ex.getMessage()});
				fail(out);
			} else {
				throw ex;
			}
		}

		if (persClasses.length > 0){
			for (int i = 0; i < persClasses.length; i++) {
				assertTrue(persClasses[i] instanceof PersistentClassStub);
				PersistentClassStub persClass = (PersistentClassStub) persClasses[i];

				IEditorPart editor = null;
				Throwable ex = null;
				try {
					editor = new OpenDiagramActionDelegate().openEditor(persClass, consCFG);
				} catch (PartInitException e) {
					ex = e;
				}
				if (ex == null ) {
					ex = Utils.getExceptionIfItOccured(editor);
				}
				if (ex != null) {
					String out = NLS.bind(ConsoleTestMessages.OpenMappingDiagramTest_mapping_diagram_for_not_opened,
							new Object[]{persClass.getClassName(), ex.getMessage()});
					fail(out);
				}
			}
		}
		//close all editors
	}

	public String getConsoleConfigName() {
		return consoleConfigName;
	}

	public void setConsoleConfigName(String consoleConfigName) {
		this.consoleConfigName = consoleConfigName;
	}

	public IPackageFragment getTestPackage() {
		return testPackage;
	}

	public void setTestPackage(IPackageFragment testPackage) {
		this.testPackage = testPackage;
	}
	
	protected void closeAllEditors() {
		final IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (workbenchWindow != null) {
			final IWorkbenchPage workbenchPage = workbenchWindow.getActivePage();
			if (workbenchPage != null) {
				openEditors += workbenchPage.getEditorReferences().length;
				workbenchPage.closeAllEditors(false);
			}
		}
		// clean up event queue to avoid "memory leak",
		// this is necessary to fix https://jira.jboss.org/jira/browse/JBIDE-4824
		while (Display.getCurrent().readAndDispatch());
	}
}
