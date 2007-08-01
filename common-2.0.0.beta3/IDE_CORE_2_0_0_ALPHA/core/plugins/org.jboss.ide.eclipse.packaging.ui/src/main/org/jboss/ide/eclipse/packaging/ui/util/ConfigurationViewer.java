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
package org.jboss.ide.eclipse.packaging.ui.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.packaging.core.PackagingCorePlugin;
import org.jboss.ide.eclipse.packaging.core.model.PackagingArchive;
import org.jboss.ide.eclipse.packaging.core.model.PackagingData;
import org.jboss.ide.eclipse.packaging.core.model.PackagingFile;
import org.jboss.ide.eclipse.packaging.core.model.PackagingFolder;
import org.jboss.ide.eclipse.packaging.ui.PackagingUIMessages;
import org.jboss.ide.eclipse.packaging.ui.dialogs.ArchiveEditDialog;
import org.jboss.ide.eclipse.packaging.ui.dialogs.DataChoiceDialog;
import org.jboss.ide.eclipse.packaging.ui.dialogs.FileEditDialog;
import org.jboss.ide.eclipse.packaging.ui.dialogs.FolderEditDialog;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class ConfigurationViewer implements IPropertyChangeListener
{
   private Action addArchiveAction;

   private Action addFileAction;

   private Action addFolderAction;

   private Action addStandardArchiveAction;

   private List configurations;

   private Menu contextMenu;

   private Action editAction;

   private Composite parentComposite;

   private IProject project;

   private Action removeAction;

   private CheckboxTreeViewer viewer;

   /**
    *Constructor for the ConfigurationViewer object
    *
    * @param parent          Description of the Parameter
    * @param configurations  Description of the Parameter
    * @param project         Description of the Parameter
    */
   public ConfigurationViewer(Composite parent, IProject project, List configurations)
   {
      this.parentComposite = parent;
      this.project = project;
      this.configurations = configurations;

      Tree tree = new Tree(parent, SWT.BORDER | SWT.CHECK | SWT.SINGLE);
      GridData data = new GridData(GridData.FILL_BOTH);
      tree.setLayoutData(data);
      this.viewer = new CheckboxTreeViewer(tree);
      this.viewer.setContentProvider(new ConfigurationContentProvider());
      this.viewer.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);
      this.viewer.setLabelProvider(new ConfigurationLabelProvider());

      this.setInput(configurations);

      this.viewer.addCheckStateListener(new ICheckStateListener()
      {
         public void checkStateChanged(CheckStateChangedEvent event)
         {
            PackagingData data = (PackagingData) event.getElement();
            data.setUsed(event.getChecked());
         }
      });

      this.assign();
      this.initContextMenu();
   }

   /**
    * Adds a feature to the DoubleClickListener attribute of the ConfigurationViewer object
    *
    * @param listener  The feature to be added to the DoubleClickListener attribute
    */
   public void addDoubleClickListener(IDoubleClickListener listener)
   {
      this.viewer.addDoubleClickListener(listener);
   }

   /**
    * Adds a feature to the SelectionChangedListener attribute of the ConfigurationViewer object
    *
    * @param listener  The feature to be added to the SelectionChangedListener attribute
    */
   public void addSelectionChangedListener(ISelectionChangedListener listener)
   {
      this.viewer.addSelectionChangedListener(listener);
   }

   /**
    * Gets the current attribute of the ConfigurationListViewer object
    *
    * @return   The current value
    */
   public PackagingData getCurrent()
   {
      IStructuredSelection selection = (IStructuredSelection) this.viewer.getSelection();
      if (!selection.isEmpty())
      {
         PackagingData element = (PackagingData) selection.getFirstElement();
         return element;
      }
      return null;
   }

   /**
    * Description of the Method
    *
    * @param event  Description of the Parameter
    */
   public void propertyChange(PropertyChangeEvent event)
   {
      this.refresh();
   }

   /** Description of the Method */
   public void refresh()
   {
      this.viewer.refresh();
      this.viewer.setCheckedElements(this.getUsedElements().toArray());
   }

   /**
    * Description of the Method
    *
    * @param listener  Description of the Parameter
    */
   public void removeSelectionChangedListener(ISelectionChangedListener listener)
   {
      this.viewer.removeSelectionChangedListener(listener);
   }

   /** Description of the Method */
   private void assign()
   {
      this.addArchiveAction = new Action()
      {
         public void run()
         {
            doAddArchive();
         }

      };
      this.addArchiveAction.setText(PackagingUIMessages.getString("ConfigurationViewer.action.add.archive"));//$NON-NLS-1$
      this.addArchiveAction.setToolTipText(PackagingUIMessages.getString("ConfigurationViewer.action.add.archive.tip"));//$NON-NLS-1$
      this.addArchiveAction.setEnabled(true);

      this.addStandardArchiveAction = new Action()
      {
         public void run()
         {
            doAddStandardArchive();
         }

      };
      this.addStandardArchiveAction.setText(PackagingUIMessages
            .getString("ConfigurationViewer.action.add.standard.archive"));//$NON-NLS-1$
      this.addStandardArchiveAction.setToolTipText(PackagingUIMessages
            .getString("ConfigurationViewer.action.add.standard.archive.tip"));//$NON-NLS-1$
      this.addStandardArchiveAction.setEnabled(true);

      this.addFileAction = new Action()
      {
         public void run()
         {
            doAddFile();
         }

      };
      this.addFileAction.setText(PackagingUIMessages.getString("ConfigurationViewer.action.add.file"));//$NON-NLS-1$
      this.addFileAction.setToolTipText(PackagingUIMessages.getString("ConfigurationViewer.action.add.file.tip"));//$NON-NLS-1$
      this.addFileAction.setEnabled(true);

      this.addFolderAction = new Action()
      {
         public void run()
         {
            doAddFolder();
         }

      };
      this.addFolderAction.setText(PackagingUIMessages.getString("ConfigurationViewer.action.add.folder"));//$NON-NLS-1$
      this.addFolderAction.setToolTipText(PackagingUIMessages.getString("ConfigurationViewer.action.add.folder.tip"));//$NON-NLS-1$
      this.addFolderAction.setEnabled(true);

      this.editAction = new Action()
      {
         public void run()
         {
            doEdit();
         }

      };
      this.editAction.setText(PackagingUIMessages.getString("ConfigurationViewer.action.edit"));//$NON-NLS-1$
      this.editAction.setToolTipText(PackagingUIMessages.getString("ConfigurationViewer.action.edit.tip"));//$NON-NLS-1$
      this.editAction.setEnabled(true);

      this.removeAction = new Action()
      {
         public void run()
         {
            doRemove();
         }

      };
      this.removeAction.setText(PackagingUIMessages.getString("ConfigurationViewer.action.remove"));//$NON-NLS-1$
      this.removeAction.setToolTipText(PackagingUIMessages.getString("ConfigurationViewer.action.remove.tip"));//$NON-NLS-1$
      this.removeAction.setEnabled(true);

      this.viewer.addDoubleClickListener(new IDoubleClickListener()
      {
         public void doubleClick(DoubleClickEvent event)
         {
            doEdit();
         }
      });
   }

   /** Description of the Method */
   public void doAddArchive()
   {
      PackagingArchive archive = new PackagingArchive();
      archive.setName("Untitled.jar");//$NON-NLS-1$
      ArchiveEditDialog dialog = new ArchiveEditDialog(AbstractPlugin.getShell(), this.project.getProject(), archive);
      if (dialog.open() == IDialogConstants.OK_ID)
      {
         archive.setProject(this.project.getProject());
         this.configurations.add(archive);
         this.refresh();
      }
   }

   /** Description of the Method */
   public void doAddFile()
   {
      PackagingFile file = new PackagingFile();
      PackagingArchive archive = (PackagingArchive) this.getCurrent();
      FileEditDialog dialog = new FileEditDialog(AbstractPlugin.getShell(), archive, file);
      if (dialog.open() == IDialogConstants.OK_ID)
      {
         this.getCurrent().addNode(file);
         this.refresh();
      }
      
      this.viewer.setExpandedState(archive, true);
   }

   /** Description of the Method */
   public void doAddFolder()
   {
      PackagingFolder folder = new PackagingFolder();
      PackagingArchive archive = (PackagingArchive) this.getCurrent();
      FolderEditDialog dialog = new FolderEditDialog(AbstractPlugin.getShell(), archive, folder);
      if (dialog.open() == IDialogConstants.OK_ID)
      {
         this.getCurrent().addNode(folder);
         this.refresh();
      }
      this.viewer.setExpandedState(archive, true);
   }

   /** Description of the Method */
   public void doAddStandardArchive()
   {
      Collection choices = PackagingCorePlugin.getDefault().getStandardConfigurations().getConfigurations();
      DataChoiceDialog dialog = new DataChoiceDialog(AbstractPlugin.getShell(), choices);
      if (dialog.open() == IDialogConstants.OK_ID)
      {
         PackagingData data = dialog.getPackagingArchive();
         if (data != null)
         {
            PackagingArchive archive = (PackagingArchive) data.cloneData();
            archive.setProject(this.project);
            this.configurations.add(archive);
            this.refresh();
         }
      }
   }

   /** Description of the Method */
   public void doEdit()
   {
      PackagingData data = this.getCurrent();
      if (data != null)
      {
         if (data instanceof PackagingArchive)
         {
            PackagingArchive archive = (PackagingArchive) data;
            ArchiveEditDialog dialog = new ArchiveEditDialog(AbstractPlugin.getShell(), this.project.getProject(),
                  archive);
            if (dialog.open() == IDialogConstants.OK_ID)
            {
               this.refresh();
            }
            return;
         }
         if (data instanceof PackagingFolder)
         {
            PackagingFolder folder = (PackagingFolder) data;
            PackagingArchive archive = (PackagingArchive) folder.getParent();
            FolderEditDialog dialog = new FolderEditDialog(AbstractPlugin.getShell(), archive, folder);
            if (dialog.open() == IDialogConstants.OK_ID)
            {
               this.refresh();
            }
            return;
         }
         if (data instanceof PackagingFile)
         {
            PackagingFile file = (PackagingFile) data;
            PackagingArchive archive = (PackagingArchive) file.getParent();
            FileEditDialog dialog = new FileEditDialog(AbstractPlugin.getShell(), archive, file);
            if (dialog.open() == IDialogConstants.OK_ID)
            {
               this.refresh();
            }
            return;
         }
      }
   }

   /** Description of the Method */
   public void doRemove()
   {
      PackagingData data = this.getCurrent();
      if (data != null)
      {
         if (data instanceof PackagingArchive)
         {
            this.configurations.remove(data);
         }
         else
         {
            data.getParent().removeNode(data);
         }
         this.refresh();
      }
   }

   /** Description of the Method */
   private void enableActions()
   {
      PackagingData data = this.getCurrent();
      if (data != null)
      {
         this.editAction.setEnabled(true);
         this.removeAction.setEnabled(true);
         if (data instanceof PackagingArchive)
         {
            this.addFileAction.setEnabled(true);
            this.addFolderAction.setEnabled(true);
         }
         else
         {
            this.addFileAction.setEnabled(false);
            this.addFolderAction.setEnabled(false);
         }
      }
      else
      {
         this.addFileAction.setEnabled(false);
         this.addFolderAction.setEnabled(false);
         this.editAction.setEnabled(false);
         this.removeAction.setEnabled(false);
      }
   }

   /**
    * Description of the Method
    *
    * @param manager  Description of the Parameter
    */
   private void fillContextMenu(IMenuManager manager)
   {
      manager.add(this.addFileAction);
      manager.add(this.addFolderAction);
      manager.add(this.editAction);
      manager.add(this.removeAction);
      manager.add(new Separator("Additions"));//$NON-NLS-1$
   }

   /**
    * Gets the usedElements attribute of the ConfigurationViewer object
    *
    * @param element  Description of the Parameter
    * @return         The usedElements value
    */
   private Collection getUsedElements(PackagingData element)
   {
      Collection used = new ArrayList();
      if (element != null)
      {
         Iterator iterator = element.getNodes().iterator();
         while (iterator.hasNext())
         {
            PackagingData data = (PackagingData) iterator.next();
            if (data.isUsed())
            {
               used.add(data);
            }
            if (!data.isEmpty())
            {
               used.addAll(this.getUsedElements(data));
            }
         }
      }
      return used;
   }

   /**
    * Gets the usedElements attribute of the ConfigurationViewer object
    *
    * @return   The usedElements value
    */
   private Collection getUsedElements()
   {
      Collection used = new ArrayList();
      Iterator iterator = this.configurations.iterator();
      while (iterator.hasNext())
      {
         PackagingData data = (PackagingData) iterator.next();
         if (data.isUsed())
         {
            used.add(data);
         }
         used.addAll(this.getUsedElements(data));
      }
      return used;
   }

   /** Description of the Method */
   private void initContextMenu()
   {
      MenuManager menuManager = new MenuManager("#PopupMenu");//$NON-NLS-1$
      menuManager.setRemoveAllWhenShown(true);
      menuManager.addMenuListener(new IMenuListener()
      {
         public void menuAboutToShow(IMenuManager manager)
         {
            enableActions();
            fillContextMenu(manager);
         }

      });
      this.contextMenu = menuManager.createContextMenu(this.viewer.getControl());
      this.viewer.getControl().setMenu(contextMenu);
   }

   /**
    * Sets the input attribute of the ConfigurationViewer object
    *
    * @param input  The new input value
    */
   private void setInput(Object input)
   {
      this.viewer.setInput(input);
      this.viewer.setCheckedElements(this.getUsedElements().toArray());
   }
}
