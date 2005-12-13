/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.actions;

import java.io.File;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.jboss.ide.eclipse.jbosscache.editors.input.CacheFileEditorInput;
import org.jboss.ide.eclipse.jbosscache.internal.CacheMessages;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstance;
import org.jboss.ide.eclipse.jbosscache.views.config.TreeCacheView;

/**
 * This class represnets action that is related with cache configuration parameters
 * @author Gurkaner
 *
 */
public class EditConfigurationAction extends AbstractCacheAction
{

   public EditConfigurationAction()
   {
      this(null, null);
   }

   /**
    * Constructor
    */
   public EditConfigurationAction(TreeCacheView treeView, String actionId)
   {
      super(treeView, actionId);
   }

   public void run()
   {
      ICacheRootInstance rootInstance = (ICacheRootInstance) getTreeViewer().getSelection();
      String fileName = rootInstance.getRootConfigurationFileName();

      try
      {
         File file = new File(fileName);
         if (!file.exists())
         {
            MessageDialog.openError(getTreeViewer().getShell(),
                  CacheMessages.EditConfigurationAction_FileNot_Found_Error_Dialog_Title, CacheMessages.bind(
                        CacheMessages.EditConfigurationAction_FileNot_Found_Error_Dialog_Message, rootInstance
                              .getRootName()));
            return;
         }
         CacheFileEditorInput editorInput = new CacheFileEditorInput(file);
         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(editorInput,
               "org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart");
      }
      catch (PartInitException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

}
