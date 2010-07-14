/*******************************************************************************
 * Copyright (c) 2007-2010 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.wizard;

import java.io.File;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.test.WidgetVariables;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;

public class ImportUnknownTagsWizardTest extends VPEAutoTestCase {

	private final String STORED_TAGS_PATH = "storedTags.xml"; //$NON-NLS-1$
	
	public ImportUnknownTagsWizardTest() {
		super();
	}

	@Override
	protected void closeUnuseDialogs() {

	}

	@Override
	protected boolean isUnuseDialogOpened() {
		return false;
	}

	public void testImportWizard() throws Throwable {
		/*
		 * Open wizard page
		 */
		bot.menu("File").menu("Import...").click(); //$NON-NLS-1$ //$NON-NLS-2$
		bot.shell("Import").activate(); //$NON-NLS-1$
		SWTBotTree importTree = bot.tree();
		importTree.expandNode("Other").select("Unknown tags templates"); //$NON-NLS-1$ //$NON-NLS-2$
		bot.button(WidgetVariables.NEXT_BUTTON).click();
		/*
		 * Load stored tags
		 */
		File file = new File (getPathToResources(STORED_TAGS_PATH));
        assertTrue("File '" + file.getAbsolutePath() +"' does not exist.", file.exists()); //$NON-NLS-1$ //$NON-NLS-2$
        bot.text().setText(file.getAbsolutePath());
        /*
         * Check table values 
         */
        String taglib = bot.table().cell(0, 0);
        assertEquals("Wrong table value.", "lib:tag", taglib); //$NON-NLS-1$ //$NON-NLS-2$
        taglib = bot.table().cell(1, 0);
        assertEquals("Wrong table value.", "taglibName:tagName", taglib); //$NON-NLS-1$  //$NON-NLS-2$
        /*
         * Check that finish button is enabled and press it.
         */
        assertTrue("Finish button should be enabled.", //$NON-NLS-1$
        		bot.button(WidgetVariables.FINISH_BUTTON).isEnabled());
        bot.button(WidgetVariables.FINISH_BUTTON).click();
        /*
         * Check that templates have been added to the preference page 
         */
        bot.menu(IDELabel.Menu.WINDOW).menu(IDELabel.Menu.PREFERENCES).click(); //$NON-NLS-1$ //$NON-NLS-2$
		bot.shell(IDELabel.Shell.PREFERENCES).activate(); //$NON-NLS-1$
		importTree = bot.tree();
		importTree.expandNode(IDELabel.PreferencesDialog.JBOSS_TOOLS) //$NON-NLS-1$d
				.expandNode(IDELabel.PreferencesDialog.JBOSS_TOOLS_WEB) //$NON-NLS-1$
				.expandNode(IDELabel.PreferencesDialog.JBOSS_TOOLS_WEB_EDITORS) //$NON-NLS-1$
				.select(IDELabel.PreferencesDialog.JBOSS_TOOLS_WEB_EDITORS_VPE); //$NON-NLS-1$
		bot.tabItem(IDELabel.PreferencesDialog.JBOSS_TOOLS_WEB_EDITORS_VPE_VISUAL_TEMPLATES).activate(); //$NON-NLS-1$
		/*
         * Check table values on the preferences page
         */
        String taglib00 = bot.table().cell(0, 0);
        String taglib10 = bot.table().cell(1, 0);
        bot.button(WidgetVariables.OK_BUTTON).click();
        assertEquals("Wrong table value.", "taglibName:tagName", taglib00); //$NON-NLS-1$  //$NON-NLS-2$
        assertEquals("Wrong table value.", "lib:tag", taglib10); //$NON-NLS-1$ //$NON-NLS-2$
        
	}
	
}
