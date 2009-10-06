package org.jboss.tools.ui.bot.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;

public abstract class JBTSWTBotTestCase extends SWTBotTestCase implements
		ILogListener{

	protected static final String BUILDING_WS = "Building workspace";
	protected static final String VISUAL_UPDATE = "Visual Editor View Update";
	protected static final String VISUAL_REFRESH = "Visual Editor Refresh";
	private volatile Throwable exception;
	public static final String PATH_TO_SWT_BOT_PROPERTIES = "SWTBot.properties";
	private static Properties SWT_BOT_PROPERTIES;
	protected SWTJBTBot bot = new SWTJBTBot();
	private int sleepTime = 5000;
	
	/* (non-Javadoc)
	 * This static block read properties from 
	 * org.jboss.tools.ui.bot.test/resources/SWTBot.properties file
	 * and set up parameters for SWTBot tests. You may change a number of parameters
	 * in static block and their values in property file.
	 */
	
	static {
//		try {
//			InputStream swtPreferenceIS = Platform.getBundle(Activator.PLUGIN_ID).getResource(PATH_TO_SWT_BOT_PROPERTIES)
//					.openStream();
//			SWT_BOT_PROPERTIES = new Properties();
//			SWT_BOT_PROPERTIES.load(swtPreferenceIS);
//			SWTBotPreferences.PLAYBACK_DELAY = Long
//					.parseLong(SWT_BOT_PROPERTIES
//							.getProperty("SWTBotPreferences.PLAYBACK_DELAY"));
//			SWTBotPreferences.TIMEOUT = Long.parseLong(SWT_BOT_PROPERTIES
//					.getProperty("SWTBotPreferences.TIMEOUT"));
//			swtPreferenceIS.close();
//		} catch (IOException e) {
//			fail("Can't load properties from " + PATH_TO_SWT_BOT_PROPERTIES + " file");
//		} catch (IllegalStateException e) {
//			fail("Property file " + PATH_TO_SWT_BOT_PROPERTIES + " was not found");
//		}
	}

	public void logging(IStatus status, String plugin) {
		switch (status.getSeverity()) {
		case IStatus.ERROR:
			Throwable throwable = status.getException();
			if (throwable == null) {
				throwable = new Throwable(status.getMessage() + " in "
						+ status.getPlugin());
			}
			setException(throwable);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Getter method for exception that may be thrown during test
	 * execution.<p>
	 * You can call this method from any place of your methods
	 * and verify if any exception was thrown during test executing
	 * including Error Log.
	 * @return exception
	 * @see Throwable
	 */
	
	protected synchronized Throwable getException() {
		return exception;
	}

	/**
	 * Setter method for exception.
	 * If param is not null test will fail and you will see stack trace in JUnit Error Log
	 * @param e - exception, that can be frown during test executing
	 * @see Throwable
	 */
	
	protected synchronized void setException(Throwable e) {
		this.exception = e;
	}

	/**
	 * Delete .log file from junit-workspace .metadata, if it hadn't been deleted before<p>
	 * So we can catch exceptions and errors, which were thrown during current test.
	 */
	
	private void deleteLog() {
		try {
			Platform.getLogFileLocation().toFile().delete();
		} catch (Exception e) {
		}
	}

	/**
	 * Make a default preconditions before test launch
	 * @see #activePerspective()
	 * @see #openErrorLog()
	 * @see #openPackageExplorer()
	 * @see #setException(Throwable)
	 * @see #deleteLog()
	 * @see #delay()
	 */
	
	@Override
	protected void setUp() throws Exception {
		activePerspective();
		try {
			bot.viewByTitle(WidgetVariables.WELCOME).close();
		} catch (WidgetNotFoundException e) {
		}
		openErrorLog();
		openPackageExplorer();
//		openProgressStatus();
		deleteLog();
		setException(null);
		Platform.addLogListener(this);
//		delay();
	}

	/**
	 * Tears down the fixture. Verify Error Log.
	 * @see #getException()
	 */
	
	@Override
	protected void tearDown() throws Exception {
		Platform.removeLogListener(this);
		deleteLog();
		if (getException() != null) {
			throw new Exception(getException());
		}
	}

	/**
	 * A little delay between test's steps. Use it where necessary.
	 */
	
	protected void delay() {
		bot.sleep(sleepTime);
	}
	
	/** Defines which kind of perspective should be activated before tests' run.
	 */
	
	abstract protected void activePerspective ();
	
	/**
	 * Open and activate Error Log view if it hadn't been opened before
	 */
	
	protected void openErrorLog() {
		try {
			bot.viewByTitle(WidgetVariables.ERROR_LOG);
		} catch (WidgetNotFoundException e) {
			bot.menu("Window").menu("Show View").menu("Other...").click();
			SWTBotTree viewTree = bot.tree();
			viewTree.expandNode("General")
					.expandNode(WidgetVariables.ERROR_LOG).select();
			bot.button("OK").click();
		}
	}
	
	/**
	 * Open and activate Package Explorer view if it hadn't been opened before
	 */
	
	protected void openPackageExplorer() {
		try {
			bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).setFocus();
		} catch (WidgetNotFoundException e) {
			bot.menu("Window").menu("Show View").menu("Other...").click();
			SWTBotTree viewTree = bot.tree();
			viewTree.expandNode("Java").expandNode(
					WidgetVariables.PACKAGE_EXPLORER).select();
			bot.button("OK").click();
		}
	}
	
//	protected void openProgressStatus() {
//		try {
//			bot.viewByTitle(WidgetVariables.PROGRESS_STATUS);
//		} catch (WidgetNotFoundException e) {
//			bot.menu("Window").menu("Show View").menu("Other...").click();
//			SWTBotTree viewTree = bot.tree();
//			delay();
//			viewTree.expandNode("General").expandNode(WidgetVariables.PROGRESS_STATUS).select();
//			bot.button("OK").click();
//		}
//	}
	
	/**
	 * Use delay() method instead
	 * @see #delay()
	 */
	@Deprecated 
	protected final void waitForJobs(){
		delay();
	}
	
	protected final void waitForBlockingJobsAcomplished(long timeOut, String... jobNames) throws InterruptedException{
		if (jobNames == null) {
			delay();
		} else {
			boolean isProcessStarted = false;
			long startTime = System.currentTimeMillis();
			while (!isProcessStarted) {
				Job[] jobs = Job.getJobManager().find(null);
				for (Job job : jobs) {
					for (String jobName : jobNames) {
						if (jobName.equalsIgnoreCase(job.getName())) {
							isProcessStarted = true;
						}
					}
				}
				long endTime = System.currentTimeMillis();
				if (endTime-startTime>timeOut) {
					throw new InterruptedException(stringArrayToString(jobNames) + "job(s) has never appeared or already completed");
				}
			}
			while (isProcessStarted) {
				isProcessStarted = false;
				Job[] jobs = Job.getJobManager().find(null);
				for (Job job : jobs) {
					for (String jobName : jobNames) {
						if (jobName.equalsIgnoreCase(job.getName())) {
							delay();
							isProcessStarted = true;
						}
					}
				}
			}
		}
	}
	
	protected final void waitForBlockingJobsAcomplished(String... jobNames) throws InterruptedException{
		waitForBlockingJobsAcomplished(5*1000L, jobNames);
	}
	
	private String stringArrayToString (String... strings){
		StringBuffer buffer = new StringBuffer("");
		for (String string : strings) {
			buffer.append(string+", ");
		}
		return buffer.toString();
	}
	
}
