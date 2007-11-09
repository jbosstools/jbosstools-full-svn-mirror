package org.jboss.tools.vpe.xulrunner.browser;

import org.mozilla.interfaces.nsISupports;
import org.mozilla.interfaces.nsIURI;
import org.mozilla.interfaces.nsIWebBrowserChrome;
import org.mozilla.interfaces.nsIWindowCreator;
import org.mozilla.interfaces.nsIWindowCreator2;
import org.mozilla.xpcom.Mozilla;

public class WindowCreator implements nsIWindowCreator, nsIWindowCreator2 {

	public nsIWebBrowserChrome createChromeWindow(nsIWebBrowserChrome arg0,
			long arg1) {
		// TODO Sergey Vasilyev implement
		System.out.println("WindowCreator:createChromeWindow(" + arg0 +", " + arg1 + ")");
		return arg0;
	}

	public nsISupports queryInterface(String arg0) {
		// TODO Auto-generated method stub
		return Mozilla.queryInterface(this, arg0);
	}

	public nsIWebBrowserChrome createChromeWindow2(nsIWebBrowserChrome arg0,
			long arg1, long arg2, nsIURI arg3, boolean[] arg4) {
		// TODO Sergey Vasilyev implement
		System.out.println("WindowCreator:createChromeWindow2(" + arg0 +", "
				+ arg1 + ", "+ arg2 + ", "+ (arg3 == null ? "null" : arg3.getSpec()) +")");
		return arg0;
	}

}
