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

import org.eclipse.debug.internal.ui.DefaultLabelProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.jboss.ide.eclipse.ui.util.StringViewSorter;
import org.jboss.ide.eclipse.xdoclet.run.XDocletRunMessages;
import org.jboss.ide.eclipse.xdoclet.run.XDocletRunPlugin;
import org.jboss.ide.eclipse.xdoclet.run.model.XDocletConfiguration;
import org.jboss.ide.eclipse.xdoclet.run.model.XDocletData;
import org.jboss.ide.eclipse.xdoclet.run.model.XDocletElement;
import org.jboss.ide.eclipse.xdoclet.run.model.XDocletTask;
import org.jboss.ide.eclipse.xdoclet.run.ui.dialogs.DataChoiceDialog;
import org.jboss.ide.eclipse.xdoclet.run.util.ConfigurationContentProvider;
import org.jboss.ide.eclipse.xdoclet.ui.IXDocletUIConstants;
import org.jboss.ide.eclipse.xdoclet.ui.XDocletUIImages;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 * @todo      Javadoc to complete
 */
public class ConfigurationViewer
{
   /** Description of the Field */
   private Action addDocletAction;
   /** Description of the Field */
   private Action addElementAction;
   /** Description of the Field */
   private XDocletConfiguration configuration;
   /** Description of the Field */
   private Menu contextMenu;
   /** Description of the Field */
   private Composite parentComposite;
   /** Description of the Field */
   private Action removeElementAction;
   /** Description of the Field */
   private CheckboxTreeViewer viewer;


   /**
    *Constructor for the ConfigurationViewer object
    *
    * @param parent  Description of the Parameter
    */
   public ConfigurationViewer(Composite parent)
   {
      this.parentComposite = parent;

      Tree tree = new Tree(parent, SWT.BORDER | SWT.CHECK | SWT.SINGLE);
      tree.setLayoutData(new GridData(GridData.FILL_BOTH));
      this.viewer = new CheckboxTreeViewer(tree);
      this.viewer.setContentProvider(new ConfigurationContentProvider());
      this.viewer.setSorter(new StringViewSorter());
      this.viewer.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);
      this.viewer.setLabelProvider(
         new DefaultLabelProvider()
         {
            public Image getImage(Object element)
            {
               if (element instanceof XDocletTask)
               {
                  return XDocletUIImages.getImage(IXDocletUIConstants.IMG_OBJS_XDOCLET_TASK);
               }
               return XDocletUIImages.getImage(IXDocletUIConstants.IMG_OBJS_XDOCLET_SUBTASK);
            }


            public String getText(Object element)
            {
               return element.toString();
            }
         });

