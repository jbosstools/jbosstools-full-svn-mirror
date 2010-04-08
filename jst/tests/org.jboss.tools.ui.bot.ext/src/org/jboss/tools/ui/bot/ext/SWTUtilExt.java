 /*******************************************************************************
  * Copyright (c) 2007-2009 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.ui.bot.ext;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.jboss.tools.ui.bot.ext.parts.SWTBotBrowserExt;
import org.jboss.tools.ui.bot.ext.types.JobLists;
import org.jboss.tools.ui.bot.ext.types.JobState;
import org.osgi.framework.Bundle;

/**
 * Base class for all classes using SWTBot
 * 
 * @author jpeterka
 * 
 */
public class SWTUtilExt extends SWTUtils {

	private Logger log = Logger.getLogger(SWTUtilExt.class);
	protected SWTWorkbenchBot bot;

	public SWTUtilExt(SWTWorkbenchBot bot) {
		this.bot = bot;
	}
	
	// ------------------------------------------------------------
	// Waiting methods
	// ------------------------------------------------------------

	// Constants
	final int TIMEOUT = 20000; // 10s
	final int SLEEPTIME = 1000; // 0.5s

	public void waitForBrowserLoadsPage(SWTBotBrowserExt browser) {
		waitForBrowserLoadsPage(browser, TIMEOUT);
	}
	/**
	 * waits until given browser finishes with loading page
	 * @param browser
	 * @param timeOut
	 */
	public void waitForBrowserLoadsPage(SWTBotBrowserExt browser, long timeOut) {

		long startTime = System.currentTimeMillis();
		while (true) {
			if (browser.isPageLoaded()) {
				log.info("Page load finished");
				break;
			}

			long waitTime = System.currentTimeMillis() - startTime;
			if ((System.currentTimeMillis() - startTime) > timeOut) {
				log.info("Waiting for browser loading page " 
				  + timeOut + "s");
				break;
			}
			log.info("Browser is loading page for " + waitTime
					/ 1000 + "s");
			bot.sleep(SLEEPTIME);
		}
		
	}
	/**
	 * Wait for named running jobs with defined TIMEOUT
	 */
	public void waitForJobs(String... jobNames) {
		waitForJobs(TIMEOUT, jobNames);
	}

	/**
	 * Wait for all running jobs not named in JobList.ignoredJobs 
	 * @param timeOut
	 */
	public void waitForNonIgnoredJobs(long timeOut) {
		waitForAllExcept(timeOut,JobLists.ignoredJobs);
	}
  /**
   * Wait for all running jobs not named in JobList.ignoredJobs 
   */
  public void waitForNonIgnoredJobs() {
    waitForAllExcept(TIMEOUT,JobLists.ignoredJobs);
  }

	/**
	 * Wait for all running jobs
	 */
	public void waitForAll(long timeOut) {
		waitForAllExcept(timeOut,new String[0]);
	}
  /**
   * Wait for all running jobs
   * @param timeOut
   */
  public void waitForAll() {
    waitForAllExcept(TIMEOUT,new String[0]);
  }
	/**
	 * Wait for all running jobs except named jobs
	 * @param timeOut
	 * @param jobNames
	 */
	public void waitForAllExcept(long timeOut,String... jobNames) {

		// Find all jobs
		Job[] jobs = Job.getJobManager().find(null);
		List<String> listNames = new ArrayList<String>();

		for (Job job : jobs) {
			listNames.add(job.getName());
		}

		// Remove ignored jobs
		for (String jobName : jobNames) {
			if (listNames.contains(jobName))
				listNames.remove(jobName);
		}

		// Create rest job list
		String[] names = new String[listNames.size()];
		for (int i = 0; i < listNames.size(); i++) {
			names[i] = listNames.get(i);
		}

		waitForJobs(timeOut,names);
	}

