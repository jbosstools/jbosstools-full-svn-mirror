package org.jboss.tools.vpe.xulrunner.browser;

import java.io.File;

import org.eclipse.core.runtime.Platform;
import org.jboss.tools.vpe.xulrunner.BrowserPlugin;
import org.mozilla.xpcom.IAppFileLocProvider;

public class AppFileLocProvider implements IAppFileLocProvider {
	private File xulRunnerPath;
	private File userDataPath;
	
	private static final String PLUGINS_DIRECTORY = "plugins"; //$NON-NLS-1$
	private static final String HISTORY_FILE = "history.dat"; //$NON-NLS-1$
	private static final String COMPREG_FILE = "compreg.dat"; //$NON-NLS-1$
	private static final String XPTI_FILE = "xpti.dat"; //$NON-NLS-1$
	private static final String COMPONENTS_DIRECTORY = "components"; //$NON-NLS-1$
	
	
	public AppFileLocProvider(File xulRunnerPath) {
		this.xulRunnerPath = xulRunnerPath;
		this.userDataPath = Platform.getLocation().append(".metadata/.plugins") //$NON-NLS-1$
				.append(BrowserPlugin.PLUGIN_ID)
				.append("xulrunner").toFile(); //$NON-NLS-1$
	}
	
	public File getFile(String prop, boolean[] persistent) {
		persistent[0] = false;
		
		File resultFile = null;
		
		if ("ProfD".equals(prop)) { //$NON-NLS-1$
			resultFile = userDataPath;
		} else if ("UHist".equals(prop)) { //$NON-NLS-1$
			resultFile = new File(userDataPath, HISTORY_FILE); 
		} else if ("ComRegF".equals(prop)) { //$NON-NLS-1$
			resultFile = new File(userDataPath, COMPREG_FILE);
		} else if ("XptiRegF".equals(prop)) { //$NON-NLS-1$
			resultFile = new File(userDataPath, XPTI_FILE);
		} else if ("GreD".equals(prop)) { //$NON-NLS-1$
			resultFile = xulRunnerPath;
		} else if ("GreComsD".equals(prop) || "ComsD".equals(prop)) { //$NON-NLS-1$ //$NON-NLS-2$
			resultFile = new File(xulRunnerPath, COMPONENTS_DIRECTORY);
		}

		if (BrowserPlugin.DEBUG_BROWSERSTART) {
			System.out.println("AppFileLocProvider.getFile(" + prop + ")=\"" + (resultFile == null ? "null" : resultFile.toString()) + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		
		return resultFile;
	}

	public File[] getFiles(String prop) {
		File[] resultFiles = null;
		if("APluginsDL".equals(prop)) { //$NON-NLS-1$
			resultFiles = new File[] {new File(xulRunnerPath, PLUGINS_DIRECTORY)};
		}
		
		if (BrowserPlugin.DEBUG_BROWSERSTART) {
			if (resultFiles != null) {
				int size = resultFiles.length;

				System.out.print("AppFileLocProvider.getFiles(" + prop + ")={\""); //$NON-NLS-1$ //$NON-NLS-2$
				for(int i=0; i<size; i++) {
					System.out.print((i > 0 ? ", " : "") + resultFiles[i].toString()); //$NON-NLS-1$ //$NON-NLS-2$
				}
				System.out.println("\"}"); //$NON-NLS-1$
			}
		}
		
		return resultFiles;
	}

}
