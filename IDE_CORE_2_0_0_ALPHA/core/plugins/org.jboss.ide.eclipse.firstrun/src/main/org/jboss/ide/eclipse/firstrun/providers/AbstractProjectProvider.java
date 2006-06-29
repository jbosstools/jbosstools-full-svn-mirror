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
package org.jboss.ide.eclipse.firstrun.providers;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public abstract class AbstractProjectProvider implements IStructuredContentProvider
{

   public void dispose()
   {
   }

   public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
   {
   }

   public abstract boolean checkProject(IProject project);

   public Object[] getElements(Object inputElement)
   {
      if (inputElement instanceof IWorkspace)
      {
         IWorkspace workspace = (IWorkspace) inputElement;

         IProject projects[] = workspace.getRoot().getProjects();
         ArrayList elements = new ArrayList();

         for (int i = 0; i < projects.length; i++)
         {
            if (projects[i] != null && projects[i].isAccessible())
            {
               if (checkProject(projects[i]))
                  elements.add(projects[i]);
            }
         }

         return elements.toArray();
      }

      return new Object[0];
   }

}
