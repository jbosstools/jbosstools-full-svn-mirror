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

import java.io.FileNotFoundException;

import junit.framework.TestCase;

import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.hibernate.InvalidMappingException;
import org.hibernate.cfg.Configuration;
import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.console.KnownConfigurations;
import org.hibernate.eclipse.console.actions.OpenSourceAction;
import org.hibernate.eclipse.console.test.ConsoleTestMessages;
import org.hibernate.eclipse.console.workbench.ConfigurationWorkbenchAdapter;
import org.hibernate.eclipse.console.workbench.ConsoleConfigurationWorkbenchAdapter;
import org.hibernate.eclipse.console.workbench.PersistentClassWorkbenchAdapter;
import org.hibernate.eclipse.console.workbench.PropertyWorkbenchAdapter;
import org.hibernate.mapping.Component;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;

/**
 * @author Dmitry Geraskov
 *
 */
public class OpenSourceFileTest extends TestCase {

	public void testOpenSourceFileTest() {
		//fail("test fail");
		KnownConfigurations knownConfigurations = KnownConfigurations.getInstance();
		final ConsoleConfiguration consCFG = knownConfigurations.find(ProjectUtil.ConsoleCFGName);
		assertNotNull(consCFG);
		consCFG.reset();
		Object[] configs = null;
		Object[] persClasses = null;
		Object[] fields = null;
		try {
			configs = new ConsoleConfigurationWorkbenchAdapter().getChildren(consCFG);
			assertTrue(configs[0] instanceof Configuration);
			persClasses = new ConfigurationWorkbenchAdapter().getChildren(configs[0]);
		} catch (InvalidMappingException ex){
			String out = NLS.bind(ConsoleTestMessages.OpenSourceFileTest_source_files_for_package_cannot_be_opened,
					HibernateAllMappingTests.getActivePackage().getElementName(), ex.getMessage());
			fail(out);
		}
		if (persClasses.length > 0){
			for (int i = 0; i < persClasses.length; i++) {
				assertTrue(persClasses[0] instanceof PersistentClass);
				PersistentClass persClass = (PersistentClass) persClasses[i];
				String fullyQualifiedName = persClass.getClassName();
				// test PersistentClasses
				openTest(persClass, consCFG, fullyQualifiedName);
				fields =  new PersistentClassWorkbenchAdapter().getChildren(persClass);
				for (int j = 0; j < fields.length; j++) {
					if (fields[j].getClass() != Property.class) continue;
					fullyQualifiedName = persClass.getClassName();
					// test Properties
					openTest(fields[j], consCFG, fullyQualifiedName);
					if (fields[j] instanceof Property
						&& ((Property)fields[j]).isComposite()) {
						fullyQualifiedName =((Component)((Property) fields[j]).getValue()).getComponentClassName();

						Object[] compProperties = new PropertyWorkbenchAdapter().getChildren(fields[j]);
						for (int k = 0; k < compProperties.length; k++) {
							if (compProperties[k].getClass() != Property.class) continue;
							//test Composite properties
							openTest(compProperties[k], consCFG, fullyQualifiedName);
						}
					}
				}
			}
		}
		//close all editors
	}


	private void openTest(Object selection, ConsoleConfiguration consCFG, String fullyQualifiedName){
		IEditorPart editor = null;
		Throwable ex = null;
		try {
			editor = OpenSourceAction.run(consCFG, selection, fullyQualifiedName);
			boolean highlighted = ProjectUtil.checkHighlighting(editor);
			if (!highlighted) {
				String out = NLS.bind(ConsoleTestMessages.OpenSourceFileTest_highlighted_region_for_is_empty, selection);
				fail(out);
			}
		} catch (PartInitException e) {
			ex = e;
		} catch (JavaModelException e) {
			ex = e;
		} catch (FileNotFoundException e) {
			ex = e;
		}
		if (ex == null ) ex = ProjectUtil.getExceptionIfItOccured(editor);
		if (ex != null) {
			String out = NLS.bind(ConsoleTestMessages.OpenSourceFileTest_mapping_file_for_not_opened,
					fullyQualifiedName/*.getClassName()*/, ex.getMessage());
			fail(out);
		}
	}



}
