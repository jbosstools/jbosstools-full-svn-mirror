/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.launcher.core.logfiles;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.jface.text.Document;
import org.eclipse.swt.widgets.Display;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.launcher.core.util.LogUtil;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   18 mai 2003
 */
public class LogFileDocument extends Document implements ILogFileListener
{
   /** Description of the Field */
   protected char[] buffer;
   /** Description of the Field */
   protected File file;
   /** Description of the Field */
   protected LogFileObservable fileObservable;
   /** Description of the Field */
   protected boolean monitor = false;
   /** Description of the Field */
   protected FileReader reader;


   /**
    * Constructor for LogFileDocument.
    *
    * @param file              Description of the Parameter
    * @param pollingIntervall  Description of the Parameter
    * @exception IOException   Description of the Exception
    */
   public LogFileDocument(File file, int pollingIntervall) throws IOException
   {
      super();
      if (file == null || !LogUtil.isValidPollingIntervall(pollingIntervall))
      {
         throw new IllegalArgumentException();
      }
      set(readFile(file));
      fileObservable = new LogFileObservable(this, file, pollingIntervall);
      this.file = file;
   }


   /**
    * @param file  Description of the Parameter
    * @see         org.rocklet.monitoring.logfiles.ILogFileListener#fileChanged(File)
    */
   public void fileChanged(final File file)
   {
      Runnable r =
         new Runnable()
         {
            public void run()
            {
               try
               {
                  set(readFile(file));
               }
               catch (IOException e)
               {
                  AbstractPlugin.log(e);
               }
            }
         };
      if (Display.getDefault() != null)
      {
         Display.getDefault().asyncExec(r);
      }
   }


   /**
    * Returns the monitor.
    *
    * @return   boolean
    */
   public boolean isMonitor()
   {
      return monitor;
   }


   /**
    * Description of the Method
    *
    * @param file             Description of the Parameter
    * @return                 Description of the Return Value
    * @exception IOException  Description of the Exception
    */
   public String readFile(File file) throws IOException
   {
      buffer = new char[(int) file.length()];
      if (file.length() > 0)
      {
         (new FileReader(file)).read(buffer, 0, (int) file.length());
      }
      return (new String(buffer));
   }


   /**
    * Sets the monitor.
    *
    * @param monitor          The monitor to set
    * @exception IOException  Description of the Exception
    */
   public void setMonitor(boolean monitor) throws IOException
   {
      this.monitor = monitor;
      fileObservable.setThreadOn(monitor);
      if (monitor)
      {
         set(readFile(file));
      }
   }


   /**
    * Description of the Method
    *
    * @exception IOException  Description of the Exception
    */
   public void synchronize() throws IOException
   {
      set(readFile(file));
   }
}
