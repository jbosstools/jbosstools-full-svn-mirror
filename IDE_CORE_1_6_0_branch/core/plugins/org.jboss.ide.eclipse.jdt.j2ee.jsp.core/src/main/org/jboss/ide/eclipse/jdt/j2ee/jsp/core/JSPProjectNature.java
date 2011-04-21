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
package org.jboss.ide.eclipse.jdt.j2ee.jsp.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.jboss.ide.eclipse.core.util.ProjectUtil;
import org.jboss.ide.eclipse.jdt.j2ee.core.JDTJ2EECorePlugin;

/**
 * JSP Nature that installs the JSP builder.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPProjectNature implements IProjectNature
{
   private IProject project;

   /**Constructor for the JSPProjectNature object */
   public JSPProjectNature()
   {
      super();
   }

   /**
    * Perform the JSP builder installation.
    *
    * @exception CoreException  Description of the Exception
    */
   public void configure() throws CoreException
   {
      if (!ProjectUtil.projectHasBuilder(this.project, JDTJ2EEJSPCorePlugin.JSP_BUILDER_ID))
      {
         ProjectUtil.addProjectBuilder(this.project, JDTJ2EEJSPCorePlugin.JSP_BUILDER_ID);
      }

      if (!ProjectUtil.projectHasBuilder(this.project, JDTJ2EECorePlugin.WST_VALIDATION_BUILDER_ID))
      {
         ProjectUtil.addProjectBuilder(this.project, JDTJ2EECorePlugin.WST_VALIDATION_BUILDER_ID);
      }
   }

   /**
    * Perform the JSP builder removal.
    *
    * @exception CoreException  Description of the Exception
    */
   public void deconfigure() throws CoreException
   {
      if (ProjectUtil.projectHasBuilder(this.project, JDTJ2EEJSPCorePlugin.JSP_BUILDER_ID))
      {
         ProjectUtil.removeProjectBuilder(this.project, JDTJ2EEJSPCorePlugin.JSP_BUILDER_ID);
      }

      // we won't actually remove the WST validation builder (some people may still want validation without compilation..)
   }

   /**
    * Gets the project attribute of the JSPProjectNature object
    *
    * @return   The project value
    */
   public IProject getProject()
   {
      return this.project;
   }

   /**
    * Sets the project attribute of the JSPProjectNature object
    *
    * @param project  The new project value
    */
   public void setProject(IProject project)
   {
      this.project = project;
   }
}