	/**
	 * Waits for selected job
	 * @param timeOut
	 * @param jobNames
	 */
	public void waitForJobs(long timeOut, String... jobNames) {

		// DEBUG
		printRunningJobs();

		// No Jobs
		if (jobNames == null || jobNames.length == 0) {
			log.info("No jobs prescribed as blocking");
			delay();
			return;
		}

		// Jobs prescribed
		long startTime = System.currentTimeMillis();
		List<String> blockingJobs = new ArrayList<String>();

		// Go through jobs and wait for completion
		for (String jobName : jobNames) {
			if (isJobRunning(jobName)) {
				log.info("Blocking job " + jobName + " found");
				blockingJobs.add(jobName);
			}
		}

		// Wait until all blocking jobs aren't finished or timeout
		for (String jobName : blockingJobs) {
			while (true) {
				if (!isJobRunning(jobName)) {
					log.info("Job  " + jobName + " is finished");
					break;
				}

				long waitTime = System.currentTimeMillis() - startTime;
				if ((System.currentTimeMillis() - startTime) > timeOut) {
					log.info("Waiting for job " + jobName + " timeOut " 
					  + timeOut + "s");
					break;
				}
				log.info("Job \"" + jobName + "\" is running for " + waitTime
						/ 1000 + "s");
				bot.sleep(SLEEPTIME);
			}
		}

		log.info("All blocking jobs finished or skipped");
	}

	/**
	 * Search for jobname in JobManager job list
	 * 
	 * @param jobName
	 *            name of the job
	 * @return true if job with corresponding name found, else false
	 */
	private boolean isJobRunning(String jobName) {
		Job[] jobs = Job.getJobManager().find(null);
		for (Job job : jobs) {
			if ((jobName.equalsIgnoreCase(job.getName()))
					&& ((job.getState() == JobState.RUNNING) || (job.getState() == JobState.WAITING)))
				return true;
		}
		return false;
	}

	public void printRunningJobs() {
		Job[] jobs = Job.getJobManager().find(null);
		for (Job job : jobs) {

			String jobStateName = JobState.getStateName(job.getState());

			log.info("Active Job: P:" + job.getPriority() + "\" S:"
					+ jobStateName + "\" R:" + job.getResult() + "\" N:\""
					+ job.getName() + "\"");
		}
	}

	public void delay() {
		bot.sleep(500);
	}

	// ------------------------------------------------------------
	// Resource methods
	// ------------------------------------------------------------

	/**
	 * Get resource file
	 */
	public File getResourceFile(String pluginId, String... path) {

		// Construct path
		StringBuilder builder = new StringBuilder();
		for (String fragment: path) {
			builder.append("/" + fragment);
		}
		
		String filePath = "";
		try {
			filePath = FileLocator.toFileURL(
					Platform.getBundle(pluginId).getEntry("/"))
					.getFile() + "resources" + builder.toString();
			File file = new File(filePath);
			if (!file.isFile()) {
				filePath = FileLocator.toFileURL(
						Platform.getBundle(pluginId).getEntry("/"))
						.getFile() + builder.toString();
			}
		} catch (IOException ex) {
			String message = filePath + " resource file not found";
			log.error(message);
			fail(message);
		}

		File file = new File(filePath);
		return file;
	}

	/**
	 * Reads text file
	 * 
	 * @param file
	 * @return
	 */
	public String readTextFile(File file) {

		StringBuilder contents = new StringBuilder();

		try {
			BufferedReader input =  new BufferedReader(new FileReader(file));
			try {
				String line = null; //not declared within while loop
				while (( line = input.readLine()) != null){
					contents.append(line);
					contents.append(System.getProperty("line.separator"));
				}
			}
			finally {
				input.close();
			}
		}
		catch (IOException ex){
			ex.printStackTrace();
		}

		return contents.toString();
	}
	
	/**
	 * Loads project property file for particular test plugin
	 * @param pluginId
	 * @return
	 */
	public Properties loadProperties(String pluginId) {
		Properties properties = new Properties();
		try {
			log.info("Loading properties for " + pluginId);
			// Read project properties
			Bundle bundle = Platform.getBundle(pluginId);
			InputStream is = bundle.getResource("project.properties").openStream();			
			properties.load(is);
			log.info("Properties for " + pluginId + " loaded:");
		
		} catch (Exception ex) {
			logAndFail("Problem with loading properties file");
		}
		return properties;
	}

	/**
	 * Get value from property file with error logging
	 * @param properties
	 * @param key
	 * @return
	 */
	public String getValue(Properties properties, String key) {
		String value = properties.getProperty(key);
		if ((value == null) || value.equalsIgnoreCase("")) {
			logAndFail("Missing property value for key \"" + key
					+ "\"");
		}
		return value;
	}

