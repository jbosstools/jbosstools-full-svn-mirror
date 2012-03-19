package org.jboss.tools.central.test.ui.bot;

import java.io.File;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.SWTFormsBotExt;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ui.bot.ext.parts.SWTBotTwistie;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.wizards.SWTBotWizard;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

@Require(server=@org.jboss.tools.ui.bot.ext.config.Annotations.Server(type=ServerType.JbossAS))
public class CreateProjectsTest extends SWTTestExt{
	
	private static final String JBOSS_INSTALL_PATH = "/tmp/jbossAS";
	
	@BeforeClass
	public static void setup(){
		log.info(configuredState.getServer().name);
		bot.menu("Help").menu(IDELabel.JBossCentralEditor.JBOSS_CENTRAL).click();
		util.waitForAll();
	}
	@AfterClass
	public static void teardown(){
		deleteDirectory(new File(JBOSS_INSTALL_PATH));
	}
	
	//@Test
	public void createProjectsSectionTest(){
		//waitForAWhile();
		SWTFormsBotExt formsBot = SWTBotFactory.getFormsBot();
		//Dynamic web project
		bot.hyperlink(IDELabel.JBossCentralEditor.DYNAMIC_WEB_PROJECT).click();
		bot.waitForShell(IDELabel.JBossCentralEditor.NEW_DYNAMIC_WEB_PROJECT);
		assertTrue("New Dynamic Web Project should have appeared", bot.shell(IDELabel.JBossCentralEditor.NEW_DYNAMIC_WEB_PROJECT).isActive());
		bot.activeShell().close();
		//Openshift app
		log.info(bot.activeShell().getText());
		bot.hyperlink(IDELabel.JBossCentralEditor.OPENSHIFT_APP).click();
		bot.waitForShell(IDELabel.JBossCentralEditor.OPENSHIFT_APP_WIZARD);
		//assertTrue("New Dynamic Web Project should have appeared", bot.shell(IDELabel.JBossCentralEditor.OPENSHIFT_APP_WIZARD).isActive());
		bot.activeShell().close();
		
		//check Project example and detection of server
//		formsBot.formTextWithText(IDELabel.JBossCentralEditor.JAVA_EE_WEB_PROJECT).click();
//		//bot.hyperlink(IDELabel.JBossCentralEditor.JAVA_EE_WEB_PROJECT).click();
//		SWTBotShell projectExampleShell = bot.waitForShell(IDELabel.JBossCentralEditor.PROJECT_EXAMPLE);
//		assertTrue("Project Example window should have appeared", bot.shell(IDELabel.JBossCentralEditor.PROJECT_EXAMPLE).isActive());
//		try{
//			bot.clickButton("Install");
//			SWTBotShell shell = bot.waitForShell(IDELabel.Menu.PREFERENCES);
//			if (shell == null){
//				fail("Preferences shell should have appeared");
//			}
//			bot.activeShell().close();
//		}catch(WidgetNotFoundException wnfex){
//			fail("Missing Install button");
//		}
//		try{
//			projectExampleShell.activate();
//			bot.clickButton("Download and Install...");
//		}catch(WidgetNotFoundException wnfex){
//			fail("Missing \"Download and Install\" button");
//		}
//		
//		//create direcotry where will be JBossAS downloaded
//		if(!createDirectory(JBOSS_INSTALL_PATH)){
//			fail("Unable to create direcory for JBoss - \""+JBOSS_INSTALL_PATH+"\"");
//		}
//		
//		bot.textWithLabel("Install folder:").setText(JBOSS_INSTALL_PATH);
//		bot.textWithLabel("Download folder:").setText("/tmp");
//		bot.clickButton("OK");
//		bot.waitForShell("Progress Information");
//		util.waitForNonIgnoredJobs(Long.MAX_VALUE);
//		//bot.waitUntil(Conditions.shellCloses(bot.activeShell()), Long.MAX_VALUE, TIME_5S);
//		projectExampleShell.close();
		
		//server should be added.. check again
		//formsBot.formTextWithText(IDELabel.JBossCentralEditor.JAVA_EE_WEB_PROJECT).click();
		bot.hyperlink(IDELabel.JBossCentralEditor.JAVA_EE_WEB_PROJECT).click();
		SWTBotShell projectExampleShell = bot.waitForShell(IDELabel.JBossCentralEditor.PROJECT_EXAMPLE);
		assertTrue("Project Example window should have appeared", bot.shell(IDELabel.JBossCentralEditor.PROJECT_EXAMPLE).isActive());
		try{
			bot.clickButton("Install");
			fail("Button \"Install\" should not be enabled, because all requirements should have been met");
		}catch(WidgetNotFoundException wnfex){
			//ok
		}
		projectExampleShell.activate();
		assertFalse("Button \"Download and Install...\" should not be enabled, because all requirements should have been met, condition", bot.button("Download and Install...").isEnabled());
//		bot.clickButton("Download and Install...");
//		fail("Button \"Download and Install...\" should not be enabled, because all requirements should have been met");
		projectExampleShell.close();
		
		//check the rest of project examples
		checkCreateProject(IDELabel.JBossCentralEditor.JAVA_EE_WEB_PROJECT);
		checkCreateProject(IDELabel.JBossCentralEditor.JAVA_EE_PROJECT);
		checkCreateProject(IDELabel.JBossCentralEditor.HTML5_PROJECT);
		checkCreateProject(IDELabel.JBossCentralEditor.SPRING_MVC_PROJECT);
		checkCreateProject(IDELabel.JBossCentralEditor.RICHFACES_PROJECT);
		bot.toolbarDropDownButtonWithTooltip("New").click();
		bot.waitForShell("New");
		assertTrue("Shell \"New\" should have appeared", bot.shell("New").isActive());
		bot.activeShell().close();
	}
	
