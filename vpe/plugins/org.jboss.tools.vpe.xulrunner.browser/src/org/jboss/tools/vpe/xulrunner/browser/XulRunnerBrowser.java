package org.jboss.tools.vpe.xulrunner.browser;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.jboss.tools.vpe.xulrunner.XulRunnerException;
import org.mozilla.interfaces.nsIBaseWindow;
import org.mozilla.interfaces.nsIComponentManager;
import org.mozilla.interfaces.nsISupports;
import org.mozilla.interfaces.nsIWebBrowser;
import org.mozilla.interfaces.nsIWebBrowserChrome;
import org.mozilla.interfaces.nsIWebBrowserFocus;
import org.mozilla.interfaces.nsIWebNavigation;
import org.mozilla.xpcom.Mozilla;
import org.osgi.framework.Bundle;

public class XulRunnerBrowser extends Composite implements nsIWebBrowserChrome {
	private static String xulRunnerBundle = "org.jboss.tools.vpe.xulrunner"; //$NON-NLS-1$
	
	// TEMPORARY CODE (@see org.eclipse.swt.browser.Mozilla)
	static final String XULRUNNER_INITIALIZED = "org.eclipse.swt.browser.XULRunnerInitialized"; //$NON-NLS-1$
	static final String XULRUNNER_PATH = "org.eclipse.swt.browser.XULRunnerPath"; //$NON-NLS-1$

	private Mozilla mozilla = null;
	private nsIWebBrowser webBrowser = null;
	private long chrome_flags = nsIWebBrowserChrome.CHROME_ALL;
	
	private boolean busyResizeFlag = false;
	
	public XulRunnerBrowser(Composite parent) throws XulRunnerException {
		super(parent, SWT.NONE);
		
		mozilla = Mozilla.getInstance();
		
		String xulRunnerPath = getXulRunnerPath(); 
		
		Boolean isXulRunnerInitialized = "true".equals(System.getProperty(XULRUNNER_INITIALIZED));
		if (!isXulRunnerInitialized) {
			File file = new File(xulRunnerPath);
			mozilla.initialize(file);
			mozilla.initEmbedding(file, file, new AppFileLocProvider(file));
			System.setProperty(XULRUNNER_INITIALIZED, "true");
		}
		
		nsIComponentManager componentManager = mozilla.getComponentManager();
		webBrowser = (nsIWebBrowser) componentManager.createInstance("F1EAC761-87E9-11d3-AF80-00A024FFC08C", null, nsIWebBrowser.NS_IWEBBROWSER_IID); //$NON-NLS-1$
		webBrowser.setContainerWindow(this);
		nsIBaseWindow baseWindow = (nsIBaseWindow) webBrowser.queryInterface(nsIBaseWindow.NS_IBASEWINDOW_IID);
		
		Rectangle rect = getClientArea();
		if (rect.isEmpty()) {
			rect.height = 1;
			rect.width = 1;
		}
		baseWindow.initWindow(handle, 0, 0, 0, rect.height, rect.width);
		baseWindow.create();
		baseWindow.setVisibility(true);
		
		Listener listener = new Listener(){
			public void handleEvent (Event event) {
				switch(event.type) {
				case SWT.Dispose:
						onDispose();
						break;
				case SWT.Activate:
				case SWT.FocusIn:
						onFocusGained();
						break;
				case SWT.Deactivate:
					if (XulRunnerBrowser.this == event.display.getFocusControl()) {
						onFocusLost();
					}
					break;
				case SWT.Resize:
				case SWT.Show:
					/*
					* Feature on GTK Mozilla.  Mozilla does not show up when
					* its container (a GTK fixed handle) is made visible
					* after having been hidden.  The workaround is to reset
					* its size after the container has been made visible. 
					*/
					if (!busyResizeFlag) {
						busyResizeFlag = true;
						event.display.asyncExec(new Runnable() {
							public void run() {
								if (XulRunnerBrowser.this.isDisposed()) return;
								onResize();
								busyResizeFlag = false;
							}
						});
					}
					break;
				case SWT.KeyDown:
					onKeyDown();
					break;
				}
			}
		};

		addListener(SWT.Dispose, listener);
		addListener(SWT.Resize, listener);
		addListener(SWT.FocusIn, listener); 
		addListener(SWT.KeyDown, listener);
		addListener(SWT.Activate, listener);
		addListener(SWT.Deactivate, listener);
		addListener(SWT.Show, listener);
	}

	private void onDispose() {
		nsIBaseWindow baseWindow = (nsIBaseWindow) webBrowser.queryInterface(nsIBaseWindow.NS_IBASEWINDOW_IID);
		baseWindow.destroy();
	}
	
	private void onFocusGained() {
		nsIWebBrowserFocus webBrowserFocus = (nsIWebBrowserFocus) webBrowser.queryInterface(nsIWebBrowserFocus.NS_IWEBBROWSERFOCUS_IID);
		webBrowserFocus.activate();
	}
	
	private void onFocusLost() {
		nsIWebBrowserFocus webBrowserFocus = (nsIWebBrowserFocus) webBrowser.queryInterface(nsIWebBrowserFocus.NS_IWEBBROWSERFOCUS_IID);
		webBrowserFocus.deactivate();
	}
	
