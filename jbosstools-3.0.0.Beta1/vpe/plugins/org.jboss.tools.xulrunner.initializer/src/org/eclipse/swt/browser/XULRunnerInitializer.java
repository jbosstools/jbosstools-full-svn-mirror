package org.eclipse.swt.browser;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class XULRunnerInitializer {

	private static final String XULRUNNER_PATH = "org.eclipse.swt.browser.XULRunnerPath";
	private static final String XULRUNNER_ENTRY = "/xulrunner";

	static {
		String xulrunnerPath = System.getProperty(XULRUNNER_PATH);
		if (xulrunnerPath != null) {
			File xulrunnerFile = new File(xulrunnerPath);
			if (!xulrunnerFile.exists()) {
				xulrunnerPath = null;
			}
		}
		if (xulrunnerPath == null) {
			String XULRUNNER_BUNDLE = (new StringBuffer("org.mozilla.xulrunner"))
					.append(".")
					.append(Platform.getWS())
					.append(".")
					.append(Platform.getOS())
					.append(Platform.OS_MACOSX.equals(Platform.getOS()) ? "" : (new StringBuffer(".")).append(Platform.getOSArch()).toString())
					.toString();
			Bundle xulRunnerBundle = Platform.getBundle(XULRUNNER_BUNDLE);
			if (xulRunnerBundle == null) {
				System.out.println("Bundle " + XULRUNNER_BUNDLE + " is not found.");
			} else {
				URL url = xulRunnerBundle.getEntry(XULRUNNER_ENTRY);
				if (url == null) {
					System.out.println("Bundle " + XULRUNNER_BUNDLE + " doesn't contain " + XULRUNNER_ENTRY);
				} else {
					File xulrunnerFile;
					try {
						URL url1 = FileLocator.resolve(url);
						xulrunnerFile = new File(FileLocator.toFileURL(url1)
								.getFile());
						xulrunnerPath = xulrunnerFile.getAbsolutePath();
						System.setProperty(XULRUNNER_PATH, xulrunnerPath);
					} catch (IOException ioe) {
						System.out.println("Cannot get path to XULRunner from bundle " + XULRUNNER_BUNDLE);
						ioe.printStackTrace();
					}
				}
			}
		}
	}
}