      this.viewer.addCheckStateListener(
         new ICheckStateListener()
         {
            public void checkStateChanged(CheckStateChangedEvent event)
            {
               XDocletElement element = (XDocletElement) event.getElement();
               element.setUsed(event.getChecked());
            }
         });
      this.prepareActions();
      this.initContextMenu();
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
   public XDocletElement getCurrent()
   {
      IStructuredSelection selection = (IStructuredSelection) this.viewer.getSelection();
      if (!selection.isEmpty())
      {
         XDocletElement element = (XDocletElement) selection.getFirstElement();
         return element;
      }
      return null;
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


   /**
    * Sets the configuration attribute of the ConfigurationViewer object
    *
    * @param configuration  The new configuration value
    */
   public void setConfiguration(XDocletConfiguration configuration)
   {
      this.configuration = configuration;
      this.viewer.setInput(this.configuration);
      this.refresh();
   }


   /** Description of the Method */
   private void doAdd()
   {
      XDocletElement element = this.getCurrent();
      if (element != null)
      {
         Collection choices = XDocletRunPlugin.getDefault().getXDocletDataRepository().getElements(element);
         if ((choices != null) && (!choices.isEmpty()))
         {
            DataChoiceDialog dialog = new DataChoiceDialog(this.parentComposite.getShell(), choices);
            if (dialog.open() == Window.OK)
            {
               XDocletData data = dialog.getXDocletData();
               if (data != null)
               {
                  element.addNode((XDocletData) data.clone());
                  this.refresh();
               }
            }
         }
      }
   }


   /** Description of the Method */
   private void doAddDoclet()
   {
      if (this.configuration != null)
      {
         DataChoiceDialog dialog = new DataChoiceDialog(this.parentComposite.getShell(), XDocletRunPlugin.getDefault().getXDocletDataRepository().getTasks());
         if (dialog.open() == Window.OK)
         {
            XDocletData data = dialog.getXDocletData();
            if (data != null)
            {
               this.configuration.addNode((XDocletData) data.clone());
               this.refresh();
            }
         }
      }
      else
      {
         XDocletRunPlugin.getDefault().showInfoMessage(XDocletRunMessages.getString("ConfigurationViewer.add.doclet.failed"));//$NON-NLS-1$
      }
   }


   /** Description of the Method */
   private void doRemove()
   {
      XDocletElement element = this.getCurrent();
      if (element != null)
      {
         element.getParent().removeNode(element);
         this.refresh();
      }
   }


   /** Description of the Method */
   private void enableAction()
   {
      XDocletElement element = this.getCurrent();
      if (element != null)
      {
         this.addDocletAction.setEnabled(true);
         this.addElementAction.setEnabled(true);
         this.removeElementAction.setEnabled(true);
      }
      else
      {
         this.addDocletAction.setEnabled(true);
         this.addElementAction.setEnabled(false);
         this.removeElementAction.setEnabled(false);
      }
   }


   /**
    * Description of the Method
    *
    * @param manager  Description of the Parameter
    */
   private void fillContextMenu(IMenuManager manager)
   {
      manager.add(this.addElementAction);
      manager.add(this.removeElementAction);
      manager.add(new Separator("Doclet"));//$NON-NLS-1$
      manager.add(this.addDocletAction);
      manager.add(new Separator("Additions"));//$NON-NLS-1$
   }


   /**
    * Gets the usedElements attribute of the ConfigurationViewer object
    *
    * @param element  Description of the Parameter
    * @return         The usedElements value
    */
   private Collection getUsedElements(XDocletData element)
   {
      Collection used = new ArrayList();
      if (element != null)
      {
         Iterator iterator = element.getNodes().iterator();

         while (iterator.hasNext())
         {
            XDocletData data = (XDocletData) iterator.next();
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
               enableAction();
               fillContextMenu(manager);
            }

         });
      this.contextMenu = menuManager.createContextMenu(this.viewer.getControl());
      this.viewer.getControl().setMenu(contextMenu);
   }


   /** Description of the Method */
   private void prepareActions()
   {
      this.addDocletAction =
         new Action()
         {
            public void run()
            {
               doAddDoclet();
            }

         };
      this.addDocletAction.setText(XDocletRunMessages.getString("ConfigurationViewer.action.add.doclet"));//$NON-NLS-1$
      this.addDocletAction.setToolTipText(XDocletRunMessages.getString("ConfigurationViewer.action.add.doclet.tip"));//$NON-NLS-1$
      this.addDocletAction.setEnabled(true);

      this.addElementAction =
         new Action()
         {
            public void run()
            {
               doAdd();
            }

         };
      this.addElementAction.setText(XDocletRunMessages.getString("ConfigurationViewer.action.add"));//$NON-NLS-1$
      this.addElementAction.setToolTipText(XDocletRunMessages.getString("ConfigurationViewer.action.add.tip"));//$NON-NLS-1$
      this.addElementAction.setEnabled(true);

      this.removeElementAction =
         new Action()
         {
            public void run()
            {
               doRemove();
            }

         };
      this.removeElementAction.setText(XDocletRunMessages.getString("ConfigurationViewer.action.remove"));//$NON-NLS-1$
      this.removeElementAction.setToolTipText(XDocletRunMessages.getString("ConfigurationViewer.action.remove.tip"));//$NON-NLS-1$
      this.removeElementAction.setEnabled(true);
   }


   /** Description of the Method */
   private void refresh()
   {
      this.viewer.refresh();
      this.viewer.setCheckedElements(this.getUsedElements(this.configuration).toArray());
   }
}
