 /*******************************************************************************
  * Copyright (c) 2007-2009 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.hibernate.ui.bot.testcase;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swtbot.eclipse.finder.matchers.WidgetMatcherFactory;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.hamcrest.Matcher;
import org.jboss.tools.hibernate.ui.bot.test.util.DataHolder;
import org.jboss.tools.hibernate.ui.bot.testsuite.HibernateTest;
import org.jboss.tools.hibernate.ui.bot.testsuite.Project;
import org.jboss.tools.ui.bot.ext.config.Annotations.DB;
import org.jboss.tools.ui.bot.ext.config.Annotations.SWTBotTestRequires;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.types.PerspectiveType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

@SWTBotTestRequires( db=@DB, perspective="Hibernate")
public class CodeGenerationLauncherTest extends HibernateTest {

	SWTBotShell mainShell = null;
	public static boolean generationDone = false;
	
	@BeforeClass
	/**
	 * Setup prerequisites for this test
	 */
	public static void setUpTest() {

		eclipse.maximizeActiveShell();
		eclipse.closeView(IDELabel.View.WELCOME);
		
		prepareProject();
		prepareConsole();
		util.waitForNonIgnoredJobs();
	}
	
	/**
	 * Run code generation code
	 */
	@Test
	public void generate() {
		if (generationDone) return;
		
		log.info("HB Code Generation STARTED");
		log.info("Active Shell: " + bot.activeShell().getText());
		
		eclipse.openPerspective(PerspectiveType.HIBERNATE);
				
		createNewHibernateCodeGenerationConfiguration();
		
		fillMainTab();		
		fillExportersTab();
		fillRefreshTab();
		fillCommonTab();		
				
		bot.button(IDELabel.Button.RUN).click();	
		log.info("HB Code Generation FINISHED");
		util.waitForNonIgnoredJobs();
		
		log.info("Active Shell: " + bot.activeShell().getText());
		
		checkGeneratedFiles();
				
		generationDone = true;			
	}
	/**
	 * Checks existence generated files after code generation 
	 */
	private void checkGeneratedFiles() {
	
		log.info("Active Shell: " + bot.activeShell().getText());
		SWTBotShell[] shells = bot.shells();
		for (SWTBotShell shell : shells) {
			log.info("Possible shell: " + shell.getText());
		}
					 
		open.viewOpen(ActionItem.View.JavaPackageExplorer.LABEL);					

		
		for (String table : DataHolder.tables) {
			String className = table.substring(0,1).toUpperCase() + table.substring(1).toLowerCase();
			packageExplorer.openFile(Project.PROJECT_NAME,"gen","org","test", className + ".java"); 
		}

		log.info("Generated files check DONE");
	}

	/**
	 * 
	 */
	private void createNewHibernateCodeGenerationConfiguration() {
		SWTBotMenu menu = null;
		menu = bot.menu("Run");
		menu = menu.menu(IDELabel.Menu.HIBERNATE_CODE_GENERATION);
		menu = menu.menu(IDELabel.Menu.HIBERNATE_CODE_GENERATION_CONF).click();
		
		mainShell = bot.activeShell();
	}

	/**
	 * TC 09
	 */
	public void fillMainTab() {		
		
		bot.tree().expandNode("Hibernate Code Generation").select();
		bot.toolbarButtonWithTooltip("New launch configuration").click();
		
		eclipse.selectTreeLocation("Hibernate Code Generation","New_configuration");
		bot.textWithLabel("Name:").setText("HSQL Configuration");
		
		// Console Configuration
		bot.comboBoxWithLabel("Console configuration:").setSelection(Project.PROJECT_NAME);
		
		// Output directory
		bot.button("Browse...").click();
		bot.shell("Select output directory").activate();
		eclipse.selectTreeLocation(Project.PROJECT_NAME);
		bot.button("Create New Folder...").click();
		bot.shell("New Folder").activate();
		bot.textWithLabel("Folder name:").setText("gen");
		bot.button(IDELabel.Button.OK).click();
		eclipse.selectTreeLocation(Project.PROJECT_NAME,"gen");
		bot.button(IDELabel.Button.OK).click();
		
		// Create console configuration
		Matcher<Button> matcher = WidgetMatcherFactory.withText("Reverse engineer from JDBC Connection");
		Button button = bot.widget(matcher);		
		SWTBotCheckBox cb = new SWTBotCheckBox(button); 
				
		if (!cb.isChecked())
			cb.click();
		
		bot.textWithLabel("Package:").setText("org.test");
		log.info("HB Code Generation Main tab DONE");
		bot.sleep(TIME_1S);
	}

	/**
	 * TC 10
	 */
	public void fillExportersTab() {
		mainShell.activate();
		bot.cTabItem(IDELabel.HBLaunchConfigurationDialog.EXPORTERS_TAB).activate();
		bot.table().select("Domain code (.java)");
		bot.table().getTableItem(0).check();
		log.info("HB Code Generation Exporters tab DONE");
		bot.sleep(TIME_1S);
	}

	/**
	 * TC 11
	 */
	public void fillRefreshTab() {
		mainShell.activate();
		bot.cTabItem(IDELabel.HBLaunchConfigurationDialog.REFRESH_TAB).activate();
		log.info("HB Code Generation Refresh tab DONE");
		bot.sleep(TIME_1S);
	}

	/**
	 * TC 12
	 */
	public void fillCommonTab() {
		mainShell.activate();
		bot.cTabItem(IDELabel.HBLaunchConfigurationDialog.COMMON_TAB).activate();
		log.info("HB Code Generation Common tab DONE");
		bot.sleep(TIME_1S);
	}
	
	@AfterClass
	public static void clean() {
	}	
}