	@Test
	public void projectExamplesSectionTest(){
		SWTBotTwistie twistieBot = bot.twistieByLabel("JBoss Quickstarts");
		while (!twistieBot.isExpanded()){
			twistieBot.toggle();
		}
		/*if (!twistieBot.isExpanded()){
			twistieBot.toggle();
		}*/
		SWTFormsBotExt formsBot = SWTBotFactory.getFormsBot();
		checkExample(formsBot, "Helloworld", true);
		checkExample(formsBot, "Numberguess", true);
		//checkExample(formsBot, "Login", false); //Login example ma nejaky divny login.xml cheatsheet
		//checkExample(formsBot, "Kitchensink", true); //zatim nejaka chyba v JBDS https://issues.jboss.org/browse/JBDS-2072
		checkExample(formsBot, "HTML5", true);
	}
	
	private void waitForAWhile(){
		bot.sleep(Long.MAX_VALUE);
	}
	
	private static boolean deleteDirectory(File path) {
	    if( path.exists() ) {
	      File[] files = path.listFiles();
	      for(int i=0; i<files.length; i++) {
	         if(files[i].isDirectory()) {
	           deleteDirectory(files[i]);
	         }
	         else {
	           files[i].delete();
	         }
	      }
	    }
	    return( path.delete() );
	  }
	
	private void checkExample(SWTFormsBotExt formsBot, String formText, boolean readme){
		formsBot.formTextWithText(formText).click();
		bot.waitForShell(IDELabel.JBossCentralEditor.PROJECT_EXAMPLE);
		SWTBotWizard wizard = new SWTBotWizard(bot.shell(IDELabel.JBossCentralEditor.PROJECT_EXAMPLE).widget);
		wizard.next();
		wizard.finishWithWait();
		String readmeText = bot.checkBox(1).getText();
		assertFalse("Quick fix should not be enabled (Everything should be fine)", bot.checkBox(0).isEnabled());
		if (readme){
			assertTrue("Show readme checkbox should be enabled", bot.checkBox(1).isEnabled());
			assertTrue("Show readme checkbox should be checked by default", bot.checkBox(1).isChecked());
			if (readmeText.contains("cheatsheet.xml")){
				bot.clickButton("Finish");
				assertTrue("Cheat Sheets view should be open right now", bot.activeView().getTitle().equals("Cheat Sheets"));
				bot.activeView().close();
			}else if (readmeText.toLowerCase().contains("readme.md")){
				bot.clickButton("Finish");
				assertTrue("Readme should have opened in Text Editor", bot.activeEditor().getReference().getEditor(false).getClass().getName().contains("org.eclipse.ui.editors.text.TextEditor"));
				bot.activeEditor().close();
			}else if (readmeText.toLowerCase().contains("readme.htm")){
				bot.clickButton("Finish");
				assertTrue("Readme should have opened in Internal Browser", bot.activeEditor().getReference().getEditor(false).getClass().getName().contains("org.eclipse.ui.internal.browser.WebBrowserEditor"));
			}
		}
	}
	
	private void checkCreateProject(String formText){
		//formsBot.formTextWithText(formText).click();
		bot.hyperlink(formText).click();
		bot.waitForShell(IDELabel.JBossCentralEditor.PROJECT_EXAMPLE);
		SWTBotWizard wizard = new SWTBotWizard(bot.shell(IDELabel.JBossCentralEditor.PROJECT_EXAMPLE).widget);
		wizard.next();
		wizard.finishWithWait();
		bot.activeShell().close();
		if (bot.activeEditor().getTitle().equalsIgnoreCase("cheat sheets")){
			bot.activeEditor().close();
		}
//		assertTrue("Project Example window should have appeared", bot.shell(IDELabel.JBossCentralEditor.PROJECT_EXAMPLE).isActive());
//		bot.button("Start").click();
//		bot.waitForShell(wizzardShellText);
//		assertTrue(wizzardShellText+"  should have appeared", bot.shell(wizzardShellText).isActive());
//		bot.activeShell().close();
	}
}
