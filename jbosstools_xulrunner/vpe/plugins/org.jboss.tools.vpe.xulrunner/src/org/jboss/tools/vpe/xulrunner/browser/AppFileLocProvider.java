package org.jboss.tools.vpe.xulrunner.browser;

import java.io.File;

import org.eclipse.core.runtime.Platform;
import org.jboss.tools.vpe.xulrunner.BrowserPlugin;
import org.mozilla.xpcom.IAppFileLocProvider;

public class AppFileLocProvider implements IAppFileLocProvider {
	private File xulRunnerPath;
	private File userDataPath;
	
	private final String PLUGINS_DIRECTORY = "plugins";
	private final String HISTORY_FILE = "history.dat";
	private final String COMPREG_FILE = "compreg.dat";
	private final String XPTI_FILE = "xpti.dat";
	
	public AppFileLocProvider(File xulRunnerPath) {
		this.xulRunnerPath = xulRunnerPath;
		this.userDataPath = Platform.getLocation().append(".metadata/.plugins")
				.append(BrowserPlugin.PLUGIN_ID)
				.append("xulrunner").toFile();
	}
	
	public File getFile(String prop, boolean[] persistent) {
		persistent[0] = false;
		
		if ("ProfD".equals(prop)) {
			return userDataPath;
		} else if ("UHist".equals(prop)) {
			return new File(userDataPath, HISTORY_FILE); 
		} else if ("ComRegF".equals(prop)) {
			return new File(userDataPath, COMPREG_FILE);
		} else if ("XptiRegF".equals(prop)) {
			return new File(userDataPath, XPTI_FILE);
		}
		
		return null;
	}

	public File[] getFiles(String prop) {
		if("APluginsDL".equals(prop)) {
			return new File[] {new File(xulRunnerPath, PLUGINS_DIRECTORY)};
		}
		
		return null;
	}

}
