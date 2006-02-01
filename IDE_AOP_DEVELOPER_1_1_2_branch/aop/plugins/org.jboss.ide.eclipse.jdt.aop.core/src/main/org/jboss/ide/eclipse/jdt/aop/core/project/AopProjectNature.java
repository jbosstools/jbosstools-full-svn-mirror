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
package org.jboss.ide.eclipse.jdt.aop.core.project;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.AopDescriptor;

public class AopProjectNature implements IProjectNature
{

   public static final String NATURE_ID = "org.jboss.ide.eclipse.jdt.aop.core.AopProjectNature";

   private IProject project;

   public AopProjectNature()
   {
   }

   public void configure() throws CoreException
   {

      IProjectDescription desc = project.getDescription();
      ICommand[] commands = desc.getBuildSpec();

      //add builders to project
      ICommand builderCommand = desc.newCommand();
      builderCommand.setBuilderName(AopProjectBuilder.BUILDER_ID);

      //ICommand preBuilderCommand = desc.newCommand();
      //preBuilderCommand.setBuilderName(AopProjectPreBuilder.BUILDER_ID);

      ICommand[] newCommands = new ICommand[commands.length + 1];
      System.arraycopy(commands, 0, newCommands, 0, commands.length);

      //newCommands[0] = preBuilderCommand;
      newCommands[newCommands.length - 1] = builderCommand;

      desc.setBuildSpec(newCommands);

      try
      {
         project.setDescription(desc, new NullProgressMonitor());
      }
      catch (CoreException e)
      {
         e.printStackTrace();
      }

      System.out.println("finished configuring");

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
      IJavaProject javaProject = JavaCore.create(project);
      AopCorePlugin.setCurrentJavaProject(javaProject);

      this.project = project;
   }

   public static void ensureAopProjectNature(IJavaProject project)
   {
      boolean added = AopCorePlugin.addProjectNature(project.getProject(), NATURE_ID);

      if (added)
      {
         AopDescriptor descriptor = AopCorePlugin.getDefault().getDefaultDescriptor(project);
         descriptor.save();
      }
   }
}
