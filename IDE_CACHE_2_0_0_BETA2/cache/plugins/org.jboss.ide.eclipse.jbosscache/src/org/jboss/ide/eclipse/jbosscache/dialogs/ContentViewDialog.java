/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeColumn;
import org.jboss.ide.eclipse.jbosscache.internal.CacheMessages;

/**
 * This class represents dialog that is filled by node content value
 * If the value is instance of object, then this object fileds will be shown
 * to the user via this dialog 
 * @author Owner
 */
public class ContentViewDialog extends Dialog
{

   /**Object name is in shown for values*/
   private String titleForObjectType;

   /**Table tree viewer to show oject values with related fields*/
   private TreeViewer tblTreeViewer;

   private Object inputForViewer;

   private String columnTitles[] =
   {CacheMessages.ContentViewDialog_fieldName, CacheMessages.ContentViewDialog_valueName};

   /**
    * Constructor
    * @param parentShell
    */
   public ContentViewDialog(Shell parentShell, String title, Object obj)
   {
      super(parentShell);
      setShellStyle(SWT.SHELL_TRIM);
      this.titleForObjectType = title;
      this.inputForViewer = obj;
   }

   /*
    *  (non-Javadoc)
    * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
    */
   protected void configureShell(Shell newShell)
   {
      super.configureShell(newShell);
      if (titleForObjectType != null)
         titleForObjectType = NLS.bind(CacheMessages.ContentViewDialog_titleForObjectType, titleForObjectType);

      if (titleForObjectType != null)
         newShell.setText(titleForObjectType);

   }

   /**
    * Create the contents of the dialog (above the button bar).
    * 
    * Subclasses should overide.
    * 
    * @param parent
    *            the parent composite to contain the dialog area
    * @return the dialog area control
    */
   protected Control createDialogArea(Composite parent)
   {
      Composite outer = (Composite) super.createDialogArea(parent);
      createTable(outer);

      return outer;
   }

   /**
    * Create tree table
    * @param outer
    */
   private void createTable(Composite outer)
   {
      tblTreeViewer = new TreeViewer(outer, SWT.FULL_SELECTION);
      tblTreeViewer.setContentProvider(new CViewDialogContentProvider());
      tblTreeViewer.setLabelProvider(new CViewDialogLabelProvider());

      GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
      gridData.heightHint = convertVerticalDLUsToPixels(200);

      tblTreeViewer.getTree().setLayoutData(gridData);
      tblTreeViewer.getTree().setLinesVisible(true);
      tblTreeViewer.getTree().setHeaderVisible(true);
      //tblTreeViewer.setColumnProperties() Use if editing

      int[] columnWidths =
      {convertHorizontalDLUsToPixels(150), convertHorizontalDLUsToPixels(150)};

      // create table headers
      for (int i = 0; i < columnTitles.length; i++)
      {
         TreeColumn column = new TreeColumn(tblTreeViewer.getTree(), SWT.CENTER);
         column.setWidth(columnWidths[i]);
         column.setText(columnTitles[i]);
         column.setAlignment(SWT.LEFT);
         column.setResizable(true);
      }

      if (inputForViewer != null)
         tblTreeViewer.setInput(inputForViewer);

   }
}