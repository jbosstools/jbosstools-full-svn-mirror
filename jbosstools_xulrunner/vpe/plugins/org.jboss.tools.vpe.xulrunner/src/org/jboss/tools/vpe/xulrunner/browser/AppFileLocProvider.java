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
		this.userDataPath = Platform.getLocation().append(".metadata/.plugins") // $NON-NLS-1$
				.append(BrowserPlugin.PLUGIN_ID)
				.append("xulrunner").toFile(); // $NON-NLS-1$
	}
	
	public File getFile(String prop, boolean[] persistent) {
		persistent[0] = false;
		
		// TODO Sergey Vasilyev remove debug output
		System.out.println("AppFileLocProvider.getFile(" + prop + ")");
		
		if ("ProfD".equals(prop)) { // $NON-NLS-1$
			return userDataPath;
		} else if ("UHist".equals(prop)) { // $NON-NLS-1$
			return new File(userDataPath, HISTORY_FILE); 
		} else if ("ComRegF".equals(prop)) { // $NON-NLS-1$
			return new File(userDataPath, COMPREG_FILE);
		} else if ("XptiRegF".equals(prop)) { // $NON-NLS-1$
			return new File(userDataPath, XPTI_FILE);
		}
		
		return null;
	}

	public File[] getFiles(String prop) {
		// TODO Sergey Vasilyev remove debug output
		System.out.println("AppFileLocProvider.getFiles(" + prop + ")");

		if("APluginsDL".equals(prop)) { // $NON-NLS-1$
			return new File[] {new File(xulRunnerPath, PLUGINS_DIRECTORY)};
		}
		
		return null;
	}

}
