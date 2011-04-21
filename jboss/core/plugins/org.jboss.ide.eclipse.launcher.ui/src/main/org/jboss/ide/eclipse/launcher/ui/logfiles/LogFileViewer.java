/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.launcher.ui.logfiles;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.part.WorkbenchPart;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.launcher.core.LauncherPlugin;
import org.jboss.ide.eclipse.launcher.core.logfiles.LogFile;
import org.jboss.ide.eclipse.launcher.core.logfiles.LogFileDocument;
import org.jboss.ide.eclipse.launcher.ui.ILauncherUIConstants;
import org.jboss.ide.eclipse.launcher.ui.LauncherUIImages;
import org.jboss.ide.eclipse.launcher.ui.LauncherUIMessages;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   18 mai 2003
 */
public class LogFileViewer extends ViewPart
{
   /** Description of the Field */
   protected Action closeAction;
   /** Description of the Field */
   protected IDocumentListener documentListener;
   /** Description of the Field */
   protected TabFolder fFolder;
   /** Description of the Field */
   protected TextViewer fViewer;
   /** Description of the Field */
   protected HashMap logTabs = new HashMap();
   /** Description of the Field */
   protected Action refreshAction;


   /**
    * @param parent  Description of the Parameter
    * @see           org.eclipse.ui.IWorkbenchPart#createPartControl(Composite)
    */
   public void createPartControl(Composite parent)
   {
      fFolder = new TabFolder(parent, SWT.NONE);
      fViewer = new TextViewer(fFolder, SWT.H_SCROLL | SWT.V_SCROLL);
      fViewer.getTextWidget().setFont(JFaceResources.getTextFont());
      fFolder.addSelectionListener(
         new SelectionAdapter()
         {
            public void widgetSelected(SelectionEvent e)
            {
               e.doit = refreshDocument((TabItem) e.item);
            }
         });
      documentListener = new ViewDocumentListener();
      makeActions();
      initContextMenu();
      fillLocalToolBar(getViewSite().getActionBars().getToolBarManager());
   }


   /** Description of the Method */
   public void doClose()
   {
      try
      {
         LogTab logTab = findLogTab(getSelectedItem());
         logTab.close();
         logTabs.remove(logTab.key);
      }
      catch (IOException e)
      {
         LauncherPlugin.getDefault().showErrorMessage(e.getMessage());
      }
      int index = fFolder.getSelectionIndex();
      getSelectedItem().dispose();
      if (fFolder.getItemCount() == 0)
      {
         closeAction.setEnabled(false);
         refreshAction.setEnabled(false);
      }
      else
      {
         if (index == 0)
         {
            fFolder.setSelection(0);
         }
         else
         {
            fFolder.setSelection(index - 1);
         }
      }
   }


   /** Description of the Method */
   public void doRefresh()
   {
      try
      {
         findLogTab(getSelectedItem()).document.synchronize();
      }
      catch (IOException e)
      {
         LauncherPlugin.getDefault().showErrorMessage(e.getMessage());
      }
   }


   /**
    * Description of the Method
    *
    * @param logFile  Description of the Parameter
    * @return         Description of the Return Value
    */
   public boolean hasLog(LogFile logFile)
   {
      if (logFile == null)
      {
         return false;
      }
      String key =
            logFile.getConfiguration().getName() + logFile.getFileName();
      return logTabs.containsKey(key);
   }


   /**
    * Gets the available attribute of the LogFileViewer object
    *
    * @return   The available value
    */
   public boolean isAvailable()
   {
      return !(fViewer == null
            || fViewer.getControl() == null
            || fViewer.getControl().isDisposed());
   }


   /**
    * Description of the Method
    *
    * @param logFile  Description of the Parameter
    */
   public void openLog(LogFile logFile)
   {
      boolean monitor = false;
      String key =
            logFile.getConfiguration().getName() + logFile.getFileName();
      if (!logTabs.containsKey(key))
      {
         LogFileDocument document;
         TabItem item;
         try
         {
            document = new LogFileDocument(new File(logFile.getFileName()), logFile.getPollingIntervall());
            item = new TabItem(fFolder, SWT.NONE);
            item.setControl(fViewer.getControl());
            File file = new File(logFile.getFileName());
            item.setText(file.getName());
            item.setToolTipText(file.getPath());
            logTabs.put(key, new LogTab(key, item, document));
            document.addDocumentListener(documentListener);
            document.setMonitor(true);
         }
         catch (IOException e)
         {
            LauncherPlugin.getDefault().showErrorMessage(e.getMessage());
            return;
         }
      }
      LogTab logTab = (LogTab) logTabs.get(key);
      try
      {
         showDocument(logTab.document, monitor);
         fFolder.setSelection(new TabItem[]{logTab.item});
      }
      catch (Exception e)
      {
         LauncherPlugin.getDefault().showErrorMessage(e.getMessage());
         AbstractPlugin.log(e);
         e.printStackTrace();
      }
   }


   /**
    * @see   org.eclipse.ui.IWorkbenchPart#setFocus()
    */
   public void setFocus()
   {
      fFolder.setFocus();
   }


