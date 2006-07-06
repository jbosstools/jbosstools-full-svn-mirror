/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.actions;

import java.io.File;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.editors.input.CacheFileEditorInput;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstance;
import org.jboss.ide.eclipse.jbosscache.model.internal.TreeCacheManager;
import org.jboss.ide.eclipse.jbosscache.utils.CacheUtil;
import org.jboss.ide.eclipse.jbosscache.views.config.TreeCacheView;

/**
 * This class represents action that is related with cache configuration remove
 * @author Owner
 */
public class DeleteConfigurationAction extends AbstractCacheAction
{

   public DeleteConfigurationAction()
   {
      this(null, null);
   }

   public DeleteConfigurationAction(TreeCacheView view, String actionId)
   {
      super(view, actionId);
   }
   
   private class CustomMessageDialog extends MessageDialog{

      private boolean isCheck = false;
      private ICacheRootInstance rootInstance;
      
      public CustomMessageDialog(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage, int dialogImageType, String[] dialogButtonLabels, int defaultIndex)
      {
         super(parentShell, dialogTitle, dialogTitleImage, dialogMessage, dialogImageType, dialogButtonLabels, defaultIndex);
      }

      protected Control createCustomArea(Composite parent)
      {
         Button btn = new Button(parent,SWT.CHECK);
         btn.setText("Configuration file will be removed from the file system");
         btn.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
         
         if(rootInstance.isRemoteCache())
            btn.setEnabled(false);
         else
            btn.setEnabled(true);
         
         btn.addSelectionListener(new SelectionListener(){

            public void widgetSelected(SelectionEvent e)
            {
               Button btn = (Button)e.widget;
               if(btn.getSelection())
                  isCheck = true;
               else
                  isCheck = false;
               
            }

            public void widgetDefaultSelected(SelectionEvent e)
            {
               widgetSelected(e);
               
            }

            
         });
           
         return btn;
      }
      
      public boolean isCheck(){
         return isCheck;
      }
      
      public void setRootInstance(ICacheRootInstance rootInstance){
         this.rootInstance = rootInstance;
      }
            
   }

   /**
    * Delete configuration action
    */
   public void run()
   {
      ICacheRootInstance rootInstance = (ICacheRootInstance) getTreeViewer().getSelection();
      
      CustomMessageDialog msgDialog = new CustomMessageDialog(getTreeViewer().getShell(), CacheUtil
            .getResourceBundleValue(ICacheConstants.TREECACHEVIEW_DELETE_CONFIGURATION_DIALOG_TITLE), null, CacheUtil
            .getResourceBundleValue(ICacheConstants.TREECACHEVIEW_DELETE_CONFIGURATION_DIALOG_MESSAGE),
            MessageDialog.QUESTION, new String[]
            {IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL}, 1);
      
      msgDialog.setRootInstance(rootInstance);
     
      int delete = msgDialog.open();
      

      if (delete == IDialogConstants.OK_ID)
      {  
         if(!rootInstance.isRemoteCache())
         {
            if (rootInstance.isConnected())
            {
               TreeCacheManager manager = rootInstance.getTreeCacheManager();
               TreeCacheManager.stopService_(manager);
               TreeCacheManager.destroyService_(manager);
            }
            
            boolean isCheck = msgDialog.isCheck();
            if(isCheck)
            {                              
               File file = new File(rootInstance.getRootConfigurationFileName());
               IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findEditor(new CacheFileEditorInput(file));
               
               if(part != null){
                  PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(part,false);
               }
               
               if(file.exists())
                  file.delete();
               
            }
         }
                  
         rootInstance.deleteRoot();
      }
   }

}
