/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.dialogs;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.jboss.ide.eclipse.jbosscache.internal.CacheMessages;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstance;
import org.jboss.ide.eclipse.jbosscache.model.factory.CacheInstanceFactory;
import org.jboss.ide.eclipse.jbosscache.views.config.TreeCacheView;

/**
 * Renames dialog that appears when renaming selected cache instance
 * @author Owner
 */
public class RenameDialog extends Dialog
{

   private Label lblNewName;

   private Text txtNewName;

   private TreeCacheView cacheView;

   /**
    * Constructor
    * @param provider
    */
   public RenameDialog(Shell parentShell, ViewPart provider)
   {
      super(parentShell);
      cacheView = (TreeCacheView) provider;
      setDefaultOrientation(SWT.LEFT_TO_RIGHT);
      setShellStyle(SWT.SHELL_TRIM);
   }

   /**
    * Contents of the dialog
    */
   protected Control createDialogArea(Composite parent)
   {
      Composite comp = (Composite) super.createDialogArea(parent);

      GridLayout gridLayout = new GridLayout(2, false);
      comp.setLayout(gridLayout);

      GridData gridData = new GridData(GridData.FILL_HORIZONTAL);

      lblNewName = new Label(comp, SWT.NONE);
      lblNewName.setText(CacheMessages.RenameDialog_Label_Text);
      txtNewName = new Text(comp, SWT.BORDER);
      txtNewName.setLayoutData(gridData);
      return comp;
   }

   /**
    * Ok pressed
    */
   protected void okPressed()
   {
      String newName = txtNewName.getText().trim();

      if (newName.equals(""))
      {
         MessageDialog.openError(getParentShell(), CacheMessages.RenameAction_Error_Dialog_Title,
               CacheMessages.RenameAction_Error_Dialog_Message);
         txtNewName.setFocus();
         return;
      }

      Object obj = cacheView.getSelection();

      if (obj == null)
         return;

      //Check name conflict
      ICacheRootInstance mainRootInstance = CacheInstanceFactory.getCacheRootMainInstance();
      if (mainRootInstance != null)
      {
         if (mainRootInstance.getRootInstanceChilds() != null)
         {
            List childList = mainRootInstance.getRootInstanceChilds();
            for (int i = 0, j = childList.size(); i < j; i++)
            {
               ICacheRootInstance rootChilds = (ICacheRootInstance) childList.get(i);
               if (rootChilds.getRootName().equals(newName))
               {
                  MessageDialog.openError(getParentShell(), CacheMessages.RenameAction_Error_Dialog_Title,
                        CacheMessages
                              .bind(CacheMessages.ImportActionDialog_Already_Exist_Cache_Dialog_Message, newName));
                  txtNewName.setFocus();
                  return;
               }
            }
         }

      }

      ICacheRootInstance rootInstance = (ICacheRootInstance) obj;
      rootInstance.setRootLabel(newName);
      rootInstance.setRootName(newName);

      cacheView.getTreeViewer().refresh(rootInstance, true);
      super.okPressed();
   }

   protected void configureShell(Shell shell)
   {
      super.configureShell(shell);
      shell.setText(CacheMessages.RenameDialog_Dialog_Title);
   }

}
