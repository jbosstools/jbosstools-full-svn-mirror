package org.jboss.tools.jsf.ui.bot.test.cssdialog.jbide;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.tools.jsf.ui.bot.test.JSFAutoTestCase;
import org.jboss.tools.ui.bot.test.WidgetVariables;

public class JBIDE3577Test extends JSFAutoTestCase{

	private static String CSS_FILE_NAME = "JBIDE3577";
	private static String CSS_CLASS_NAME = "cssclass";
	
	public void testJBIDE3577(){
		
		//Test create new CSS file
		
		SWTBot innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).bot();
		SWTBotTree tree = innerBot.tree();
		try {
			tree.expandNode(projectProperties.getProperty("JSFProjectName")).
			getNode(CSS_FILE_NAME+".css").doubleClick();
			bot.editorByTitle(CSS_FILE_NAME+".css").setFocus();
			bot.menu("Edit").menu("Select All").click();
			bot.menu("Edit").menu("Delete").click();
		} catch (WidgetNotFoundException e) {
			tree.getTreeItem(projectProperties.getProperty("JSFProjectName")).select();
			bot.menu("File").menu("New").menu("CSS File").click();
			bot.shell("New CSS File").activate();
			bot.textWithLabel("Name*").setText(CSS_FILE_NAME);
			bot.button("Finish").click();
		}
		SWTBotEclipseEditor eclipseEditor =	bot.editorByTitle(CSS_FILE_NAME+".css").toTextEditor();
		eclipseEditor.setFocus();
		eclipseEditor.insertText("cssclass{\r\tcolor:red;\r\t" +
		"background-color:green;\r}");
		eclipseEditor.save();
		eclipseEditor.contextMenu("Open CSS Dialog").click();
		
		//Test Clear all attributes

		bot.shell("CSS Class").activate();
		bot.comboBoxWithLabel("Style class:").setSelection(CSS_CLASS_NAME);
		bot.tabItem("Preview").activate();
		bot.button("Clear").click();
		bot.button("Apply").click();
		bot.button("OK").click();
		assertEquals("cssclass{\r\t\r}", bot.editorByTitle(CSS_FILE_NAME+".css").toTextEditor().getText());
		bot.editorByTitle(CSS_FILE_NAME+".css").close();

	}

	@Override
	protected void closeUnuseDialogs() {
		try {
			bot.shell("CSS Class").close();
		} catch (WidgetNotFoundException e) {
		}
	}

	@Override
	protected boolean isUnuseDialogOpened() {
		boolean isOpened = false;
		try {
			bot.shell("CSS Class").activate();
			isOpened = true;
		} catch (WidgetNotFoundException e) {
		}
		return isOpened;
	}
	
}
