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
package org.jboss.ide.eclipse.firstrun.wizard;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.wizard.Wizard;
import org.jboss.ide.eclipse.firstrun.wizard.pages.FirstRunFinalPage;
import org.jboss.ide.eclipse.firstrun.wizard.pages.FirstRunInfoPage;
import org.jboss.ide.eclipse.firstrun.wizard.pages.FirstRunPackagingProjectsPage;
import org.jboss.ide.eclipse.firstrun.wizard.pages.FirstRunXDocletProjectsPage;
import org.jboss.ide.eclipse.packaging.core.PackagingCorePlugin;
import org.jboss.ide.eclipse.xdoclet.run.XDocletRunPlugin;

public class FirstRunWizard extends Wizard
{

   private FirstRunInfoPage page1;

   private FirstRunPackagingProjectsPage page2;

   private FirstRunXDocletProjectsPage page3;

   private FirstRunFinalPage page4;

   public boolean performFinish()
   {

      IProject packagingProjectsToConvert[] = page2.getSelectedProjects();
      IProject xdocletProjectsToConvert[] = page3.getSelectedProjects();

      for (int i = 0; i < packagingProjectsToConvert.length; i++)
      {
         PackagingCorePlugin.getDefault().enablePackagingBuilder(JavaCore.create(packagingProjectsToConvert[i]), true);
      }

      for (int i = 0; i < xdocletProjectsToConvert.length; i++)
      {
         XDocletRunPlugin.getDefault().enableXDocletBuilder(JavaCore.create(xdocletProjectsToConvert[i]), true);
      }

      return true;
   }

   public boolean canFinish()
   {
      if (page4 == null)
         return false;

      return page4.isPageComplete();
   }

   public void addPages()
   {
      page1 = new FirstRunInfoPage();
      page2 = new FirstRunPackagingProjectsPage();
      page3 = new FirstRunXDocletProjectsPage();
      page4 = new FirstRunFinalPage();

      addPage(page1);
      addPage(page2);
      addPage(page3);
      addPage(page4);
   }

}
