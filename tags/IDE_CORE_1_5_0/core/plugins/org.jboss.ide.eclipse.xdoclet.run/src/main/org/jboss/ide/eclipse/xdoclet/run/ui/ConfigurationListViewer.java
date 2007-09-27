/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.run.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.debug.internal.ui.DefaultLabelProvider;
import org.eclipse.jdt.internal.ui.viewsupport.ListContentProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.jboss.ide.eclipse.xdoclet.run.XDocletRunMessages;
import org.jboss.ide.eclipse.xdoclet.run.XDocletRunPlugin;
import org.jboss.ide.eclipse.xdoclet.run.model.XDocletConfiguration;
import org.jboss.ide.eclipse.xdoclet.run.model.XDocletData;
import org.jboss.ide.eclipse.xdoclet.run.ui.dialogs.ConfigurationChoiceDialog;
import org.jboss.ide.eclipse.xdoclet.run.ui.dialogs.ConfigurationEditDialog;
import org.jboss.ide.eclipse.xdoclet.ui.IXDocletUIConstants;
import org.jboss.ide.eclipse.xdoclet.ui.XDocletUIImages;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 * @todo      Javadoc to complete
 */
public class ConfigurationListViewer
{
   /** Description of the Field */
   private Action addAction;
   /** Description of the Field */
   private Action addStandardAction;
   /** Description of the Field */
   private List configurations;
   /** Description of the Field */
   private Menu contextMenu;
   /** Description of the Field */
   private Composite parentComposite;
   /** Description of the Field */
   private Action removeAction;
   /** Description of the Field */
   private Action renameAction;
   /** Description of the Field */
   private CheckboxTableViewer viewer;


   /**
    *Constructor for the ConfigurationsViewer object
    *
    * @param parent          Description of the Parameter
    * @param configurations  Description of the Parameter
    */
   public ConfigurationListViewer(Composite parent, List configurations)
   {
      this.parentComposite = parent;
      this.configurations = configurations;

      Table table = new Table(parent, SWT.CHECK | SWT.BORDER | SWT.FULL_SELECTION);
      table.setLayoutData(new GridData(GridData.FILL_BOTH));

      TableLayout tableLayout = new TableLayout();
      tableLayout.addColumnData(new ColumnWeightData(100));
      table.setLayout(tableLayout);

      this.viewer = new CheckboxTableViewer(table);
      this.viewer.setContentProvider(new ListContentProvider());
      this.viewer.setLabelProvider(
         new DefaultLabelProvider()
         {
            public Image getImage(Object element)
            {
               return XDocletUIImages.getImage(IXDocletUIConstants.IMG_OBJS_XDOCLET_CONF);
            }


            public String getText(Object element)
            {
               return element.toString();
            }
         });

      this.setInput(configurations);

      this.viewer.addCheckStateListener(
         new ICheckStateListener()
         {
            public void checkStateChanged(CheckStateChangedEvent event)
            {
               XDocletConfiguration configuration = (XDocletConfiguration) event.getElement();
               configuration.setUsed(event.getChecked());
            }
         });

      this.assign();
      this.initContextMenu();
   }


   /**
    * Adds a feature to the SelectionChangeListener attribute of the ConfigurationListViewer object
    *
    * @param listener  The feature to be added to the SelectionChangeListener attribute
    */
   public void addSelectionChangeListener(ISelectionChangedListener listener)
   {
      this.viewer.addSelectionChangedListener(listener);
   }


   /**
    * Gets the current attribute of the ConfigurationListViewer object
    *
    * @return   The current value
    */
   public XDocletConfiguration getCurrent()
   {
      IStructuredSelection selection = (IStructuredSelection) this.viewer.getSelection();
      if (!selection.isEmpty())
      {
         XDocletConfiguration configuration = (XDocletConfiguration) selection.getFirstElement();
         return configuration;
      }
      return null;
   }


   /** Description of the Method */
   public void refresh()
   {
      this.viewer.refresh();
   }


   /**
    * Description of the Method
    *
    * @param listener  Description of the Parameter
    */
   public void removeSelectionChangeListener(ISelectionChangedListener listener)
   {
      this.viewer.removeSelectionChangedListener(listener);
   }


