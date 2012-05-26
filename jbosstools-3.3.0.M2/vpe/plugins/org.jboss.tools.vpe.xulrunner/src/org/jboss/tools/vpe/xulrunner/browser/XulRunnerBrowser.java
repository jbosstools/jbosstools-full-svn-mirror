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
import java.net.URL;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.jboss.tools.vpe.xulrunner.VpeXulrunnerMessages;
import org.jboss.tools.vpe.xulrunner.XulRunnerException;
import org.jboss.tools.vpe.xulrunner.util.XPCOM;
import org.mozilla.interfaces.nsIComponentManager;
import org.mozilla.interfaces.nsIPrefService;
import org.mozilla.interfaces.nsIRequest;
import org.mozilla.interfaces.nsIServiceManager;
import org.mozilla.interfaces.nsISupports;
import org.mozilla.interfaces.nsITooltipListener;
import org.mozilla.interfaces.nsIURI;
import org.mozilla.interfaces.nsIWebBrowser;
import org.mozilla.interfaces.nsIWebBrowserChrome;
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
	private static final String XULRUNNER_HIGHER_VERSION = "1.9.2.16"; //$NON-NLS-1$
	// TODO Sergey Vasilyev Think. May be XULRUNNER_BUNDLE shouldn't be final?
	private static final String XULRUNNER_BUNDLE;
	
	// TEMPORARY CODE (@see org.eclipse.swt.browser.Mozilla)
	static final String XULRUNNER_INITIALIZED = "org.eclipse.swt.browser.XULRunnerInitialized"; //$NON-NLS-1$
	public static final String XULRUNNER_PATH = "org.eclipse.swt.browser.XULRunnerPath"; //$NON-NLS-1$

	private static final String ROOT_BRANCH_NAME = ""; //$NON-NLS-1$
	
	private static final String PREFERENCE_DISABLEOPENDURINGLOAD = "dom.disable_open_during_load"; //$NON-NLS-1$
	private static final String PREFERENCE_DISABLEWINDOWSTATUSCHANGE = "dom.disable_window_status_change"; //$NON-NLS-1$
	
	/* XXX: yradtsevich: these constants are duplicated
	 * in XULRunnerInitializer, see JBIDE-9188 */
	private static final String LOAD_XULRUNNER_SYSTEM_PROPERTY = "org.jboss.tools.vpe.loadxulrunner";//$NON-NLS-1$
	private static boolean EMBEDDED_XULRUNNER_ENABLED = !"false".equals(System.getProperty(LOAD_XULRUNNER_SYSTEM_PROPERTY));  //$NON-NLS-1$

	private Browser browser = null;
	private nsIWebBrowser webBrowser = null;
	private long chrome_flags = nsIWebBrowserChrome.CHROME_ALL; 
	public static final long NS_ERROR_FAILURE = 0x80004005L;
	private static final String XULRUNNER_ENTRY = "/xulrunner"; //$NON-NLS-1$

	private static final Set<String> OFFICIALLY_SUPPORTED_PLATFORM_IDS = new HashSet<String>();
	static {
		Collections.addAll(OFFICIALLY_SUPPORTED_PLATFORM_IDS,
				"cocoa.macosx.x86",     //$NON-NLS-1$
				"gtk.linux.x86",    //$NON-NLS-1$
				"gtk.linux.x86_64", //$NON-NLS-1$
				"win32.win32.x86"); //$NON-NLS-1$
	}
	public static final String CURRENT_PLATFORM_ID = Platform.getWS() + '.'
			+ Platform.getOS() + '.' + Platform.getOSArch();
	
	private static final Mozilla mozilla;
	static {
		StringBuffer buff = new StringBuffer();
		buff.append("org.mozilla.xulrunner.") //$NON-NLS-1$
			.append(Platform.getWS()).append('.')
			.append(Platform.getOS());
		
		/* XULRunner bundle names do not have
		 * '.x86' postfix for Mac OS X.	 */
		if(! Platform.OS_MACOSX.equals(Platform.getOS())) {
			buff.append('.').append(Platform.getOSArch());
		}
		XULRUNNER_BUNDLE =  buff.toString();
		mozilla = Mozilla.getInstance();
	}

	public XulRunnerBrowser(Composite parent) throws XulRunnerException {
		ensureEmbeddedXulRunnerEnabled();
		
	    if(Platform.OS_MACOSX.equals(Platform.getOS())){
	    	getXulRunnerPath();
	    }
		
	    browser = new Browser(parent, SWT.MOZILLA);
	    
	    webBrowser = (nsIWebBrowser) browser.getWebBrowser();
            if (webBrowser == null) {
                throw new XulRunnerException(VpeXulrunnerMessages.XulRunnerBrowser_notAvailable); 
            }

            setBoolRootPref(PREFERENCE_DISABLEOPENDURINGLOAD, true);
            setBoolRootPref(PREFERENCE_DISABLEWINDOWSTATUSCHANGE, true);
            
            /* yradtsevich: the following two lines are commented due to JBIDE-6647.
             * By some reason they were preventing of handling of Drag&Drop events. */
//            nsIWebBrowserSetup setup = XPCOM.queryInterface(webBrowser, nsIWebBrowserSetup.class);
//            setup.setProperty(nsIWebBrowserSetup.SETUP_IS_CHROME_WRAPPER, 1);
    
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
		removeProgressListener(this);
		browser.dispose();
		browser = null;
	}
	
	public static String getXulRunnerBundle() {
		return XULRUNNER_BUNDLE;
	}

	public synchronized static String getXulRunnerPath() throws XulRunnerException {
		ensureEmbeddedXulRunnerEnabled();
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
					if (!XulRunnerBrowser.isCurrentPlatformOfficiallySupported()) {
						throw new XulRunnerException(MessageFormat.format(
								VpeXulrunnerMessages.CURRENT_PLATFORM_IS_NOT_SUPPORTED,
								XulRunnerBrowser.CURRENT_PLATFORM_ID));
					} else {
						throw new XulRunnerException(MessageFormat.format(
								VpeXulrunnerMessages.XulRunnerBrowser_bundleNotFound,
								getXulRunnerBundle()));
					}
				}

				String xulRunnerVersion = (String) xulRunnerBundle.getHeaders().get("Bundle-Version"); //$NON-NLS-1$
				if (!greRanges[0].check(xulRunnerVersion)) {
					throw new XulRunnerException(MessageFormat.format(VpeXulrunnerMessages.XulRunnerBrowser_wrongVersion, xulRunnerBundle.getLocation() ,XulRunnerBrowser.XULRUNNER_LOWER_VERSION,XulRunnerBrowser.XULRUNNER_HIGHER_VERSION) ); 
				}
				
				
				URL url = xulRunnerBundle.getEntry(XULRUNNER_ENTRY);
				if (url == null) {
					throw new XulRunnerException(MessageFormat.format(VpeXulrunnerMessages.XulRunnerBrowser_bundleDoesNotContainXulrunner, getXulRunnerBundle(),XULRUNNER_ENTRY));  
				}

				try {
					URL url1 = FileLocator.resolve(url);
					xulRunnerFile = new File(FileLocator.toFileURL(url1).getFile());
				} catch (IOException ioe) {
					throw new XulRunnerException(MessageFormat.format(VpeXulrunnerMessages.XulRunnerBrowser_cannotGetPathToXulrunner,getXulRunnerBundle()), ioe); 
				}
				
			xulRunnerPath = xulRunnerFile.getAbsolutePath();
			System.setProperty(XULRUNNER_PATH, xulRunnerPath);
		}

		
		return xulRunnerPath;
	}
		
	/**
	 * Check if embedded XULRunner is not disabled by {@link #EMBEDDED_XULRUNNER_ENABLED}
	 * system property. If it is, then throws a {@link XulRunnerException}.
	 * 
	 * @see <a href="https://issues.jboss.org/browse/JBIDE-9188">JBIDE-9188</a>
	 */
	private static void ensureEmbeddedXulRunnerEnabled() throws XulRunnerException {
		if (!EMBEDDED_XULRUNNER_ENABLED) {
			throw new XulRunnerException(MessageFormat.format(
					VpeXulrunnerMessages.XulRunnerBrowser_embeddedXulRunnerIsDisabledByOption, LOAD_XULRUNNER_SYSTEM_PROPERTY));
		}
	}

	public nsIServiceManager getServiceManager() {
		return mozilla.getServiceManager();
	}
	
	public nsIComponentManager getComponentManager() {
		return mozilla.getComponentManager();
	}
	
	public void setURL(String url) {
		nsIWebNavigation webNavigation = XPCOM.queryInterface(webBrowser, nsIWebNavigation.class);
		webNavigation.loadURI(url, nsIWebNavigation.LOAD_FLAGS_NONE, null, null, null);
	}
	
	public String getURL() {
		nsIWebNavigation webNavigation = XPCOM.queryInterface(webBrowser, nsIWebNavigation.class);
		return webNavigation.getCurrentURI().getSpec();
	}
	
	public void stop() {
		nsIWebNavigation webNavigation = XPCOM.queryInterface(webBrowser, nsIWebNavigation.class);
		webNavigation.stop(nsIWebNavigation.STOP_ALL);
	}
	
	public void reload() {
		nsIWebNavigation webNavigation = XPCOM.queryInterface(webBrowser, nsIWebNavigation.class);
		webNavigation.reload(nsIWebNavigation.LOAD_FLAGS_NONE);
	}
	
	public void goBack() {
		nsIWebNavigation webNavigation = XPCOM.queryInterface(webBrowser, nsIWebNavigation.class);
		webNavigation.goBack();
	}
	
	public void goForward() {
		nsIWebNavigation webNavigation = XPCOM.queryInterface(webBrowser, nsIWebNavigation.class);
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
	
	protected void removeProgressListener(nsIWebProgressListener progressListener){
		 nsIServiceManager serviceManager = mozilla.getServiceManager();
	        nsIWebProgress webProgress = (nsIWebProgress) serviceManager
			.getServiceByContractID("@mozilla.org/docloaderservice;1", //$NON-NLS-1$
				nsIWebProgress.NS_IWEBPROGRESS_IID);
	        try {
	        	webProgress.removeProgressListener(progressListener);
	        } catch(XPCOMException xpcomException) {
	        	
	        	//this exception throws when progress listener already has been deleted, 
	        	//so just ignore if error code NS_ERROR_FAILURE
	        	if(xpcomException.errorcode!=XulRunnerBrowser.NS_ERROR_FAILURE) {
	        		throw xpcomException;
	        	}
	        }
	}
	
	/**
	 * Return {@code true} if and only if the current
	 * platform is officially supported by the Visual Page Editor.
	 * But {@code false} does not necessary mean that XULRunner
	 * cannot be run on the system.
	 * 
	 * @see #CURRENT_PLATFORM_ID
	 * @see #OFFICIALLY_SUPPORTED_PLATFORM_IDS
	 */
	public static boolean isCurrentPlatformOfficiallySupported() {
		return OFFICIALLY_SUPPORTED_PLATFORM_IDS.contains(CURRENT_PLATFORM_ID);
	}
}
