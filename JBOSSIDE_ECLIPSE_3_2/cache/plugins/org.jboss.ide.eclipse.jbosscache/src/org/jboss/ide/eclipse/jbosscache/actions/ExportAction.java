/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.actions;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.JBossCachePlugin;
import org.jboss.ide.eclipse.jbosscache.internal.CacheMessages;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstance;
import org.jboss.ide.eclipse.jbosscache.views.config.TreeCacheView;

public class ExportAction extends AbstractCacheAction
{

   private static final String TreeCacheView_Export_Action_FileDialog_Title = CacheMessages.ExportAction_TreeCacheView_Export_Action_FileDialog_Title;

   public ExportAction(TreeCacheView view, String id)
   {
      super(view, id);
      setImageDescriptor(JBossCachePlugin.getDefault().getImageRegistry().getDescriptor(
            ICacheConstants.IMAGE_KEY_EXPORT_WIZ));
      setText(CacheMessages.TreeCacheView_exportAction);
      setToolTipText(CacheMessages.TreeCacheView_exportAction);
   }

   /**
    * Actual run
    */
   public void run()
   {

      ICacheRootInstance rootInstance = (ICacheRootInstance) getTreeViewer().getSelection();
      ProgressMonitorDialog monitorDialog = new ProgressMonitorDialog(getTreeViewer().getShell());

      if (rootInstance != null)
      {

         String configFileName = rootInstance.getRootConfigurationFileName();
         File originalFile = new File(configFileName);
         if (!originalFile.exists())
         {
            MessageDialog.openError(getTreeViewer().getShell(),
                  CacheMessages.ExportAction_FileNot_Found_Error_Dialog_Title, CacheMessages.bind(
                        CacheMessages.ExportAction_FileNot_Found_Error_Dialog_Message, rootInstance.getRootName()));
            return;
         }

         FileDialog fileDialog = new FileDialog(getTreeViewer().getShell(), SWT.SAVE);
         fileDialog.setFilterExtensions(new String[]
         {"*.xml"}); //$NON-NLS-1$
         fileDialog.setFileName(((ICacheRootInstance) getTreeViewer().getSelection()).getRootName());
         fileDialog.setText(TreeCacheView_Export_Action_FileDialog_Title);
         String fileName = fileDialog.open();

         if (fileName == null)
            return;
         try
         {
            File file = new File(fileName);
            if (file.exists())
            {

               boolean ok = MessageDialog.openQuestion(getTreeViewer().getShell(), CacheMessages.bind(
                     CacheMessages.ExportAction_TreeCacheView_AlreadyExistFile_Dialog_Title, rootInstance.getRootName()
                           + ".xml"), CacheMessages.bind(
                     CacheMessages.ExportAction_TreeCacheView_AlreadyExistFile_Dialog_Message, rootInstance
                           .getRootName()
                           + ".xml"));
               if (!ok)
                  return;
            }
            monitorDialog.run(true, false, getRunnable(rootInstance, fileName));
         }
         catch (Exception e)
         {
            IStatus status = new Status(IStatus.ERROR, ICacheConstants.CACHE_PLUGIN_UNIQUE_ID, IStatus.OK, e
                  .getMessage(), e);
            JBossCachePlugin.getDefault().getLog().log(status);

         }
      }
   }

   public IRunnableWithProgress getRunnable(final ICacheRootInstance rootInstance, final String fileName)
   {
      return new IRunnableWithProgress()
      {

         public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
         {
            FileWriter writer = null;
            FileReader fileReader = null;
            try
            {
               File file = new File(fileName);
               if (!file.exists())
               {
                  file.createNewFile();
               }

               writer = new FileWriter(file);

               File fileOriginal = new File(rootInstance.getRootConfigurationFileName());
               fileReader = new FileReader(fileOriginal);

               int i;
               while ((i = fileReader.read()) != -1)
               {
                  writer.write(i);
               }

            }
            catch (Exception e)
            {
               e.printStackTrace();
               throw new InvocationTargetException(e);
            }
            finally
            {
               try
               {
                  if (fileReader != null)
                  {
                     fileReader.close();
                  }
                  if (writer != null)
                  {
                     writer.flush();
                     writer.close();
                  }
               }
               catch (Exception e)
               {
                  throw new InvocationTargetException(e);
               }
            }
         }

      };
   }
}
