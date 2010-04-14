package org.jboss.tools.ui.bot.ext;

import static org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.eclipse.finder.matchers.WidgetMatcherFactory;
import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.hamcrest.Matcher;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.gen.IActionItem;
import org.jboss.tools.ui.bot.ext.gen.IExport;
import org.jboss.tools.ui.bot.ext.gen.IImport;
import org.jboss.tools.ui.bot.ext.gen.INewObject;
import org.jboss.tools.ui.bot.ext.gen.IPerspective;
import org.jboss.tools.ui.bot.ext.gen.IPreference;
import org.jboss.tools.ui.bot.ext.gen.IView;

/**
 * this class represents
 * 
 * @author lzoubek
 * 
 */
public class SWTOpenExt {

	private static final Logger log = Logger.getLogger(SWTOpenExt.class);
	private final SWTBotExt bot;

	public SWTOpenExt(SWTBotExt bot) {
		this.bot = bot;
	}

	/**
	 * opens and shows view defined by given argument
	 */
	public SWTBotView viewOpen(IView view) {
		SWTBotView viewObj = null;
		try {
			viewObj = bot.viewByTitle(view.getName());
			viewObj.setFocus();
			viewObj.show();
			return viewObj;
		} catch (WidgetNotFoundException ex) {
		}
		bot.menu("Window").menu("Show View").menu("Other...").click();
		SWTBotShell shell = bot.shell("Show View");
		shell.activate();
		selectTreeNode(view);
		bot.button("OK").click();
		viewObj = bot.viewByTitle(view.getName());
		viewObj.setFocus();
		viewObj.show();
		return viewObj;
	}
	/**
	 * closes given view
	 * 
	 * @param view
	 */
	public void viewClose(IView view) {
		try {
			bot.viewByTitle(view.getName()).close();
		} catch (WidgetNotFoundException ex) {
			log.info("Unsuccessfull attempt to close view '" + view.getName()
					+ "'");
		}
	}

	/**
	 * selects and returns given view, view must be opened
	 * 
	 * @param view
	 * @return
	 */
	public SWTBotView viewSelect(IView view) {
		SWTBotView v = bot.viewByTitle(view.getName());
		v.show();
		v.setFocus();
		return v;

	}

	/**
	 * selects given actionItem in bot's tree();
	 * 
	 * @param item
	 */
	public void selectTreeNode(IActionItem item) {
		SWTBotTreeItem ti = null;
		try {
			Iterator<String> iter = item.getGroupPath().iterator();
			
			if (iter.hasNext()) {
				String next = iter.next();
				ti = bot.tree().expandNode(next);				
				try {
					while (iter.hasNext()) {
						next = iter.next();
						// expanding node is failing, so try to collapse and expand it again					
						ti.expand();
						ti = ti.expandNode(next);										
					}
				next = item.getName();	
				ti.expandNode(next).select();
				}
				catch (WidgetNotFoundException ex) {
					log.warn("Tree item '"+next+"' was not found, trying to collapse and reexpand parent node");
					ti.collapse();
					ti.expand();
					ti.select();
					ti = ti.expandNode(next);
					ti.select();
				}
			} else {
				bot.tree().select(item.getName());
			}
		} catch (WidgetNotFoundException ex) {
			String exStr="Item '"+ ActionItem.getItemString(item)+ "' does not exist in tree";
			if (ti!=null) {
				exStr+=", last selected item was '"+ti.getText()+"'";
			}
			throw new WidgetNotFoundException(exStr, ex);
		}
	}

	/**
	 * shows Preferences dialog, select given preference in tree
	 * 
	 * @param pref
	 * @return
	 */
	public SWTBot preferenceOpen(IPreference pref) {
		bot.menu("Window").menu("Preferences").click();
		SWTBotShell shell = bot.shell("Preferences");
		shell.activate();
		selectTreeNode(pref);
		return shell.bot();
	}

	/**
	 * switches perspective
	 * 
	 * @param perspective
	 */
	public void perspective(IPerspective perspective) {
		bot.menu("Window").menu("Open Perspective").menu("Other...").click();
		SWTBotShell shell = bot.shell("Open Perspective");
		shell.activate();
		bot.table().select(perspective.getName());
		bot.button("OK").click();
	}

	/**
	 * shows new 'anything' dialog selecting given argument in treeview and
	 * clicks next button
	 * 
	 * @param wizard
	 * @return
	 */
	public SWTBot newObject(INewObject wizard) {
		bot.menu("File").menu("New").menu("Other...").click();
		waitForShell("New");
		SWTBotShell shell = bot.shell("New");
		shell.activate();
		selectTreeNode(wizard);
		bot.button("Next >").click();
		return bot;
	}
	
	/**
	 * Wait for appearance shell of given name
	 * 
	 * @param shellName
	 */
	public void waitForShell(String shellName) {
		Matcher<Shell> matcher = WidgetMatcherFactory.withText(shellName);
		bot.waitUntil(Conditions.waitForShell(matcher));

	}

	/**
	 * shows import wizard dialog selecting given argument in treeview and
	 * clicks next button
	 * 
	 * @param importWizard
	 * @return
	 */
	public SWTBot newImport(IImport importWizard) {
		bot.menu("File").menu("Import...").click();
		SWTBotShell shell = bot.shell("Import");
		shell.activate();
		selectTreeNode(importWizard);
		bot.button("Next >").click();
		return bot;
	}

	/**
	 * shows import wizard dialog selecting given argument in treeview and
	 * clicks next button
	 * 
	 * @param importWizard
	 * @return
	 */
	public SWTBot newExport(IExport export) {
		bot.menu("File").menu("Export...").click();
		SWTBotShell shell = bot.shell("Export");
		shell.activate();
		selectTreeNode(export);
		bot.button("Next >").click();
		return bot;
	}

	/**
	 * closes active window clicking 'Cancel'
	 * 
	 * @param bot
	 */
	public void closeCancel(SWTBot bot) {
		SWTBotButton btn = bot.button("Cancel");
		btn.click();
	}
	/**
	 * clicks given button on active shell and waits until shell disappears
	 * @param bot 
	 * @param finishButtonText
	 */
	public void finish(SWTBot bot, String finishButtonText) {
		long timeout =480*1000;
		
		SWTBotShell activeShell = bot.activeShell();
		String activeShellStr = bot.activeShell().getText();
		bot.button(finishButtonText).click();
		long time = System.currentTimeMillis();
		while (true) {
			log.info("Waiting until shell '" + activeShellStr + "' closes");
			try {
				bot.waitUntil(shellCloses(activeShell));
				log.info("OK, shell '"+activeShellStr+"' closed.");
				return;
			} catch (TimeoutException ex) {
				if (System.currentTimeMillis()-time>timeout) {
					log.error("Shell '"+activeShellStr+"' probably hanged up (480s timeout), returning, forcing to close it, expect errors");
					try {
						bot.activeShell().close();
						activeShell.close();
						bot.waitUntil(shellCloses(activeShell));
						log.info("Shell  '"+activeShellStr+"' was forced to close.");
						return;
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					throw new WidgetNotFoundException("Shell '"+activeShellStr+"' did not close after timeout", ex);
				}
				log.warn("Shell '" + activeShellStr + "' is still opened");
			}
		}
	}
	/**
	 * clicks 'Finish' button on active shell and waits until shell disappears
	 * @param bot
	 */
	public void finish(SWTBot bot) {
		finish(bot,"Finish");
	}
}
