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
package org.jboss.ide.eclipse.jdt.aop.core.model;

import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAdvisedCollector;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvised;

/**
 * @author Marshall
 * 
 * Just an empty stub implementation
 */
public class AdvisedCollector implements IAdvisedCollector
{

   /* (non-Javadoc)
    * @see org.jboss.ide.eclipse.jdt.aop.core.model.IAdvisedCollector#collectAdvised(org.jboss.ide.eclipse.jdt.aop.core.model.IAopAdvised)
    */
   public void collectAdvised(IAopAdvised advised)
   {
      // TODO Auto-generated method stub

   }

   /* (non-Javadoc)
    * @see org.eclipse.core.runtime.IProgressMonitor#beginTask(java.lang.String, int)
    */
   public void beginTask(String name, int totalWork)
   {
      // TODO Auto-generated method stub

   }

   /* (non-Javadoc)
    * @see org.eclipse.core.runtime.IProgressMonitor#done()
    */
   public void done()
   {
      // TODO Auto-generated method stub

   }

   /* (non-Javadoc)
    * @see org.eclipse.core.runtime.IProgressMonitor#internalWorked(double)
    */
   public void internalWorked(double work)
   {
      // TODO Auto-generated method stub

   }

   /* (non-Javadoc)
    * @see org.eclipse.core.runtime.IProgressMonitor#isCanceled()
    */
   public boolean isCanceled()
   {
      // TODO Auto-generated method stub
      return false;
   }

   /* (non-Javadoc)
    * @see org.eclipse.core.runtime.IProgressMonitor#setCanceled(boolean)
    */
   public void setCanceled(boolean value)
   {
      // TODO Auto-generated method stub

   }

   /* (non-Javadoc)
    * @see org.eclipse.core.runtime.IProgressMonitor#setTaskName(java.lang.String)
    */
   public void setTaskName(String name)
   {
      // TODO Auto-generated method stub

   }

   /* (non-Javadoc)
    * @see org.eclipse.core.runtime.IProgressMonitor#subTask(java.lang.String)
    */
   public void subTask(String name)
   {
      // TODO Auto-generated method stub

   }

   /* (non-Javadoc)
    * @see org.eclipse.core.runtime.IProgressMonitor#worked(int)
    */
   public void worked(int work)
   {
      // TODO Auto-generated method stub

   }

   /* (non-Javadoc)
    * @see org.jboss.ide.eclipse.jdt.aop.core.model.IAdvisedCollector#handleException(java.lang.Exception)
    */
   public void handleException(Exception e)
   {
      // TODO Auto-generated method stub

   }

}
