/*******************************************************************************
  * Copyright (c) 2007 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/

package org.jboss.tools.vpe.xulrunner.browser;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.jboss.tools.vpe.xulrunner.XPCOM;
import org.jboss.tools.vpe.xulrunner.XulRunnerException;
import org.mozilla.interfaces.nsIComponentManager;
import org.mozilla.interfaces.nsIPrefService;
import org.mozilla.interfaces.nsIRequest;
import org.mozilla.interfaces.nsIServiceManager;
import org.mozilla.interfaces.nsISupports;
import org.mozilla.interfaces.nsITooltipListener;
import org.mozilla.interfaces.nsIURI;
import org.mozilla.interfaces.nsIWebBrowser;
import org.mozilla.interfaces.nsIWebBrowserChrome;
import org.mozilla.interfaces.nsIWebBrowserSetup;
import org.mozilla.interfaces.nsIWebNavigation;
import org.mozilla.interfaces.nsIWebProgress;
import org.mozilla.interfaces.nsIWebProgressListener;
import org.mozilla.xpcom.GREVersionRange;
import org.mozilla.xpcom.Mozilla;
import org.mozilla.xpcom.XPCOMException;
import org.osgi.framework.Bundle;

/**
 * 
 * @author Sergey Vasilyev (svasilyev@exadel.com)
 *
 */

