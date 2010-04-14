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

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;
import static org.junit.Assert.fail;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCCombo;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.tools.ui.bot.ext.parts.SWTBotBrowserExt;
import org.jboss.tools.ui.bot.ext.parts.SWTBotEditorExt;
import org.jboss.tools.ui.bot.ext.parts.SWTBotScaleExt;

/**
 * Extended version of SWTWorkbenchBot, logging added
 * 
 * @author jpeterka
 * 
 */
public class SWTBotExt extends SWTWorkbenchBot {

	private Logger log = Logger.getLogger(SWTBotExt.class);

	public void logAndFail(String msg) {
		log.error(msg);
		fail(msg);
	}

	// ------------------------------------------------------------
	// SWTBot method wrapper ( for better logging mainly )
	// ------------------------------------------------------------

	@Override
	public SWTBotMenu menu(String text) {
		log.info("Menu \"" + text + "\" selected");
		return super.menu(text);
	}

	@Override
	public SWTBotButton button(String text) {
		log.info("Button \"" + text + "\" selected");
		return super.button(text);
	}

	@Override
	public SWTBotTree tree() {
		log.info("Tree selected");
		return super.tree();
	}

	@Override
	public SWTBotCCombo ccomboBox(String text) {
		log.info("Combobox \"" + text + "\" selected");
		return super.ccomboBox(text);
	}

	@Override
	public SWTBotTable table() {
		log.info("Table selected");
		return super.table();
	}

	public SWTBotEditorExt swtBotEditorExtByTitle(String fileName) {
		SWTBotEditor editor = super.editorByTitle(fileName);
		return new SWTBotEditorExt(editor.toTextEditor().getReference(),
				(SWTWorkbenchBot) this);
	}

	@SuppressWarnings("unchecked")
	public SWTBotBrowserExt browserByTitle(String title) {
		SWTBotEditor editor = editorByTitle(title);
		try {
			List<Browser> bsrs = (List<Browser>) editor.bot().widgets(
					widgetOfType(Browser.class));
			return new SWTBotBrowserExt(bsrs.get(0));
		} catch (WidgetNotFoundException ex) {
			throw new WidgetNotFoundException(
					"Could not find widget of type Browser", ex);
		}
	}

	@SuppressWarnings("unchecked")
	public SWTBotBrowserExt browser() {
		try {
			List<Browser> bsrs = (List<Browser>) widgets(widgetOfType(Browser.class));
			return new SWTBotBrowserExt(bsrs.get(0));
		} catch (WidgetNotFoundException ex) {
			throw new WidgetNotFoundException(
					"Could not find widget of type Browser", ex);
		}
	}

	public SWTBotScaleExt scale() {
		return scale(0);
	}
	@SuppressWarnings("unchecked")
	public SWTBotScaleExt scale(int index) {
		try {
			List<Scale> bsrs = (List<Scale>) widgets(widgetOfType(Scale.class));
			return new SWTBotScaleExt(bsrs.get(0));
		} catch (WidgetNotFoundException ex) {
			throw new WidgetNotFoundException(
					"Could not find widget of type Browser", ex);
		}
	}

}