   /**
    * Description of the Method
    *
    * @param item  Description of the Parameter
    * @return      Description of the Return Value
    */
   protected LogTab findLogTab(TabItem item)
   {
      if (item == null)
      {
         return null;
      }
      LogTab logTab;
      for (Iterator iter = logTabs.values().iterator(); iter.hasNext(); )
      {
         logTab = (LogTab) iter.next();
         if (logTab.item == item)
         {
            return logTab;
         }
      }
      return null;
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected LogTab findLogTab()
   {
      TabItem[] items = fFolder.getSelection();
      if (items.length > 0)
      {
         return findLogTab(items[0]);
      }
      return null;
   }


   /**
    * Gets the selectedItem attribute of the LogFileViewer object
    *
    * @return   The selectedItem value
    */
   protected TabItem getSelectedItem()
   {
      TabItem[] items = fFolder.getSelection();
      if (items.length > 0)
      {
         return items[0];
      }
      return null;
   }


   /**
    * Description of the Method
    *
    * @param item  Description of the Parameter
    * @return      Description of the Return Value
    */
   protected boolean refreshDocument(TabItem item)
   {
      LogTab logTab = findLogTab(item);
      if (logTab != null)
      {
         showDocument(logTab.document, false);
      }
      refreshAction.setEnabled(true);
      closeAction.setEnabled(true);
      return true;
   }


   /**
    * Description of the Method
    *
    * @param document  Description of the Parameter
    * @param monitor   Description of the Parameter
    */
   protected void showDocument(LogFileDocument document, boolean monitor)
   {
      fViewer.setDocument(document);
      fViewer.setTopIndex(document.getNumberOfLines());
   }


   /**
    * Method fillContextMenu.
    *
    * @param manager  Description of the Parameter
    */
   private void fillContextMenu(IMenuManager manager)
   {
      manager.add(refreshAction);
      manager.add(closeAction);
      // Other plug-ins can contribute there actions here
      manager.add(new Separator("Additions"));//$NON-NLS-1$
   }


   /**
    * Description of the Method
    *
    * @param manager  Description of the Parameter
    */
   private void fillLocalToolBar(IToolBarManager manager)
   {
      manager.add(closeAction);
      manager.add(refreshAction);
   }


   /** Description of the Method */
   private void initContextMenu()
   {
      MenuManager manager = new MenuManager("#PopupMenu");//$NON-NLS-1$
      manager.setRemoveAllWhenShown(true);
      manager.addMenuListener(
         new IMenuListener()
         {
            public void menuAboutToShow(IMenuManager manager)
            {
               LogFileViewer.this.fillContextMenu(manager);
            }
         });
      Menu menu = manager.createContextMenu(fViewer.getControl());
      fViewer.getControl().setMenu(menu);
      getSite().registerContextMenu(manager, null);
   }


   /** Method makeActions. */
   private void makeActions()
   {
      closeAction =
         new Action()
         {
            public void run()
            {
               doClose();
            }
         };
      closeAction.setText(LauncherUIMessages.getString("LogFileViewer.close_3"));//$NON-NLS-1$
      closeAction.setToolTipText(LauncherUIMessages.getString("LogFileViewer.close_4"));//$NON-NLS-1$
      closeAction.setEnabled(false);
      closeAction.setImageDescriptor(LauncherUIImages.getImageDescriptor(ILauncherUIConstants.IMG_CTOOL_CLOSE_LOGFILE));
      closeAction.setDisabledImageDescriptor(LauncherUIImages.getImageDescriptor(ILauncherUIConstants.IMG_DTOOL_CLOSE_LOGFILE));
      refreshAction =
         new Action()
         {
            public void run()
            {
               doRefresh();
            }
         };
      refreshAction.setText(LauncherUIMessages.getString("LogFileViewer.refresh_5"));//$NON-NLS-1$
      refreshAction.setToolTipText(LauncherUIMessages.getString("LogFileViewer.refresh_6"));//$NON-NLS-1$
      refreshAction.setEnabled(false);
      refreshAction.setImageDescriptor(LauncherUIImages.getImageDescriptor(ILauncherUIConstants.IMG_CTOOL_REFRESH_LOGFILE));
      refreshAction.setDisabledImageDescriptor(LauncherUIImages.getImageDescriptor(ILauncherUIConstants.IMG_DTOOL_REFRESH_LOGFILE));
   }


   /**
    * Description of the Class
    *
    * @author    Administrator
    * @version   $Revision$
    * @created   18 mai 2003
    */
   protected class ViewDocumentListener implements IDocumentListener
   {
      /**
       * Description of the Method
       *
       * @param event  Description of the Parameter
       */
      public void documentAboutToBeChanged(DocumentEvent event) { }


      /**
       * Description of the Method
       *
       * @param event  Description of the Parameter
       */
      public void documentChanged(final DocumentEvent event)
      {
         if (!isAvailable())
         {
            return;
         }
         LogTab logTab = findLogTab(getSelectedItem());
         // When the first selection is done, the documentlistener is registered before the document is assigned
         // to the viewer. Therefore we have to check for the viewer-document.
         if (logTab != null
               && event.getDocument() == logTab.document
               && fViewer.getDocument() != null)
         {
            fViewer.refresh();
            fViewer.setTopIndex(event.getDocument().getNumberOfLines());
         }
      }
   }


   /**
    * Description of the Class
    *
    * @author    Administrator
    * @version   $Revision$
    * @created   18 mai 2003
    */
   protected static class LogTab
   {
      /** Description of the Field */
      protected LogFileDocument document;
      /** Description of the Field */
      protected TabItem item;
      /** Description of the Field */
      protected String key;


      /**
       *Constructor for the LogTab object
       *
       * @param key       Description of the Parameter
       * @param item      Description of the Parameter
       * @param document  Description of the Parameter
       */
      public LogTab(String key, TabItem item, LogFileDocument document)
      {
         super();
         this.item = item;
         this.document = document;
         this.key = key;
      }


      /**
       * Description of the Method
       *
       * @exception IOException  Description of the Exception
       */
      protected void close() throws IOException
      {
         document.setMonitor(false);
      }
   }
}
