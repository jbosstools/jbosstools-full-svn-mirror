/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.dialogs;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.JBossCachePlugin;
import org.jboss.ide.eclipse.jbosscache.internal.CacheMessages;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstance;
import org.jboss.ide.eclipse.jbosscache.model.factory.CacheInstanceFactory;
import org.jboss.ide.eclipse.jbosscache.model.internal.RemoteCacheManager;
import org.jboss.ide.eclipse.jbosscache.utils.CacheUtil;
import org.jboss.ide.eclipse.jbosscache.views.config.TreeCacheView;

/**
 * Get the related node fqns from the remote cache
 * @author Owner
 */
public class RefreshDialog extends Dialog
{

   private Label lblNewName;

   private Text txtNewName;

   private TreeCacheView cacheView;

   /**
    * Constructor
    * @param provider
    */
   public RefreshDialog(Shell parentShell, ViewPart provider)
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
      lblNewName.setText("Refreshed Fqn");
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

      if (newName.equals(CacheMessages.RefreshDialog_0))
      {
         MessageDialog.openError(getParentShell(), CacheMessages.RefreshDialog_Content_Error,
               CacheMessages.RefreshDialog_Content_Error_Message);
         txtNewName.setFocus();
         return;
      }

      Object obj = cacheView.getSelection();

      if (obj == null)
         return;
      
      final String fqn = txtNewName.getText().trim();
      
      ICacheRootInstance rootInstance = (ICacheRootInstance) obj;
      final RemoteCacheManager manager = rootInstance.getRemoteCacheManager();
      try{
         
         ProgressMonitorDialog dialog = new ProgressMonitorDialog(getShell());
         dialog.setCancelable(false);
         
         IRunnableWithProgress runnabe = new IRunnableWithProgress(){

            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
            {
               try{
                  manager.setChildNodes(fqn);
               }catch(Exception e){
                  throw new InvocationTargetException(e);
               }
            }
            
         };
         
         dialog.run(true,true,runnabe);
         
         
      }catch(Exception e){
         System.out.println("Hello World");
         e.printStackTrace();
         IStatus status = new Status(IStatus.ERROR, ICacheConstants.CACHE_PLUGIN_UNIQUE_ID, IStatus.ERROR, e.getMessage(),
               e);
         JBossCachePlugin.getDefault().getLog().log(status);
      }
      
      super.okPressed();
   }

   protected void configureShell(Shell shell)
   {
      super.configureShell(shell);
      shell.setText("Refresh From Fqn");
   }

}