	private void onResize() {
		nsIBaseWindow baseWindow = (nsIBaseWindow) webBrowser.queryInterface(nsIBaseWindow.NS_IBASEWINDOW_IID);
		
		Rectangle rect = getClientArea();
		if (rect.isEmpty()) {
			rect.height = 1;
			rect.width = 1;
		}
		
		baseWindow.setPositionAndSize(rect.x, rect.y, rect.width, rect.height, true);
	}
	
	private void onKeyDown() {
		System.out.println("XulRunnerBrowser.onKeyDown()");
	}
	
	public static String getXulRunnerBundle() {
		return xulRunnerBundle;
	}

	public static void setXulRunnerBundle(String xulRunnerBundle) {
		XulRunnerBrowser.xulRunnerBundle = xulRunnerBundle;
	}

	private String getXulRunnerPath() throws XulRunnerException {
		String xulRunnerPath = System.getProperty(XULRUNNER_PATH);
		if (xulRunnerPath == null) {
			
			Bundle fragment = Platform.getBundle(getXulRunnerBundle());
			if (fragment == null) {
				throw new XulRunnerException("Bundle " + getXulRunnerBundle() + " is not found.");
			}
			
			URL url = fragment.getEntry("/xulrunner");
			if (url == null) {
				throw new XulRunnerException("Bundle " + getXulRunnerBundle() + " doesn't contain /xulrunner");
			}
			
			
			try {
				URL url1 = FileLocator.resolve(url);
				File file = new File(FileLocator.toFileURL(url1).getFile());
				xulRunnerPath = file.getAbsolutePath();
				System.setProperty(XULRUNNER_PATH, xulRunnerPath);
			} catch (IOException ioe) {
				throw new XulRunnerException(ioe);
			}
		}
		
		return xulRunnerPath;
	}

	public void setURL(String url) {
		nsIWebNavigation webNavigation = (nsIWebNavigation) webBrowser.queryInterface(nsIWebNavigation.NS_IWEBNAVIGATION_IID);
		webNavigation.loadURI(url, nsIWebNavigation.LOAD_FLAGS_NONE, null, null, null);
	}
	
	public String getURL() {
		nsIWebNavigation webNavigation = (nsIWebNavigation) webBrowser.queryInterface(nsIWebNavigation.NS_IWEBNAVIGATION_IID);
		return webNavigation.getCurrentURI().getSpec();
	}
	
	public void stop() {
		nsIWebNavigation webNavigation = (nsIWebNavigation) webBrowser.queryInterface(nsIWebNavigation.NS_IWEBNAVIGATION_IID);
		webNavigation.stop(nsIWebNavigation.STOP_ALL);
	}
	
	public void reload() {
		nsIWebNavigation webNavigation = (nsIWebNavigation) webBrowser.queryInterface(nsIWebNavigation.NS_IWEBNAVIGATION_IID);
		webNavigation.reload(nsIWebNavigation.LOAD_FLAGS_NONE);
	}
	
	public void goBack() {
		nsIWebNavigation webNavigation = (nsIWebNavigation) webBrowser.queryInterface(nsIWebNavigation.NS_IWEBNAVIGATION_IID);
		webNavigation.goBack();
	}
	
	public void goForward() {
		nsIWebNavigation webNavigation = (nsIWebNavigation) webBrowser.queryInterface(nsIWebNavigation.NS_IWEBNAVIGATION_IID);
		webNavigation.goForward();
	}

	/*
	 * nsISupports
	 * 
	 * @see org.mozilla.interfaces.nsISupports#queryInterface(java.lang.String)
	 */
	public nsISupports queryInterface(String arg0) {
		return Mozilla.queryInterface(this, arg0);
	}

	/*
	 * (non-Javadoc)
	 * @see org.mozilla.interfaces.nsIWebBrowserChrome#destroyBrowserWindow()
	 */ 
	public void destroyBrowserWindow() {
		// TODO Sergey Vasilyev implement
		throw new RuntimeException("Not implemented");
	}

	public void exitModalEventLoop(long arg0) {
		throw new RuntimeException("Not implemented");
	}

	public long getChromeFlags() {
		return chrome_flags;
	}

	public nsIWebBrowser getWebBrowser() {
		return webBrowser;
	}

	public boolean isWindowModal() {
		// TODO Sergey Vasilyev implement
		return false;
	}

	public void setChromeFlags(long arg0) {
		chrome_flags = arg0;
	}

	public void setStatus(long arg0, String arg1) {
		// TODO Sergey Vasilyev implement
		throw new RuntimeException("Not implemented");
	}

	public void setWebBrowser(nsIWebBrowser arg0) {
		webBrowser = arg0;
	}

	public void showAsModal() {
		// TODO Sergey Vasilyev implement
		throw new RuntimeException("Not implemented");
	}

	public void sizeBrowserTo(int arg0, int arg1) {
		// TODO Sergey Vasilyev implement
		throw new RuntimeException("Not implemented");
	}
	
	protected void finalize() throws Throwable {
		if (mozilla != null) {
			mozilla.termEmbedding();
		}
		
		super.finalize();
	}
}
