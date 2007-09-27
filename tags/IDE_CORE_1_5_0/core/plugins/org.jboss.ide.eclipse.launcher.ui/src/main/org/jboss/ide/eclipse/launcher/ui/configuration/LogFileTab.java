/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.launcher.ui.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.internal.debug.ui.launcher.JavaLaunchConfigurationTab;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.launcher.core.ServerLaunchManager;
import org.jboss.ide.eclipse.launcher.core.constants.IServerLaunchConfigurationConstants;
import org.jboss.ide.eclipse.launcher.core.logfiles.LogFile;
import org.jboss.ide.eclipse.launcher.ui.ILauncherUIConstants;
import org.jboss.ide.eclipse.launcher.ui.LauncherUIImages;
import org.jboss.ide.eclipse.launcher.ui.LauncherUIMessages;
import org.jboss.ide.eclipse.launcher.ui.logfiles.LogFileDialog;
import org.jboss.ide.eclipse.ui.util.ListContentProvider;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   18 mai 2003
 */
public class LogFileTab extends JavaLaunchConfigurationTab implements ISelectionChangedListener
{
   /** Description of the Field */
   protected Button addButton;
   /** Description of the Field */
   protected Composite buttonComposite;
   /** Description of the Field */
   protected Button editButton;
   /** Description of the Field */
   protected String filterPath;
   /** Description of the Field */
   protected Composite logFileComposite;
   /** Description of the Field */
   protected List logFiles = new ArrayList();
   /** Description of the Field */
   protected Button removeButton;
   /** Description of the Field */
   protected TableViewer viewer;


   /**
    * @param parent  Description of the Parameter
    * @see           org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(Composite)
    */
   public void createControl(Composite parent)
   {
      logFileComposite = new Composite(parent, SWT.NONE);
      setControl(logFileComposite);
      GridLayout mainLayout = new GridLayout();
      mainLayout.numColumns = 2;
      logFileComposite.setLayout(mainLayout);
      makeTable(logFileComposite);
      makeButtonComposite(logFileComposite);
   }


   /**
    * @return   The image value
    * @see      org.eclipse.debug.ui.ILaunchConfigurationTab#getImage()
    */
   public Image getImage()
   {
      return LauncherUIImages.getImage(ILauncherUIConstants.IMG_OBJS_LOGFILE_TAB);
   }


   /**
    * @return   The name value
    * @see      org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
    */
   public String getName()
   {
      return LauncherUIMessages.getString("LogFileTab.Log_Files_3");//$NON-NLS-1$
   }


   /**
    * @param configuration  Description of the Parameter
    * @see                  org.eclipse.debug.ui.ILaunchConfigurationTab#initializeFrom(ILaunchConfiguration)
    */
   public void initializeFrom(ILaunchConfiguration configuration)
   {
      try
      {
         logFiles.clear();
         logFiles.addAll(ServerLaunchManager.getInstance().getUserLogFiles(configuration));
         viewer.refresh();
      }
      catch (CoreException e)
      {
         AbstractPlugin.log(e);
      }
   }


   /**
    * @param configuration  Description of the Parameter
    * @see                  org.eclipse.debug.ui.ILaunchConfigurationTab#performApply(ILaunchConfigurationWorkingCopy)
    */
   public void performApply(ILaunchConfigurationWorkingCopy configuration)
   {
      HashMap map = new HashMap();
      for (Iterator iter = logFiles.iterator(); iter.hasNext(); )
      {
         LogFile logFile = (LogFile) iter.next();
         map.put(logFile.getFileName(), "" + logFile.getPollingIntervall());//$NON-NLS-1$
      }
      configuration.setAttribute(IServerLaunchConfigurationConstants.ATTR_LOG_FILES, map);
   }


   /**
    * @param event  Description of the Parameter
    * @see          org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(SelectionChangedEvent)
    */
   public void selectionChanged(SelectionChangedEvent event)
   {
      editButton.setEnabled(true);
      removeButton.setEnabled(true);
   }


   /**
    * @param configuration  The new defaults value
    * @see                  org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(ILaunchConfigurationWorkingCopy)
    */
   public void setDefaults(ILaunchConfigurationWorkingCopy configuration)
   {
   }


