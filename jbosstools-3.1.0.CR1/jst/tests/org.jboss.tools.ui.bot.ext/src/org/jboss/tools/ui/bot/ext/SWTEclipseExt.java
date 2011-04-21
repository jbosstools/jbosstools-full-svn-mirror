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

package org.jboss.tools.ui.bot.ext;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.logging.Logger;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.eclipse.finder.matchers.WidgetMatcherFactory;
import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.hamcrest.Matcher;
import org.jboss.tools.ui.bot.ext.entity.JavaClassEntity;
import org.jboss.tools.ui.bot.ext.entity.JavaProjectEntity;
import org.jboss.tools.ui.bot.ext.types.EntityType;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.types.PerspectiveType;
import org.jboss.tools.ui.bot.ext.types.ViewType;

/**
 * Provieds Eclipse common operation based on SWTBot element operations
 * @author jpeterka
 *
 */
public class SWTEclipseExt {

	SWTBotExt bot = new SWTBotExt();
	SWTUtilExt util = new SWTUtilExt();
	Logger log = Logger.getLogger(SWTEclipseExt.class.getName());

	// ------------------------------------------------------------
	// Additional missing or more advance methods
	// ------------------------------------------------------------
	/**
	 * Close view by text
	 */
	public void closeView(String view) {
		bot.viewByTitle(view).close();
	}

	/**
	 * Show view
	 * 
	 * @param type
	 */
	public void showView(ViewType type) {
		bot.menu(IDELabel.Menu.WINDOW).menu(IDELabel.Menu.SHOW_VIEW).menu(
				IDELabel.Menu.OTHER).click();
		bot.tree().expandNode(type.getGroupLabel()).expandNode(type.getViewLabel()).select();
		bot.button(IDELabel.Button.OK).click();
		
		
	}

	/**
	 * Open Perspective
	 * 
	 * @param type
	 */
	public void openPerspective(PerspectiveType type) {
		String perspectiveLabel = "";

		if (type == PerspectiveType.JAVA) {
			perspectiveLabel = IDELabel.SelectPerspectiveDialog.JAVA;
		}

		bot.menu(IDELabel.Menu.WINDOW).menu(IDELabel.Menu.OPEN_PERSPECTIVE).menu(
				IDELabel.Menu.OTHER).click();
		bot.table().select(perspectiveLabel);

		// Another approach
		// SWTBotShell openPerpectiveShell = bot.shell("Open Perspective");
		// openPerpectiveShell.activate();

		bot.button(IDELabel.Button.OK).click();
	}

	/**
	 * Create Java Project desribed with propriate entity
	 * @param entity
	 */
	public void createJavaProject(JavaProjectEntity entity) {
		// NewWizard
		createNew(EntityType.JAVA_PROJECT);
		waitForShell(IDELabel.Shell.NEW_JAVA_PROJECT);

		// JavaProjectWizard
		bot.textWithLabel(IDELabel.JavaProjectWizard.PROJECT_NAME).setText(
				entity.getProjectName());
		bot.button(IDELabel.Button.FINISH).click();

		// Wait for shell closing JavaProjectWizard
		waitForClosedShell(IDELabel.Shell.NEW_JAVA_PROJECT);
		util.waitForNonIgnoredJobs();
	}
	
	/**
	 * Create new Java Class described with JavaClassEntity
	 * @param entity
	 */
	public void createJavaClass(JavaClassEntity entity) {
		createNew(EntityType.JAVA_CLASS);
		waitForShell(IDELabel.Shell.NEW_JAVA_CLASS);
		
		bot.textWithLabel(IDELabel.NewClassCreationWizard.PACKAGE_NAME).setText(entity.getPackageName());	
		bot.textWithLabel(IDELabel.NewClassCreationWizard.CLASS_NAME).setText(entity.getClassName());
		bot.button(IDELabel.Button.FINISH).click();
		
		waitForClosedShell(IDELabel.Shell.NEW_JAVA_CLASS);
		util.waitForNonIgnoredJobs();
	}
	
