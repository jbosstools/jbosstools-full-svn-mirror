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
package org.jboss.ide.eclipse.xdoclet.run.builder;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.jboss.ide.eclipse.xdoclet.run.XDocletRunPlugin;

/**
 * This builder replaces the need to re-export the .xdoclet file to an actual ant script that runs XDoclet.
 */
public class XDocletRunBuilder extends IncrementalProjectBuilder
{

   public static final String BUILDER_ID = "org.jboss.ide.eclipse.xdoclet.run.XDocletRunBuilder";

   protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException
   {

      generateXDocletRunScript();

      return null;
   }

   private void generateXDocletRunScript()
   {
      IProject project = getProject();
      try
      {
         XDocletRunPlugin.getDefault().createBuildFile(JavaCore.create(project));
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

}