	/**
	 * Check Property values
	 * @param properties
	 * @param key
	 */
	public void checkAndLogValue(Properties properties, String key) {
		log.info(key + "=" + getValue(properties, key));
	}
	
	/**
	 * Log and fail 
	 * @param msg
	 */
	public void logAndFail(String msg) {
		log.error(msg);
		fail(msg);
	}
	/**
	 * Write all running processes names to log
	 */
	public void logAllRunningProcesses(){
    Job[] jobs = Job.getJobManager().find(null);

    for (Job job : jobs) {
      log.info(job.getName());
    }
	}
	/**
   * Write all menu items of menu to log
   * @param menu
   */
  public void logAllSubmenus(MenuItem menuItem){
    
    final MenuItem miTmp = menuItem;
    
    menuItem.getDisplay().asyncExec(new Runnable() {
      public void run() {
        int index = 0;
        for (MenuItem miSubmenu : miTmp.getMenu().getItems()){
          log.info(index++ + ": " + miSubmenu);
        }
        
      }
    });
    
  }
  /**
   * Returns Test Plugin Location within file system
   * @param projectName
   * @return
   */
  public static String getTestPluginLocation(String projectName){
    
    String[] parts = System.getProperty("eclipse.commands").split("\n");

    int index = 0;
    
    for (index = 0;parts.length > index + 1 && !parts[index].equals("-data");index++){
      // do nothing just go through
    }
    
    return parts[index + 1] + File.separator + projectName; 
    
  }
  /**
   * Returns true if shell with shellTitle is active
   * @param shellTitle
   * @param bot
   * @return
   */
  public static boolean isShellActive(String shellTitle, SWTWorkbenchBot bot){
    boolean isShellActive = false;
    try {
      bot.shell(shellTitle).activate();
      isShellActive = true;
    } catch (WidgetNotFoundException e) {
    }
    return isShellActive;
  }
  /**
   * Returns true if shell with shellTitle is active
   * @param shellTitle
   * @return
   */
  public boolean isShellActive(String shellTitle){
    return SWTUtilExt.isShellActive(shellTitle,bot);
  }
  /**
   * Closes shell with shellTitle if shell is active
   * @param shellTitle
   * @param bot
   * @return
   */
  public static void closeShellWhenActive(String shellTitle, SWTWorkbenchBot bot){
    try {
      bot.shell(shellTitle).close();
    } catch (WidgetNotFoundException e) {
    }
  }
  /**
   * Closes shell with shellTitle if shell is active
   * @param shellTitle
   * @return
   */
  public void closeShellWhenActive(String shellTitle){
    SWTUtilExt.closeShellWhenActive(shellTitle,bot);
  }
  /**
   * Returns Properties which contains Virtual Machine arguments
   * with name starting with "-D"
   * @return
   */
  public static Properties parseEclipseVMArgs (){
    
    Properties vmArgsProps = new Properties();
    
    String vmArgs = System.getProperty("eclipse.vmargs");
    
    if (vmArgs != null){
      for (String line : vmArgs.split("\n")){
        if (line.startsWith("-D")){
          String[] splitLine = line.split("=");
          vmArgsProps.setProperty(splitLine[0], splitLine[1]);
        }
      }
    }
    
    return vmArgsProps;
    
  }
  /**
   * Overrides propertyName property value within properties with value stored within vmargProperties with name vmargPropertyName
   * @param properties
   * @param propertyName
   * @param vmargPropertyName
   * @param vmargProperties
   */
  public static void overrideValueFromSystemProperty (Properties properties, String propertyName , 
    String vmargPropertyName, Properties vmargProperties){
    
    String vmargProperty = vmargProperties.getProperty(vmargPropertyName);
    if (vmargProperty != null){
      properties.setProperty(propertyName, vmargProperty);
    }
    
  }
  /**
   * Delete directory from directoryLocation
   * @param directoryLocation
   */
  public static void deleteDirectory(String directoryLocation){
    File directory = new File (directoryLocation);
    if (directory.exists()){
      if (directory.isDirectory()){
        for (File childFile :directory.listFiles()) {
          SWTUtilExt.deleteDirectory(childFile.getAbsolutePath());
        }
        directory.delete();
      }
      else{
        directory.delete();
      }
    }
  }
}