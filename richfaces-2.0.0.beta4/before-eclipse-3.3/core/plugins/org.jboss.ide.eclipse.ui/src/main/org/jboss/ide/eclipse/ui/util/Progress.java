/*
 * JBoss, a division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
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
package org.jboss.ide.eclipse.ui.util;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;

/**
 * This aims to simplify the use of a Progress Monitor.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class Progress
{
   /** The runnable to launch */
   private IRunnableWithProgress runnable;

   /** The container shell */
   private Shell shell;

   /**
    * Build a Progress object
    *
    * @param shell     The container shell
    * @param runnable  The runnable to launch
    */
   public Progress(Shell shell, IRunnableWithProgress runnable)
   {
      this.runnable = runnable;
      this.shell = shell;
   }

   /**
    * Launch the ProgressDialog and run the runnable
    *
    * @exception InvocationTargetException  Exception when trying to invoke the target
    * @exception InterruptedException       Exception when interrupting
    */
   public void run() throws InvocationTargetException, InterruptedException
   {
      // Launch the progress monitor
      ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(Progress.this.shell);
      progressMonitorDialog.run(true, true, this.runnable);
   }
}
