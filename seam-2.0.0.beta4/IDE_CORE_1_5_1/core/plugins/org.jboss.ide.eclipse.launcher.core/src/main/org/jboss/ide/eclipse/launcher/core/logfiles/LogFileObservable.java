/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.ide.eclipse.launcher.core.logfiles;

import java.io.File;

import org.jboss.ide.eclipse.launcher.core.util.LogUtil;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   18 mai 2003
 */
public class LogFileObservable
{
   /** Description of the Field */
   protected ChangeCheckerRunnable changeCheckerRunnable = new ChangeCheckerRunnable();

   /** Description of the Field */
   protected File file;

   /** Description of the Field */
   protected ILogFileListener fileListener = null;

   /** Description of the Field */
   protected long lastModified;

   /** Description of the Field */
   protected boolean observing;

   /** Description of the Field */
   protected int pollingIntervall = 0;

   /** Description of the Field */
   protected boolean threadOn = false;

   /**
    * Constructor for LogFileObservable.
    *
    * @param fileListener      Description of the Parameter
    * @param file              Description of the Parameter
    * @param pollingIntervall  Description of the Parameter
    */
   public LogFileObservable(ILogFileListener fileListener, File file, int pollingIntervall)
   {
      super();
      if (file == null || fileListener == null)
      {
         throw new IllegalArgumentException();
      }
      setPollingIntervall(pollingIntervall);
      lastModified = file.lastModified();
      this.file = file;
      this.fileListener = fileListener;
      threadOn = false;
   }

   /**
    * Returns the file.
    *
    * @return   File
    */
   public File getFile()
   {
      return file;
   }

   /**
    * Returns the fileListener.
    *
    * @return   ILogFileListener
    */
   public ILogFileListener getFileListener()
   {
      return fileListener;
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
    * Gets the observing attribute of the LogFileObservable object
    *
    * @return   The observing value
    */
   public boolean isObserving()
   {
      return observing;
   }

   /**
    * Returns the threadOn.
    *
    * @return   boolean
    */
   public boolean isThreadOn()
   {
      return threadOn;
   }

   /**
    * Sets the pollingIntervall.
    *
    * @param pollingIntervall  The pollingIntervall to set
    */
   public void setPollingIntervall(int pollingIntervall)
   {
      if (!LogUtil.isValidPollingIntervall(pollingIntervall))
      {
         throw new IllegalArgumentException();
      }
      this.pollingIntervall = pollingIntervall;
   }

   /**
    * Sets the threadOn.
    *
    * @param threadOn  The threadOn to set
    */
   public void setThreadOn(boolean threadOn)
   {
      if (threadOn != this.threadOn)
      {
         this.threadOn = threadOn;
         if (threadOn == true && pollingIntervall != 0)
         {
            Thread thread = new Thread(changeCheckerRunnable);
            thread.setDaemon(true);
            thread.start();
         }
      }
   }

   /**
    * Description of the Class
    *
    * @author    Administrator
    * @version   $Revision$
    * @created   18 mai 2003
    */
   class ChangeCheckerRunnable implements Runnable
   {
      /**
       * @see   java.lang.Runnable#run()
       */
      public void run()
      {
         // Thread me = Thread.currentThread();
         while (threadOn)
         {
            observing = true;
            if (file.lastModified() != lastModified)
            {
               lastModified = file.lastModified();
               fileListener.fileChanged(file);
            }
            try
            {
               Thread.sleep(pollingIntervall * 1000);
            }
            catch (InterruptedException e)
            {
            }
         }
         observing = false;
      }
   }
}
