package org.jboss.tools.seam.ui.bot.test.create;

import org.jboss.tools.seam.ui.bot.test.TestControl;

public class CreateSeamRuntimes extends TestControl{
	
	public void testCreateSeamRuntime12(){
		createSeamRuntime(seam12Settings, SEAM_12_SETTINGS_HOME);
		}
	
	public void testCreateSeamRuntime2fp(){
		createSeamRuntime(seam2fpSettings, SEAM_2FP_SETTINGS_HOME);
		}
	
	public void testCreateSeamRuntime21(){
		createSeamRuntime(seam21Settings, SEAM_21_SETTINGS_HOME);
		}
}
