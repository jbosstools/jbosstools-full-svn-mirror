package org.jboss.tools.usage.test;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.Platform;
import org.jboss.tools.usage.googleanalytics.eclipse.EclipseUserAgent;

public class EclipseUserAgentFake extends EclipseUserAgent {

	private static final String APPLICATION_NAME = "com.jboss.jbds.product";
	private static final String APPLICATION_VERSION = "3.0.1";
	public static final String LOCALE_US = "en_US";

	static final String WIN2000_VERSION = "5.0";
	static final String WINXP_VERSION = "5.1";
	static final String VISTA_VERSION = "6.0";
	static final String WIN7_VERSION = "6.1";
	static final String MACLEOPARD_VERSION = "10.5";
	static final String MACSNOWLEOPARD_VERSION = "10.6";
	
	private String applicationName;
	private String applicationVersion;
	private String nl;
	private String os;
	private String osVersion;

	public EclipseUserAgentFake() {
		this(APPLICATION_NAME, APPLICATION_VERSION, LOCALE_US, Platform.OS_WIN32, WIN7_VERSION);
	}
	
	public EclipseUserAgentFake(String nl) {
		this(APPLICATION_NAME, APPLICATION_VERSION, nl, Platform.OS_WIN32, WIN7_VERSION);
	}

	public EclipseUserAgentFake(String nl, String os, String osVersion) {
		this(APPLICATION_NAME, APPLICATION_VERSION, nl, os, osVersion);
	}

	/**
	 * Instantiates a fake of the EclipseUserAgent class
	 * 
	 * @param applicationName the name of the applicaiton
	 * @param applicationVersion the version of the application
	 * @param nl the platform locale as returned from <code>Platform.getNL()</code>
	 * @param os the os name as return from <code>Platform.getOS()</code>
	 * @param osVersion the version of the os as returned from <code>System.getProperty("os.name")</code>
	 * 
	 * @see Platform#getNL()
	 * @see Platform#getOS()
	 * @see System#getProperty("os.version")
	 */
	public EclipseUserAgentFake(String applicationName, String applicationVersion, String nl, String os, String osVersion) {
		Assert.isTrue(nl != null && nl.indexOf(JAVA_LOCALE_DELIMITER) >= 0, "nl parameter must for a java locale string <xx_XX>");
		this.applicationName = applicationName;
		this.applicationVersion = applicationVersion;
		this.nl = nl;
		this.os = os;
		this.osVersion = osVersion;
	}

	@Override
	protected String getNL() {
		return nl;
	}

	@Override
	protected String getApplicationName() {
		return applicationName;
	}

	@Override
	protected String getOS() {
		return os;
	}

	@Override
	protected String getOSVersion() {
		return osVersion;
	}
	
	@Override
	protected String getApplicationVersion() {
		return applicationVersion;
	}
}