   /**
    * Description of the Method
    *
    * @param parent  Description of the Parameter
    */
   protected void makeButtonComposite(Composite parent)
   {
      buttonComposite = new Composite(parent, SWT.NONE);
      GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
      buttonComposite.setLayoutData(gd);
      RowLayout buttonCompositeLayout = new RowLayout();
      buttonCompositeLayout.type = SWT.VERTICAL;
      buttonCompositeLayout.pack = false;
      buttonComposite.setLayout(buttonCompositeLayout);
      addButton = new Button(buttonComposite, SWT.PUSH);
      addButton.setText(LauncherUIMessages.getString("LogFileTab.add_6"));//$NON-NLS-1$

      addButton.addSelectionListener(
         new SelectionAdapter()
         {
            public void widgetSelected(SelectionEvent e)
            {
               // We don't have to know the configuration here. A LogFile is
               // persisted without this information
               // as the information is contained in the embracing
               // LaunchConfiguration. After creating a LogFile
               // from the store it will be added. It's a bit of a hack.
               LogFile logFile = new LogFile(null, "", LogFile.DEFAULT_POLLING_INTERVALL);//$NON-NLS-1$
               LogFileDialog dialog = new LogFileDialog(LogFileTab.this.getShell(), logFile, filterPath);
               int code = dialog.open();
               if (code == Window.OK)
               {
                  logFiles.add(logFile);
                  filterPath = new File(logFile.getFileName()).getParent();
                  viewer.refresh();
                  setDirty(true);
                  updateLaunchConfigurationDialog();
               }
            }
         });

      editButton = new Button(buttonComposite, SWT.PUSH);
      editButton.setText(LauncherUIMessages.getString("LogFileTab.edit_8"));//$NON-NLS-1$
      editButton.setEnabled(false);
      editButton.addSelectionListener(
         new SelectionAdapter()
         {
            public void widgetSelected(SelectionEvent e)
            {
               LogFile logFile = (LogFile) ((IStructuredSelection) viewer.getSelection()).getFirstElement();
               LogFileDialog dialog = new LogFileDialog(LogFileTab.this.getShell(), logFile, logFile.getFileName());
               int code = dialog.open();
               if (code == Window.OK)
               {
                  viewer.refresh();
                  setDirty(true);
                  updateLaunchConfigurationDialog();
               }
            }
         });
      removeButton = new Button(buttonComposite, SWT.PUSH);
      removeButton.setText(LauncherUIMessages.getString("LogFileTab.remove_9"));//$NON-NLS-1$
      removeButton.setEnabled(false);
      removeButton.addSelectionListener(
         new SelectionAdapter()
         {
            public void widgetSelected(SelectionEvent e)
            {
               LogFile logFile = (LogFile) ((IStructuredSelection) viewer.getSelection()).getFirstElement();
               logFiles.remove(logFile);
               viewer.refresh();
               setDirty(true);
               updateLaunchConfigurationDialog();
            }
         });
   }


   /**
    * Description of the Method
    *
    * @param parent  Description of the Parameter
    */
   protected void makeTable(Composite parent)
   {
      Table table = new Table(parent, SWT.FULL_SELECTION | SWT.BORDER);
      GridData gridData = new GridData(GridData.FILL_BOTH);
      table.setLayoutData(gridData);
      TableColumn column1 = new TableColumn(table, SWT.CENTER);
      column1.setText(LauncherUIMessages.getString("LogFileTab.Filename_4"));//$NON-NLS-1$
      TableColumn column2 = new TableColumn(table, SWT.CENTER);
      column2.setText(LauncherUIMessages.getString("LogFileTab.Polling_5"));//$NON-NLS-1$
      TableLayout layout = new TableLayout();
      layout.addColumnData(new ColumnWeightData(85));
      layout.addColumnData(new ColumnWeightData(15, 30, false));
      table.setLayout(layout);
      table.setHeaderVisible(true);
      // table.pack();
      viewer = new TableViewer(table);
      viewer.setContentProvider(new ListContentProvider());
      viewer.setLabelProvider(new ViewLabelProvider());
      viewer.setInput(logFiles);
      viewer.setSorter(new ViewerSorter());
      viewer.addSelectionChangedListener(this);
   }


   /**
    * Description of the Class
    *
    * @author    Administrator
    * @version   $Revision$
    * @created   18 mai 2003
    */
   class ViewLabelProvider extends LabelProvider implements ITableLabelProvider
   {
      /**
       * Gets the columnImage attribute of the ViewLabelProvider object
       *
       * @param element      Description of the Parameter
       * @param columnIndex  Description of the Parameter
       * @return             The columnImage value
       */
      public Image getColumnImage(Object element, int columnIndex)
      {
         return null;
      }


      /**
       * Gets the columnText attribute of the ViewLabelProvider object
       *
       * @param element      Description of the Parameter
       * @param columnIndex  Description of the Parameter
       * @return             The columnText value
       */
      public String getColumnText(Object element, int columnIndex)
      {
         LogFile logFile = (LogFile) element;
         switch (columnIndex)
         {
            case 0:
               return logFile.getFileName();
            case 1:
               return "" + logFile.getPollingIntervall();//$NON-NLS-1$
            default:
               return null;
         }
      }
   }
}