   /** Description of the Method */
   private void assign()
   {
      this.addAction =
         new Action()
         {
            public void run()
            {
               doAdd();
            }

         };
      this.addAction.setText(XDocletRunMessages.getString("ConfigurationListViewer.action.add"));//$NON-NLS-1$
      this.addAction.setToolTipText(XDocletRunMessages.getString("ConfigurationListViewer.action.add.tip"));//$NON-NLS-1$
      this.addAction.setEnabled(true);

      this.addStandardAction =
         new Action()
         {
            public void run()
            {
               doAddStandard();
            }

         };
      this.addStandardAction.setText(XDocletRunMessages.getString("ConfigurationListViewer.action.add.standard"));//$NON-NLS-1$
      this.addStandardAction.setToolTipText(XDocletRunMessages.getString("ConfigurationListViewer.action.add.standard.tip"));//$NON-NLS-1$
      this.addStandardAction.setEnabled(true);

      this.renameAction =
         new Action()
         {
            public void run()
            {
               doRename();
            }

         };
      this.renameAction.setText(XDocletRunMessages.getString("ConfigurationListViewer.action.rename"));//$NON-NLS-1$
      this.renameAction.setToolTipText(XDocletRunMessages.getString("ConfigurationListViewer.action.rename.tip"));//$NON-NLS-1$
      this.renameAction.setEnabled(true);

      this.removeAction =
         new Action()
         {
            public void run()
            {
               doRemove();
            }

         };
      this.removeAction.setText(XDocletRunMessages.getString("ConfigurationListViewer.action.remove"));//$NON-NLS-1$
      this.removeAction.setToolTipText(XDocletRunMessages.getString("ConfigurationListViewer.action.remove.tip"));//$NON-NLS-1$
      this.removeAction.setEnabled(true);

      this.viewer.addDoubleClickListener(
         new IDoubleClickListener()
         {
            public void doubleClick(DoubleClickEvent event)
            {
               doRename();
            }
         });
   }


   /** Description of the Method */
   public void doAdd()
   {
      ConfigurationEditDialog dialog = new ConfigurationEditDialog(this.parentComposite.getShell());
      if (dialog.open() == Window.OK)
      {
         XDocletConfiguration configuration = dialog.getXDocletConfiguration();
         if (configuration != null)
         {
            this.configurations.add(configuration);
            this.viewer.refresh();
            this.viewer.setChecked(configuration, configuration.isUsed());
         }
      }
   }


   /** Description of the Method */
   public void doAddStandard()
   {
      ConfigurationChoiceDialog dialog = new ConfigurationChoiceDialog(this.parentComposite.getShell(), XDocletRunPlugin.getDefault().getStandardConfigurations().getConfigurations());
      if (dialog.open() == Window.OK)
      {
         XDocletData data = dialog.getXDocletData();
         if (data != null)
         {
            XDocletConfiguration configuration = (XDocletConfiguration) data.cloneData();
            this.configurations.add(configuration);
            this.viewer.refresh();
            this.viewer.setChecked(configuration, configuration.isUsed());
         }
      }
   }


   /** Description of the Method */
   public void doRemove()
   {
      IStructuredSelection selection = (IStructuredSelection) this.viewer.getSelection();
      if (!selection.isEmpty())
      {
         XDocletConfiguration configuration = (XDocletConfiguration) selection.getFirstElement();
         this.configurations.remove(configuration);
         this.viewer.refresh();
      }
   }


   /** Description of the Method */
   public void doRename()
   {
      IStructuredSelection selection = (IStructuredSelection) this.viewer.getSelection();
      if (!selection.isEmpty())
      {
         XDocletConfiguration configuration = (XDocletConfiguration) selection.getFirstElement();
         ConfigurationEditDialog dialog = new ConfigurationEditDialog(this.parentComposite.getShell(), configuration);
         if (dialog.open() == Window.OK)
         {
            this.viewer.refresh();
            this.viewer.setChecked(configuration, configuration.isUsed());
         }
      }
   }


   /** Description of the Method */
   private void enableActions()
   {
      if (this.configurations.isEmpty())
      {
         this.addAction.setEnabled(true);
         this.addStandardAction.setEnabled(true);
         this.renameAction.setEnabled(false);
         this.removeAction.setEnabled(false);
      }
      else
      {
         this.addAction.setEnabled(true);
         this.addStandardAction.setEnabled(true);
         this.renameAction.setEnabled(true);
         this.removeAction.setEnabled(true);
      }
   }



   /**
    * Description of the Method
    *
    * @param manager  Description of the Parameter
    */
   private void fillContextMenu(IMenuManager manager)
   {
      manager.add(this.addAction);
      manager.add(this.addStandardAction);
      manager.add(this.renameAction);
      manager.add(this.removeAction);
      manager.add(new Separator("Additions"));//$NON-NLS-1$
   }


   /**
    * @return   The usedConfiguration value
    */
   private Collection getUsedConfiguration()
   {
      Iterator iterator = this.configurations.iterator();
      ArrayList result = new ArrayList();
      while (iterator.hasNext())
      {
         XDocletConfiguration attribute = (XDocletConfiguration) iterator.next();
         if (attribute.isUsed())
         {
            result.add(attribute);
         }
      }
      return result;
   }


   /** Description of the Method */
   private void initContextMenu()
   {
      MenuManager menuManager = new MenuManager("#PopupMenu");//$NON-NLS-1$
      menuManager.setRemoveAllWhenShown(true);
      menuManager.addMenuListener(
         new IMenuListener()
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
    * Sets the input attribute of the ConfigurationListViewer object
    *
    * @param input  The new input value
    */
   private void setInput(Object input)
   {
      this.viewer.setInput(input);
      this.viewer.setCheckedElements(this.getUsedConfiguration().toArray());
   }
}
