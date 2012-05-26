package org.eclipse.swt.browser;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.util.NLS;
import org.osgi.framework.Bundle;

public class XULRunnerInitializer {

	private static final String XULRUNNER_PATH = "org.eclipse.swt.browser.XULRunnerPath"; //$NON-NLS-1$
	private static final String XULRUNNER_ENTRY = "/xulrunner"; //$NON-NLS-1$
	
	/* XXX: yradtsevich: these constants are duplicated
	 * in XulRunnerBrowser, see JBIDE-9188 */
	private static final String LOAD_XULRUNNER_SYSTEM_PROPERTY = "org.jboss.tools.vpe.loadxulrunner";//$NON-NLS-1$
	private static boolean EMBEDDED_XULRUNNER_ENABLED = !"false".equals(System.getProperty(LOAD_XULRUNNER_SYSTEM_PROPERTY)); //$NON-NLS-1$ //$NON-NLS-2$

	static {
		if (EMBEDDED_XULRUNNER_ENABLED) {
			initializeXULRunner();
		}
	}

	private static void initializeXULRunner() {
		String xulrunnerPath = System.getProperty(XULRUNNER_PATH);
		if (xulrunnerPath != null) {
			File xulrunnerFile = new File(xulrunnerPath);
			if (!xulrunnerFile.exists()) {
				xulrunnerPath = null;
			}
		}
		if (xulrunnerPath == null) {
			String XULRUNNER_BUNDLE = (new StringBuffer("org.mozilla.xulrunner")) //$NON-NLS-1$
					.append(".") //$NON-NLS-1$
					.append(Platform.getWS())
					.append(".") //$NON-NLS-1$
					.append(Platform.getOS())
					.append(Platform.OS_MACOSX.equals(Platform.getOS()) ? "" : (new StringBuffer(".")).append(Platform.getOSArch()).toString()) //$NON-NLS-1$ //$NON-NLS-2$
					.toString();
			Bundle xulRunnerBundle = Platform.getBundle(XULRUNNER_BUNDLE);
			if (xulRunnerBundle == null) {
				System.out.println(NLS.bind(Messages.XULRunnerInitializer_Bundle_is_not_found, XULRUNNER_BUNDLE ));
			} else {
				URL url = xulRunnerBundle.getEntry(XULRUNNER_ENTRY);
				if (url == null) {
					System.out.println(NLS.bind(Messages.XULRunnerInitializer_Bundle_doesnt_contain, new Object[] {XULRUNNER_BUNDLE,XULRUNNER_ENTRY}));
				} else {
					File xulrunnerFile;
					try {
						URL url1 = FileLocator.resolve(url);
						xulrunnerFile = new File(FileLocator.toFileURL(url1)
								.getFile());
						xulrunnerPath = xulrunnerFile.getAbsolutePath();
						System.setProperty(XULRUNNER_PATH, xulrunnerPath);
					} catch (IOException ioe) {
						System.out.println(NLS.bind(Messages.XULRunnerInitializer_Cannot_get_path_to_XULRunner_from_bundle,XULRUNNER_BUNDLE));
						ioe.printStackTrace();
					}
				}
			}
		}
	}
}
