/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.launcher.core.logfiles;

import org.eclipse.debug.core.ILaunchConfiguration;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   18 mai 2003
 */
public class LogFile implements Comparable
{
   /** Description of the Field */
   protected ILaunchConfiguration configuration;
   /** Description of the Field */
   protected String fileName;
   /** Description of the Field */
   protected int pollingIntervall;
   /** Description of the Field */
   public final static int DEFAULT_POLLING_INTERVALL = 0;


   /**
    * Constructor for LogFile.
    *
    * @param configuration     Description of the Parameter
    * @param fileName          Description of the Parameter
    * @param pollingIntervall  Description of the Parameter
    */
   public LogFile(ILaunchConfiguration configuration, String fileName, int pollingIntervall)
   {
      super();
      this.configuration = configuration;
      this.fileName = fileName;
      this.pollingIntervall = pollingIntervall;
   }


   /**
    * @param o  Description of the Parameter
    * @return   Description of the Return Value
    * @see      java.lang.Comparable#compareTo(Object)
    */
   public int compareTo(Object o)
   {
      return this.toString().compareTo(o.toString());
   }


   /**
    * Returns the configuration.
    *
    * @return   ILaunchConfiguration
    */
   public ILaunchConfiguration getConfiguration()
   {
      return configuration;
   }


   /**
    * Returns the fileName.
    *
    * @return   String
    */
   public String getFileName()
   {
      return fileName;
   }


   /**
    * Returns the pollingIntervall.
    *
    * @return   int
    */
   public int getPollingIntervall()
   {
      return pollingIntervall;
   }


   /**
    * Sets the configuration.
    *
    * @param configuration  The configuration to set
    */
   public void setConfiguration(ILaunchConfiguration configuration)
   {
      this.configuration = configuration;
   }


   /**
    * Sets the fileName.
    *
    * @param fileName  The fileName to set
    */
   public void setFileName(String fileName)
   {
      this.fileName = fileName;
   }


   /**
    * Sets the pollingIntervall.
    *
    * @param pollingIntervall  The pollingIntervall to set
    */
   public void setPollingIntervall(int pollingIntervall)
   {
      this.pollingIntervall = pollingIntervall;
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public String toString()
   {
      return fileName + " " + pollingIntervall;//$NON-NLS-1$
   }
}