	/**
	 * Remove entity from package explorer
	 * @param projectName
	 * @param path
	 */
	public void removeFile(String projectName, String... path) {
		// Open Package Explorer and aim the Project
		SWTBot viewBot = bot.viewByTitle(IDELabel.View.PACKAGE_EXPLORER).bot();
		SWTBotTreeItem item = viewBot.tree().expandNode(projectName);

		// Go through path 
		for ( String nodeName: path ) {
			item = item.expandNode(nodeName);
		}

		// Delete File
		item.select().contextMenu("Delete").click();		
		bot.button(IDELabel.Button.OK).click();		
	}
	

	/**
	 * Open File in Package Explorer
	 * @param projectName
	 * @param path
	 */
	public void openFile(String projectName, String... path) {
		SWTBot viewBot = bot.viewByTitle(IDELabel.View.PACKAGE_EXPLORER).bot();
		SWTBotTreeItem item = viewBot.tree().expandNode(projectName);
		StringBuilder builder = new StringBuilder(projectName);
		
		// Go through path 
		for ( String nodeName: path ) {
			item = item.expandNode(nodeName);
			builder.append("/" + nodeName);
		}
		
		item.select().doubleClick();
		log.info("File Opened:" + builder.toString());
	}
	
	
	// ------------------------------------------------------------
	// Subroutines
	// ------------------------------------------------------------

	/**
	 * Invoke Create new entity dialog (it means File -> New -> EntityType -> Next )
	 */
	public void createNew(EntityType entityType) {
		
		String groupLabel = entityType.getGroupLabel();
		String entityLabel = entityType.getEntityLabel();
		
		bot.menu(IDELabel.Menu.FILE).menu(IDELabel.Menu.NEW).menu(IDELabel.Menu.OTHER).click();
		waitForShell(IDELabel.Shell.NEW);
		
		bot.tree().expandNode(groupLabel).select(entityLabel);
		bot.button(IDELabel.Button.NEXT).click();
	}
	
	/**
	 * Wait for appereance shell of given name
	 * @param shellName
	 */
	public void waitForShell(String shellName) {		
		Matcher<Shell> matcher = WidgetMatcherFactory
		.withText(shellName);
		bot.waitUntil(Conditions.waitForShell(matcher));
	}
	
	/**
	 * Waits for closed shell with shell name
	 * @param shellName
	 */
	public void waitForClosedShell(String shellName) {
		bot.waitWhile(Conditions.shellIsActive(shellName));	
	}
	
	/**
	 * Assert same content of file in project explorer with content of file in resources
	 * @param pluginId
	 * @param projectName
	 * @param path
	 */
	public void assertSameContent(String pluginId, String projectName, String... path) {
		File file = util.getResourceFile(pluginId, path);
		String resourceContent = util.readTextFile(file);
		
		openAsText(projectName, path);
		SWTBotEclipseEditor editor = bot.editorByTitle(path[path.length - 1]).toTextEditor();
		String fileContent = editor.getText();
		
		assertEquals(resourceContent, fileContent );
	}
	
	/**
	 * Open file from Package Explorer as text
	 * @param projectName
	 * @param path
	 */
	public void openAsText(String projectName, String... path) {
		SWTBot viewBot = bot.viewByTitle(IDELabel.View.PACKAGE_EXPLORER).bot();
		SWTBotTreeItem item = viewBot.tree().expandNode(projectName);
		
		// Go through path 
		for ( String nodeName: path ) {
			log.info(nodeName);
			item = item.expandNode(nodeName);
		}		
	
		item.select();		
	}

	/**
	 * Replace class content from content of class from resource
	 * @param pluginId
	 * @param path
	 */
	public void setClassContentFromResource(String pluginId, String... path) {		 
		SWTBotEclipseEditor editor;
		editor = bot.editorByTitle(path[path.length - 1]).toTextEditor();
		editor.selectRange(0, 0, editor.getLineCount());
		File file = util.getResourceFile(pluginId, path);
		String content = util.readTextFile(file);
		editor.setText(content);
		editor.save();	
	}
}
