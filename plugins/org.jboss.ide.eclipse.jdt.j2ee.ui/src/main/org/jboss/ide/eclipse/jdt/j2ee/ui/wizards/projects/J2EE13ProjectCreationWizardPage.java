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
package org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.projects;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.jboss.ide.eclipse.jdt.core.classpath.ClassPathContainerRepository;
import org.jboss.ide.eclipse.jdt.core.util.JavaProjectUtil;
import org.jboss.ide.eclipse.jdt.j2ee.core.JDTJ2EECorePlugin;
import org.jboss.ide.eclipse.jdt.j2ee.core.classpath.J2EE13ClasspathContainer;
import org.jboss.ide.eclipse.jdt.j2ee.ui.JDTJ2EEUIMessages;
import org.jboss.ide.eclipse.jdt.ui.wizards.ProjectWizardPage;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class J2EE13ProjectCreationWizardPage extends ProjectWizardPage
{
   /**
    *Constructor for the J2EE13ProjectCreationWizardPage object
    *
    * @param mainPage  Description of the Parameter
    */
   public J2EE13ProjectCreationWizardPage(WizardNewProjectCreationPage mainPage)
   {
      super(mainPage);
      this.setTitle(JDTJ2EEUIMessages.getString("J2EE13ProjectCreationWizardPage.title"));//$NON-NLS-1$
      this.setDescription(JDTJ2EEUIMessages.getString("J2EE13ProjectCreationWizardPage.description"));//$NON-NLS-1$
   }

   /**
    * Add necessary source and libraries
    *
    * @param entries
    * @return
    * @throws CoreException
    */
   protected IClasspathEntry[] checkEntries(IClasspathEntry[] entries) throws CoreException
   {
      // entries = JavaProjectUtil.mergeClasspathEntry(entries, JavaProjectUtil.createSourceClasspathEntry(fCurrProject, "src"));
      entries = JavaProjectUtil.mergeClasspathEntry(entries, JavaRuntime.getDefaultJREContainerEntry());
      entries = JavaProjectUtil.mergeClasspathEntry(entries, ClassPathContainerRepository.getInstance().getEntry(
            J2EE13ClasspathContainer.CLASSPATH_CONTAINER));
      return entries;
   }

   protected String[] getBuilders()
   {
      return new String[]
      {JDTJ2EECorePlugin.WST_VALIDATION_BUILDER_ID};
   }
}
