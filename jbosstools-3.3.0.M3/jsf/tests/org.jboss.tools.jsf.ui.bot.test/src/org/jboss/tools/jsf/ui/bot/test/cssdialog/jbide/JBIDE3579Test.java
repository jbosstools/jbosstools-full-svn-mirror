package org.jboss.tools.jsf.ui.bot.test.cssdialog.jbide;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.tools.jsf.ui.bot.test.JSFAutoTestCase;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.test.WidgetVariables;

public class JBIDE3579Test extends JSFAutoTestCase{
	
	private static String CSS_FILE_NAME = "JBIDE3579"; //$NON-NLS-1$
	private static String CSS_CLASS_NAME = "newCSSclass"; //$NON-NLS-1$
	
	public void testJBIDE3579(){
		
		//Test Create new CSS file
		
		SWTBot innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).bot();
		SWTBotTree tree = innerBot.tree();
		try {
			tree.expandNode(JBT_TEST_PROJECT_NAME). //$NON-NLS-1$
			getNode(CSS_FILE_NAME+".css").doubleClick(); //$NON-NLS-1$
			bot.editorByTitle(CSS_FILE_NAME+".css").setFocus(); //$NON-NLS-1$
			bot.menu("Edit").menu("Select All").click();  //$NON-NLS-1$//$NON-NLS-2$
			bot.menu("Edit").menu("Delete").click(); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (WidgetNotFoundException e) {
			tree.getTreeItem(JBT_TEST_PROJECT_NAME).select(); //$NON-NLS-1$
			open.newObject(ActionItem.NewObject.WebCSS.LABEL);  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
			bot.shell("New CSS File").activate(); //$NON-NLS-1$
			bot.textWithLabel("File name:").setText(CSS_FILE_NAME); //$NON-NLS-1$
			bot.button("Finish").click(); //$NON-NLS-1$
		}
		SWTBotEclipseEditor eclipseEditor =	bot.editorByTitle(CSS_FILE_NAME+".css").toTextEditor(); //$NON-NLS-1$
		eclipseEditor.setFocus();
		eclipseEditor.insertText("cssclass{\r\tcolor:red;\r\t" + //$NON-NLS-1$
		"background-color:green;\r}"); //$NON-NLS-1$
		eclipseEditor.save();
		eclipseEditor.contextMenu("Open CSS Dialog").click(); //$NON-NLS-1$
		
		//Test Create new CSS class

		bot.shell("CSS Class").activate(); //$NON-NLS-1$
		bot.button("Add CSS Class").click(); //$NON-NLS-1$
		bot.shell("Enter New CSS Class Name").activate(); //$NON-NLS-1$
		bot.textWithLabel("Enter New CSS Class Name").setText(CSS_CLASS_NAME); //$NON-NLS-1$
		bot.button("OK").click(); //$NON-NLS-1$
		bot.button("Apply").click(); //$NON-NLS-1$
		bot.button("OK").click(); //$NON-NLS-1$
		bot.editorByTitle(CSS_FILE_NAME+".css").close(); //$NON-NLS-1$
		
	}
	
	@Override
	protected void closeUnuseDialogs() {
		try {
			bot.shell("CSS Class").close(); //$NON-NLS-1$
		} catch (WidgetNotFoundException e) {
		}
	}

	@Override
	protected boolean isUnuseDialogOpened() {
		boolean isOpened = false;
		try {
			bot.shell("CSS Class").activate(); //$NON-NLS-1$
			isOpened = true;
		} catch (WidgetNotFoundException e) {
		}
		return isOpened;
	}

}
