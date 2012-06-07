/*******************************************************************************
 * Copyright (c) 2010-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test.rest;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTOpenExt;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.config.TestConfigurator;
import org.jboss.tools.ui.bot.ext.gen.IPreference;
import org.jboss.tools.ui.bot.ext.helper.BuildPathHelper;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.view.ProblemsView;
import org.jboss.tools.ui.bot.ext.view.ProjectExplorer;
import org.jboss.tools.ws.ui.bot.test.utils.ResourceHelper;

public class RESTfulHelper {
	
	private static final SWTBotExt bot = new SWTBotExt();
	private static final SWTUtilExt util = new SWTUtilExt(bot);
	private static final ProjectExplorer projectExplorer = new ProjectExplorer();
	private static final SWTOpenExt open = new SWTOpenExt(bot);
	private static ResourceHelper resourceHelper = new ResourceHelper();
	
	private final String PATH_PARAM_VALID_ERROR = "Invalid @PathParam value";
	private final String VALIDATION_PREFERENCE = "Validation";
	private final String ENABLE_ALL = "Enable All";
	private final String JAX_RS_VALIDATOR = "JAX-RS Metamodel Validator";
	private final String VALIDATION_SETTINGS_CHANGED = "Validation Settings Changed";
	
	private enum ConfigureOption {
		ADD, REMOVE;
	}
	
	private SWTBotTreeItem[] getRESTValidationErrors(String wsProjectName, String description) {
		return ProblemsView.getFilteredErrorsTreeItems(bot,
				description, "/" + wsProjectName, null, null);
	}
	
	public SWTBotTreeItem[] getPathAnnotationValidationErrors(String wsProjectName) {
		return getRESTValidationErrors(wsProjectName, PATH_PARAM_VALID_ERROR);
	}
	
	public SWTBotTreeItem[] getApplicationAnnotationValidationErrors(String wsProjectName) {
		return getRESTValidationErrors(wsProjectName, null);
	}

	public void enableRESTValidation() {
		modifyRESTValidation(ConfigureOption.ADD);
	}

	public void disableRESTValidation() {
		modifyRESTValidation(ConfigureOption.REMOVE);
	}

	/**
	 * DO IT BETTER!!!!!!!!!!!!!!!!!!
	 */
	public void modifyRESTValidation(ConfigureOption option) {

		SWTBot validationBot = openPreferencePage(VALIDATION_PREFERENCE,
				new ArrayList<String>());

		validationBot.button(ENABLE_ALL).click();

		if (option == ConfigureOption.REMOVE) {

			SWTBotTable validatorTable = validationBot.table();
			int restValidationRow = -1;
			for (int row = 0; row < validatorTable.rowCount(); row++) {
				if (validatorTable.getTableItem(row).getText()
						.equals(JAX_RS_VALIDATOR)) {
					restValidationRow = row;
					break;
				}
			}
			
			assertTrue(restValidationRow >= 0);

			validatorTable.click(restValidationRow, 1);
			validatorTable.click(restValidationRow, 2);
		}

		validationBot.button("OK").click();
		if (bot.activeShell().getText().equals(VALIDATION_SETTINGS_CHANGED)) {
			bot.activeShell().bot().button("Yes").click();
		}

		bot.sleep(Timing.time3S());
		util.waitForNonIgnoredJobs();

	}

	public void addRestSupport(String wsProjectName) {
		configureRestSupport(wsProjectName, ConfigureOption.ADD);
	}

	public void removeRestSupport(String wsProjectName) {
		configureRestSupport(wsProjectName, ConfigureOption.REMOVE);
	}

	public boolean isRestSupportEnabled(String wsProjectName) {
		return (projectExplorer.isFilePresent(wsProjectName,
				RESTFulAnnotations.REST_EXPLORER_LABEL.getLabel()) || projectExplorer
				.isFilePresent(wsProjectName,
						RESTFulAnnotations.REST_EXPLORER_LABEL_BUILD.getLabel()));
	}

	
	@SuppressWarnings("static-access")
	public List<String> addRestEasyLibs(String wsProjectName) {

		List<File> restLibsPaths = getPathForRestLibs();
		
		List<String> variables = new ArrayList<String>();
		
		BuildPathHelper buildPathHelper = new BuildPathHelper();
		
		for (File f : restLibsPaths) {
			variables.add(buildPathHelper.addExternalJar(f.getPath(), wsProjectName, true));
		}
		
		return variables;
		
	}
	
	private List<File> getPathForRestLibs() {
		
		assertTrue(TestConfigurator.currentConfig.getServer().type.equals("EAP"));
		
		String runtimeHome = TestConfigurator.currentConfig.getServer().runtimeHome;
		
		// index of last occurence of "/" in EAP runtime path: jboss-eap-5.1/jboss-as
		int indexOfAS = runtimeHome.lastIndexOf("/");
		
		// jboss-eap-5.1/jboss-as --> jboss-eap-5.1
		String eapDirHome = runtimeHome.substring(0, indexOfAS);
		
		String restEasyDirPath = eapDirHome + "/" + "resteasy";
		File restEasyDir = new File(restEasyDirPath);
		
		String[] restEasyLibs = {"jaxrs-api.jar"};
		
		return resourceHelper.searchAllFiles(restEasyDir, restEasyLibs);
	}

	private void configureRestSupport(String wsProjectName,
			ConfigureOption option) {
		projectExplorer.selectProject(wsProjectName);
		SWTBotTree tree = projectExplorer.bot().tree();
		SWTBotTreeItem item = tree.getTreeItem(wsProjectName);
		ContextMenuHelper.prepareTreeItemForContextMenu(tree, item);
		SWTBotMenu menu = new SWTBotMenu(
				ContextMenuHelper.getContextMenu(
				tree, IDELabel.Menu.PACKAGE_EXPLORER_CONFIGURE, false));
		menu.menu(option == ConfigureOption.ADD ? RESTFulAnnotations.REST_SUPPORT_MENU_LABEL_ADD
				.getLabel() : RESTFulAnnotations.REST_SUPPORT_MENU_LABEL_REMOVE
				.getLabel()).click();
		bot.sleep(Timing.time2S());		
		util.waitForAll();
		
	}
	
	private SWTBot openPreferencePage(final String name,
			final List<String> groupPath) {
		return open.preferenceOpen(new IPreference() {

			@Override
			public String getName() {
				return name;
			}

			@Override
			public List<String> getGroupPath() {
				return groupPath;
			}
		});
	}
	
}