public class XulRunnerBrowser implements nsIWebBrowserChrome,
		nsIWebProgressListener, nsITooltipListener {
	private static final String XULRUNNER_LOWER_VERSION = "1.9.1.0"; //$NON-NLS-1$
	private static final String XULRUNNER_HIGHER_VERSION = "1.9.1.9"; //$NON-NLS-1$
	// TODO Sergey Vasilyev Think. May be XULRUNNER_BUNDLE shouldn't be final?
	private static final String XULRUNNER_BUNDLE;
	private static final String XULRUNNER_ENTRY = "/xulrunner"; //$NON-NLS-1$
	
	// TEMPORARY CODE (@see org.eclipse.swt.browser.Mozilla)
	static final String XULRUNNER_INITIALIZED = "org.eclipse.swt.browser.XULRunnerInitialized"; //$NON-NLS-1$
	static final String XULRUNNER_PATH = "org.eclipse.swt.browser.XULRunnerPath"; //$NON-NLS-1$

	private static final String ROOT_BRANCH_NAME = ""; //$NON-NLS-1$
	
	private static final String PREFERENCE_DISABLEOPENDURINGLOAD = "dom.disable_open_during_load"; //$NON-NLS-1$
	private static final String PREFERENCE_DISABLEWINDOWSTATUSCHANGE = "dom.disable_window_status_change"; //$NON-NLS-1$	
	
	private static final Mozilla mozilla;
	private Browser browser = null;
	private nsIWebBrowser webBrowser = null;
	private long chrome_flags = nsIWebBrowserChrome.CHROME_ALL;
	protected static final long NS_ERROR_FAILURE = 0x80004005L; 
	
	static {
		XULRUNNER_BUNDLE = (new StringBuffer("org.mozilla.xulrunner")) //$NON-NLS-1$
			.append(".").append(Platform.getWS()) //$NON-NLS-1$
			.append(".").append(Platform.getOS()) //$NON-NLS-1$
			.append(Platform.OS_MACOSX.equals(Platform.getOS()) ? "" : (new StringBuffer(".")).append(Platform.getOSArch()).toString()) //$NON-NLS-1$ //$NON-NLS-2$
			.toString();
		
		mozilla = Mozilla.getInstance();
	}
	
	public XulRunnerBrowser(Composite parent) throws XulRunnerException {
//	    initXulRunner();
	    
	    browser = new Browser(parent, SWT.MOZILLA);
	    
	    webBrowser = (nsIWebBrowser) browser.getWebBrowser();
            if (webBrowser == null) {
                throw new XulRunnerException("nsIWebBrowser is not available"); //$NON-NLS-1$
            }

            setBoolRootPref(PREFERENCE_DISABLEOPENDURINGLOAD, true);
            setBoolRootPref(PREFERENCE_DISABLEWINDOWSTATUSCHANGE, true);
            
            nsIWebBrowserSetup setup = (nsIWebBrowserSetup) webBrowser
            	.queryInterface(nsIWebBrowserSetup.NS_IWEBBROWSERSETUP_IID);
            setup.setProperty(nsIWebBrowserSetup.SETUP_IS_CHROME_WRAPPER, 1);
    
            // JBIDE-1329 Solution was contributed by Snjezana Peco
//             webBrowser.addWebBrowserListener(this,
//            	nsIWebProgressListener.NS_IWEBPROGRESSLISTENER_IID);
            nsIServiceManager serviceManager = mozilla.getServiceManager();
            nsIWebProgress webProgress = (nsIWebProgress) serviceManager
    		.getServiceByContractID("@mozilla.org/docloaderservice;1", //$NON-NLS-1$
    			nsIWebProgress.NS_IWEBPROGRESS_IID);
            webProgress.addProgressListener(this, nsIWebProgress.NOTIFY_STATE_ALL);
            webBrowser.addWebBrowserListener(this,
    		nsITooltipListener.NS_ITOOLTIPLISTENER_IID);
        }

	public synchronized void initXulRunner() throws XulRunnerException {
		String xulRunnerPath = getXulRunnerPath(); 
		
		if (!"true".equals(System.getProperty(XULRUNNER_INITIALIZED))) { //$NON-NLS-1$
			File file = new File(xulRunnerPath);
			mozilla.initialize(file);
			mozilla.initEmbedding(file, file, new AppFileLocProvider(file));
			System.setProperty(XULRUNNER_INITIALIZED, "true"); //$NON-NLS-1$
		}
	}

	/**
	 * Decorate Widget.getDisplay()
	 */
	public Display getDisplay() {
		return browser.getDisplay();
	}
	
	/**
	 * Decorate Control.setCursor(org.eclipse.swt.graphics.Cursor)
	 */
	public void setCursor(Cursor cursor) {
		browser.setCursor(cursor);		
	}
	
	/**
	 * Decorate Composite.setFocus()
	 */
	public boolean setFocus() {
		return browser.setFocus();
	}
	
	/**
	 * Decorate Control.setLayoutData(Object)
	 */
	public void setLayoutData(Object layoutData) {
		browser.setLayoutData(layoutData);
	}
	
	/**
	 * Decorate Widget.addListener(int eventType, Listener listener)
	 */
	public void addListener(int eventType, Listener listener) {
		browser.addListener(eventType, listener);
	}
	
	public void removeListener(int eventType, Listener listener) {
		browser.removeListener(eventType, listener);
	}
	
	/**
	 * Decorate Widget.dispose()
	 */
	public void dispose() {
		//added by mareshkau, here we remove listener.
		//if we hasn't do it, listener will be continue work even after close browser
        nsIServiceManager serviceManager = mozilla.getServiceManager();
        nsIWebProgress webProgress = (nsIWebProgress) serviceManager
		.getServiceByContractID("@mozilla.org/docloaderservice;1", //$NON-NLS-1$
			nsIWebProgress.NS_IWEBPROGRESS_IID);
        try {
        	webProgress.removeProgressListener(this);
        } catch(XPCOMException xpcomException) {
        	
        	//this exception throws when progress listener already has been deleted, 
        	//so just ignore if error code NS_ERROR_FAILURE
        	if(xpcomException.errorcode!=XulRunnerBrowser.NS_ERROR_FAILURE) {
        		throw xpcomException;
        	}
        }
		browser.dispose();
		browser = null;
	}
	
	public static String getXulRunnerBundle() {
		return XULRUNNER_BUNDLE;
	}

	public synchronized static String getXulRunnerPath() throws XulRunnerException {
		//this function should be call
		String xulRunnerPath = System.getProperty(XULRUNNER_PATH);
		if (xulRunnerPath == null) {

			GREVersionRange[] greRanges = {new GREVersionRange(XULRUNNER_LOWER_VERSION, true, XULRUNNER_HIGHER_VERSION, true)};
			File xulRunnerFile  = null;

			// JBIDE-1222 begin
			/*try {
				if(!XULRUNNER_LOADING_INDICATOR) {
					
					XULRUNNER_LOADING_INDICATOR=true;
					//this function should be call
					xulRunnerFile = Mozilla.getGREPathWithProperties(greRanges, null);
				} else {
					
					xulRunnerFile = null;
				}
			} catch (FileNotFoundException fnfe) {
				// Ignre this exception. Will try to get XULRunner from plugin
			}*/ 
			// JBIDE-1222 end
			

				Bundle xulRunnerBundle = Platform.getBundle(getXulRunnerBundle());
				if (xulRunnerBundle == null) {
					throw new XulRunnerException("Bundle " + getXulRunnerBundle() + " is not found."); //$NON-NLS-1$ //$NON-NLS-2$
				}

				String xulRunnerVersion = (String) xulRunnerBundle.getHeaders().get("Bundle-Version"); //$NON-NLS-1$
				if (!greRanges[0].check(xulRunnerVersion)) {
					throw new XulRunnerException("the version of the bundled XULRunner must be >= " + XulRunnerBrowser.XULRUNNER_LOWER_VERSION //$NON-NLS-1$
						+ " and <= " + XulRunnerBrowser.XULRUNNER_HIGHER_VERSION); //$NON-NLS-1$
				}
				
				
				URL url = xulRunnerBundle.getEntry(XULRUNNER_ENTRY);
				if (url == null) {
					throw new XulRunnerException("Bundle " + getXulRunnerBundle() + " doesn't contain " + XULRUNNER_ENTRY); //$NON-NLS-1$ //$NON-NLS-2$
				}

				try {
					URL url1 = FileLocator.resolve(url);
					xulRunnerFile = new File(FileLocator.toFileURL(url1).getFile());
				} catch (IOException ioe) {
					throw new XulRunnerException("Cannot get path to XULRunner from bundle " + getXulRunnerBundle(), ioe); //$NON-NLS-1$
				}
				
			xulRunnerPath = xulRunnerFile.getAbsolutePath();
			System.setProperty(XULRUNNER_PATH, xulRunnerPath);
		}

		
		return xulRunnerPath;
	}

	public nsIServiceManager getServiceManager() {
		return mozilla.getServiceManager();
	}
	
	public nsIComponentManager getComponentManager() {
		return mozilla.getComponentManager();
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

	public void onLoadWindow() {
	}

	public nsIPrefService getPrefService() {
		return (nsIPrefService) getServiceManager().getServiceByContractID(XPCOM.NS_PREFSERVICE_CONTRACTID, nsIPrefService.NS_IPREFSERVICE_IID);
	}

	
	public void setBoolRootPref(String aPrefName, boolean aValue) {
		getPrefService().getBranch(ROOT_BRANCH_NAME).setBoolPref(aPrefName, aValue ? 1 : 0);
	}
	
	
	public void setCharRootPref(String aPrefName, String aValue) {
		getPrefService().getBranch(ROOT_BRANCH_NAME).setCharPref(aPrefName, aValue);
	}
	
	
	public void setComplexRootValue(String aPrefName, String aType, nsISupports aValue) {
		getPrefService().getBranch(ROOT_BRANCH_NAME).setComplexValue(aPrefName, aType, aValue);
	}
	
	public void setIntRootPref(String aPrefName, int aValue) {
		getPrefService().getBranch(ROOT_BRANCH_NAME).setIntPref(aPrefName, aValue);
	}
	
	public boolean getBoolRootPref(String aPrefName) {
		return getPrefService().getBranch(ROOT_BRANCH_NAME).getBoolPref(aPrefName);
	}

	public String getCharRootPref(String aPrefName) {
		return getPrefService().getBranch(ROOT_BRANCH_NAME).getCharPref(aPrefName);
	}

	public nsISupports getComplextRootPref(String aPrefName, String aType) {
		return getPrefService().getBranch(ROOT_BRANCH_NAME).getComplexValue(aPrefName, aType);
	}

	public int getIntRootf(String aPrefName) {
		return getPrefService().getBranch(ROOT_BRANCH_NAME).getIntPref(aPrefName);
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
	 * nsIWebBrowserChrome
	 * 
	 * @see org.mozilla.interfaces.nsIWebBrowserChrome
	 */ 
	public void destroyBrowserWindow() {
		// TODO Sergey Vasilyev implement
		throw new RuntimeException("Not implemented"); //$NON-NLS-1$
	}

	public void exitModalEventLoop(long arg0) {
		throw new RuntimeException("Not implemented"); //$NON-NLS-1$
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
		throw new RuntimeException("Not implemented"); //$NON-NLS-1$
	}

	public void setWebBrowser(nsIWebBrowser arg0) {
		webBrowser = arg0;
	}

	public void showAsModal() {
		// TODO Sergey Vasilyev implement
		throw new RuntimeException("Not implemented"); //$NON-NLS-1$
	}

	public void sizeBrowserTo(int arg0, int arg1) {
		// TODO Sergey Vasilyev implement
		throw new RuntimeException("Not implemented"); //$NON-NLS-1$
	}
	
	/* (non-Javadoc)
	 * @see org.mozilla.interfaces.nsIWebProgressListener#onLocationChange(org.mozilla.interfaces.nsIWebProgress, org.mozilla.interfaces.nsIRequest, org.mozilla.interfaces.nsIURI)
	 */
	public void onLocationChange(nsIWebProgress arg0, nsIRequest arg1,
			nsIURI arg2) {
	}

	/* (non-Javadoc)
	 * @see org.mozilla.interfaces.nsIWebProgressListener#onProgressChange(org.mozilla.interfaces.nsIWebProgress, org.mozilla.interfaces.nsIRequest, int, int, int, int)
	 */
	public void onProgressChange(nsIWebProgress arg0, nsIRequest arg1,
			int arg2, int arg3, int arg4, int arg5) {
	}

	/* (non-Javadoc)
	 * @see org.mozilla.interfaces.nsIWebProgressListener#onSecurityChange(org.mozilla.interfaces.nsIWebProgress, org.mozilla.interfaces.nsIRequest, long)
	 */
	public void onSecurityChange(nsIWebProgress arg0, nsIRequest arg1, long arg2) {
	}

	/* (non-Javadoc)
	 * @see org.mozilla.interfaces.nsIWebProgressListener#onStateChange(org.mozilla.interfaces.nsIWebProgress, org.mozilla.interfaces.nsIRequest, long, long)
	 */
	public void onStateChange(nsIWebProgress aWebProgress, nsIRequest aRequest, long aStateFlags, long aStstus) {
		if ((aStateFlags & nsIWebProgressListener.STATE_IS_WINDOW) != 0
				&& (aStateFlags & nsIWebProgressListener.STATE_STOP) != 0) {
			onLoadWindow();
		}
	}

	/* (non-Javadoc)
	 * @see org.mozilla.interfaces.nsIWebProgressListener#onStatusChange(org.mozilla.interfaces.nsIWebProgress, org.mozilla.interfaces.nsIRequest, long, java.lang.String)
	 */
	public void onStatusChange(nsIWebProgress arg0, nsIRequest arg1, long aStatus, String message) {
	}

	
	
	/* (non-Javadoc)
	 * @see org.mozilla.interfaces.nsITooltipListener#onHideTooltip()
	 */
	public void onHideTooltip() {
	}

	/* (non-Javadoc)
	 * @see org.mozilla.interfaces.nsITooltipListener#onShowTooltip(int, int, java.lang.String)
	 */
	public void onShowTooltip(int aXCoords, int aYCoords, String aTipText) {
	}

	/**
	 * @return the browser
	 */
	public Browser getBrowser() {
		return browser;
	}
	
	protected void onDispose() {
		webBrowser = null;
	}
	
	public void setText(String html) {
		browser.setText(html);
	}
}
