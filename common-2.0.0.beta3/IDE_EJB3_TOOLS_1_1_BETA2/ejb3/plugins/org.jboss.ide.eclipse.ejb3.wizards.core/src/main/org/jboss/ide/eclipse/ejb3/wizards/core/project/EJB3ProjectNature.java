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
package org.jboss.ide.eclipse.ejb3.wizards.core.project;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;

/**
 * @author Marshall
 */
public class EJB3ProjectNature implements IProjectNature
{

   public static final String NATURE_ID = "org.jboss.ide.eclipse.ejb3.wizards.core.EJB3ProjectNature";

   private IProject project;

   public EJB3ProjectNature()
   {
   }

   public void configure() throws CoreException
   {

   }

   public void deconfigure() throws CoreException
   {
   }

   public IProject getProject()
   {
      return this.project;
   }

   public void setProject(IProject project)
   {
      this.project = project;
   }

   public static void ensureAopProjectNature(IJavaProject project)
   {
      boolean added = AopCorePlugin.addProjectNature(project.getProject(), NATURE_ID);
   }
}
