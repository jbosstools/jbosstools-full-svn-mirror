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
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.jboss.ide.eclipse.core.util.ProjectUtil;
import org.jboss.ide.eclipse.ui.UIMessages;
import org.jboss.ide.eclipse.ui.UIPlugin;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class FileSelectionDialog extends ResourceSelectionDialog
{
   /**
    * Constructor
    *
    * @param parent           The parent shell
    * @param labelProvider    The label provider
    * @param contentProvider  The content provider
    */
   public FileSelectionDialog(Shell parent, ILabelProvider labelProvider, ITreeContentProvider contentProvider)
   {
      super(parent, labelProvider, contentProvider);

      this.setTitle(UIMessages.getString("FileSelectionDialog.title"));//$NON-NLS-1$
      this.setMessage(UIMessages.getString("FileSelectionDialog.message"));//$NON-NLS-1$
      this.addFilter(new FileResourceViewerFilter());
      this.setSorter(new FileResourceViewerSorter());
      this.setValidator(new FileResourceValidator());
   }


   /**
    * Extract a File resource from the given path
    *
    * @param path  The path from the text area
    * @return      A valid resource (may be inexistant in the workspace)
    */
   protected IResource extractResource(String path)
   {
      IResource resource = ProjectUtil.getFile(path);
      return resource;
   }


   /**
    * Validator for the dialog. Called when a validation is done.
    *
    * @author    Laurent Etiemble
    * @version   $Revision$
    * @created   4 juillet 2003
    */
   protected class FileResourceValidator implements ISelectionStatusValidator
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
            if (resource.getType() != IResource.FILE)
            {
               status = new Status(IStatus.ERROR, UIPlugin.getUniqueIdentifier(), 0, UIMessages.getString("FileSelectionDialog.select.file.status.error"), null);//$NON-NLS-1$
            }
         }
         return status;
      }
   }


   /**
    * Filter to display the following resources :
    * - a file
    * - not beginning with a dot
    * This should be ok to display folders and projects.
    *
    * @author    letiembl
    * @version   $Revision$
    * @created   4 juillet 2003
    */
   protected class FileResourceViewerFilter extends ViewerFilter
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
         if (resource.getName().startsWith(".")//$NON-NLS-1$
         )
         {
            return false;
         }
         return true;
      }
   }


   /**
    * Sorter to sort folder before files
    *
    * @author    Laurent Etiemble
    * @version   $Revision$
    * @created   4 juillet 2003
    */
   protected class FileResourceViewerSorter extends ViewerSorter
   {
      /**
       * Categorise elements
       *
       * @param element  Element to categorise
       * @return         Int to sort elements
       */
      public int category(Object element)
      {
         IResource resource = (IResource) element;
         switch (resource.getType())
         {
            case IResource.FOLDER:
               return 0;
            case IResource.FILE:
            default:
               return 1;
         }
      }
   }
}
