/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.part.ViewPart;
import org.jboss.ide.eclipse.jbosscache.dialogs.ContentViewDialog;
import org.jboss.ide.eclipse.jbosscache.internal.CacheMessages;
import org.jboss.ide.eclipse.jbosscache.utils.CacheUtil;
import org.jboss.ide.eclipse.jbosscache.views.content.NodeContentView;
import org.jboss.ide.eclipse.jbosscache.views.content.TableContentProvider.TableContentModel;

/**
 * This class represents action that is fired when the user select
 * this action in the <code>NodeContentView</code>
 * @author Owner
 * @see org.jboss.ide.eclipse.jbosscache.views.content.NodeContentView
 */
public class ShowObjectFieldsAction extends Action
{

   /**Realted view with this action*/
   private ViewPart viewPart;

   public ShowObjectFieldsAction(String id)
   {
      this(null, id);
   }

   public ShowObjectFieldsAction(ViewPart viewPart, String id)
   {
      super(id);
      this.viewPart = viewPart;
   }

   /**
    * Action
    */
   public void run()
   {

      //Action related with NodeContentView
      if (this.viewPart instanceof NodeContentView)
      {
         NodeContentView contentView = (NodeContentView) viewPart;
         Object selection = contentView.getSelection();
         TableContentModel contentModel = (TableContentModel) selection;
         if (CacheUtil.isImmediate(contentModel.getValue().getClass()))
         {
            MessageDialog.openInformation(viewPart.getViewSite().getShell(),
                  CacheMessages.ShowObjectsFieldsAction_MessageDialog_Title,
                  CacheMessages.ShowObjectsFieldsAction_MessageDialog_Message);
         }
         else
         {
            ContentViewDialog viewDialog = new ContentViewDialog(viewPart.getViewSite().getShell(), contentModel
                  .getValue().getClass().getName(), selection);
            viewDialog.open();
         }
      }
   }
}