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
package org.jboss.ide.eclipse.firstrun.wizard.pages;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jboss.ide.eclipse.core.util.ProjectUtil;
import org.jboss.ide.eclipse.firstrun.FirstRunMessages;
import org.jboss.ide.eclipse.firstrun.FirstRunPlugin;
import org.jboss.ide.eclipse.firstrun.providers.AbstractProjectProvider;
import org.jboss.ide.eclipse.firstrun.providers.ProjectLabelProvider;
import org.jboss.ide.eclipse.packaging.core.PackagingCorePlugin;
import org.jboss.ide.eclipse.packaging.core.builder.PackagingBuilder;
import org.jboss.ide.eclipse.xdoclet.run.XDocletRunPlugin;

public class FirstRunPackagingProjectsPage extends AbstractFirstRunPage {

   private CheckboxTableViewer projectTable;

   public FirstRunPackagingProjectsPage()
   {
      super(FirstRunMessages.getString("PackagingProjectsPage.title"), FirstRunMessages
            .getString("FirstRunWizard.title"), FirstRunPlugin.getImageDescriptor(FirstRunPlugin.ICON_JBOSSIDE_LOGO));
   }

   public void createControl(Composite parent)
   {
      setTitle(FirstRunMessages.getString("PackagingProjectsPage.title"));

      Composite main = new Composite(parent, SWT.NONE);
      main.setLayout(new GridLayout(1, false));

      Label label = new Label(main, SWT.WRAP);
      label.setText(FirstRunMessages.getString("PackagingProjectsPage.description"));

      projectTable = CheckboxTableViewer.newCheckList(main, SWT.BORDER);
      projectTable.setContentProvider(new AbstractProjectProvider()
      {
         public boolean checkProject(IProject project)
         {
            return !ProjectUtil.projectHasBuilder(project, PackagingBuilder.BUILDER_ID)
                  && project.getFile(new Path(PackagingCorePlugin.BUILD_FILE)).exists();
         }
      });

      projectTable.setLabelProvider(new ProjectLabelProvider());

      projectTable.setInput(ResourcesPlugin.getWorkspace());
      projectTable.setAllChecked(true);

      projectTable.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

      Composite buttonComposite = new Composite(main, SWT.NONE);
      buttonComposite.setLayout(new RowLayout());

      Button selectAllButton = new Button(buttonComposite, SWT.NONE);
      selectAllButton.setText("Select All");
      selectAllButton.addSelectionListener(new SelectionListener()
      {
         public void widgetDefaultSelected(SelectionEvent e)
         {
            widgetSelected(e);
         }

         public void widgetSelected(SelectionEvent e)
         {
            projectTable.setAllChecked(true);
         }
      });

      Button unselectAllButton = new Button(buttonComposite, SWT.NONE);
      unselectAllButton.setText("Unselect All");
      unselectAllButton.addSelectionListener(new SelectionListener()
      {
         public void widgetDefaultSelected(SelectionEvent e)
         {
            widgetSelected(e);
         }

         public void widgetSelected(SelectionEvent e)
         {
            projectTable.setAllChecked(false);
         }
      });

      setControl(main);
   }

   public IProject[] getSelectedProjects()
   {
      Object elements[] = projectTable.getCheckedElements();
      IProject projects[] = new IProject[elements.length];

      System.arraycopy(elements, 0, projects, 0, elements.length);
      return projects;
   }

   public void initialize() {
       // force initialization
       PackagingCorePlugin.getDefault();
   }

	public void performFinish() {
		IProject packagingProjectsToConvert[] = getSelectedProjects();
		for (int i = 0; i < packagingProjectsToConvert.length; i++) {
			PackagingCorePlugin.getDefault().enablePackagingBuilder(JavaCore.create(packagingProjectsToConvert[i]), true);
		}
	}
}
