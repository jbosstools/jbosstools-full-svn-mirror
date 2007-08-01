/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.ui.dialogs;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.jboss.ide.eclipse.core.util.ProjectUtil;
import org.jboss.ide.eclipse.ui.UIMessages;
import org.jboss.ide.eclipse.ui.UIPlugin;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class FolderSelectionDialog extends ResourceSelectionDialog
{
   /**
    * Constructor
    *
    * @param parent           The parent shell
    * @param labelProvider    The label provider
    * @param contentProvider  The content provider
    */
   public FolderSelectionDialog(Shell parent, ILabelProvider labelProvider, ITreeContentProvider contentProvider)
   {
      super(parent, labelProvider, contentProvider);

      this.setTitle(UIMessages.getString("FolderSelectionDialog.title"));//$NON-NLS-1$
      this.setMessage(UIMessages.getString("FolderSelectionDialog.message"));//$NON-NLS-1$
      this.addFilter(new FolderResourceViewerFilter());
   }


   /**
    * Sets the acceptFolderOnly attribute of the FolderSelectionDialog object
    *
    * @param value  The new acceptFolderOnly value
    */
   public void setAcceptFolderOnly(boolean value)
   {
      if (value)
      {
         this.setValidator(new FolderResourceValidator());
      }
      else
      {
         this.setValidator(null);
      }
   }


   /**
    * Extract a Folder or a Project resource from the given path
    *
    * @param path  The path from the text area
    * @return      A valid resource (may be inexistant in the workspace)
    */
   protected IResource extractResource(String path)
   {
      IResource resource = ProjectUtil.getFolder(path);
      return resource;
   }


   /**
    * Validator for the dialog. Called when a validation is done.
    *
    * @author    Laurent Etiemble
    * @version   $Revision$
    * @created   4 juillet 2003
    */
   protected class FolderResourceValidator implements ISelectionStatusValidator
   {
      /**
       * Return a status according to the selection
       *
       * @param selection  Selection to validate
       * @return           A status (Ok or Nok)
       */
      public IStatus validate(Object[] selection)
      {
         IStatus status = new Status(IStatus.OK, UIPlugin.getUniqueIdentifier(), 0, "", null);//$NON-NLS-1$
         if ((selection != null) && (selection.length > 0))
         {
            IResource resource = (IResource) selection[0];
            if (resource.getType() != IResource.FOLDER)
            {
               status = new Status(IStatus.ERROR, UIPlugin.getUniqueIdentifier(), 0, UIMessages.getString("FolderSelectionDialog.select.folder.status.error"), null);//$NON-NLS-1$
            }
         }
         return status;
      }
   }


   /**
    * Filter to display the following resources :
    * - not a file
    * - not beginning with a dot
    * This should be ok to display folders and projects.
    *
    * @author    Laurent Etiemble
    * @version   $Revision$
    * @created   4 juillet 2003
    */
   protected class FolderResourceViewerFilter extends ViewerFilter
   {
      /**
       * Selection of element
       *
       * @param viewer         The requester viewer
       * @param parentElement  The parent element
       * @param element        The current element
       * @return               True if the element has to be shown
       */
      public boolean select(Viewer viewer, Object parentElement, Object element)
      {
         IResource resource = (IResource) element;
         if ((resource.getType() == IResource.FILE)
            || (resource.getName().startsWith("."))//$NON-NLS-1$
         )
         {
            return false;
         }
         return true;
      }
   }
}
