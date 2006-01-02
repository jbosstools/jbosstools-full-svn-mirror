/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.ui.dialogs;

import java.util.ArrayList;
import java.util.Stack;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;

/**
 * Abstract class for selecting a resource inside a workspace.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class ResourceSelectionDialog extends ElementTreeSelectionDialog implements ISelectionChangedListener
{
   private boolean completable = true;
   private IResource initialSelection;
   private Text text;


   /**
    * Constructor
    *
    * @param parent           The parent shell
    * @param labelProvider    The label provider
    * @param contentProvider  The content provider
    */
   public ResourceSelectionDialog(Shell parent, ILabelProvider labelProvider, ITreeContentProvider contentProvider)
   {
      super(parent, labelProvider, contentProvider);
      this.setAllowMultiple(false);
      this.initialSelection = null;
   }


   /**
    * When the selection changed in the tree view, the text control is
    * updated accordingly.
    *
    * @param event  The selection event
    */
   public void selectionChanged(SelectionChangedEvent event)
   {
      StructuredSelection selection = (StructuredSelection) event.getSelection();
      if (!selection.isEmpty())
      {
         IResource resource = (IResource) selection.getFirstElement();
         this.setResult(resource);
         this.text.setText(resource.getFullPath().toString());
      }
   }


   /**
    * Override to avoid multiple selection.
    *
    * @param allowMultiple  Flag to indicate multiple selection
    */
   public void setAllowMultiple(boolean allowMultiple)
   {
      if (allowMultiple)
      {
         throw new IllegalArgumentException("Multiple selection are not supported");//$NON-NLS-1$
      }
      super.setAllowMultiple(allowMultiple);
   }


   /**
    * Sets whether or not the text area can edited to complete the resource
    * selection
    *
    * @param completable  true if the resource can be completed
    */
   public void setCompletable(boolean completable)
   {
      this.completable = completable;
   }


   /**
    * Sets the initial selection of the dialog. Convenience method.
    *
    * @param selection  The initial selection
    */
   public void setInitialSelection(Object selection)
   {
      super.setInitialSelection(selection);
      this.initialSelection = (IResource) selection;
   }


   /** Overrides that does nothing */
   protected void computeResult()
   {
      // Do nothing
   }


   /**
    * Create the dialog content.
    *
    * @param parent  Parent composite
    * @return        The content of the dialog
    */
   protected Control createDialogArea(Composite parent)
   {
      Composite result = (Composite) super.createDialogArea(parent);
      this.getTreeViewer().addSelectionChangedListener(this);

      this.text = new Text(result, SWT.BORDER);
      this.text.setEditable(this.completable);
      this.text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      this.text.addModifyListener(
         new ModifyListener()
         {
            public void modifyText(ModifyEvent e)
            {
               String path = ((Text) e.widget).getText();
               IResource resource = ResourceSelectionDialog.this.extractResource(path);
               ResourceSelectionDialog.this.setResult(resource);
            }
         });

      // Retrieve the initial selection and reveal each parent node
      if (this.initialSelection != null)
      {
         Stack parents = new Stack();
         IResource current = this.initialSelection;
         while (true)
         {
            parents.push(current);
            if (current == this.initialSelection.getProject())
            {
               break;
            }
            current = current.getParent();
         }
         while (!parents.isEmpty())
         {
            current = (IResource) parents.pop();
            this.getTreeViewer().expandToLevel(current, 1);
         }
      }

      return result;
   }


   /**
    * Extract a resource from the text area
    *
    * @param path  The path from the text area
    * @return      A valid resource (may be inexistant in the workspace)
    */
   protected abstract IResource extractResource(String path);


   /** Overrides to update the resource */
   protected void okPressed()
   {
      String path = this.text.getText();
      IResource resource = this.extractResource(path);
      this.setResult(resource);
      super.okPressed();
   }


   /**
    * Sets the result of the dialog.
    *
    * @param resource  The new result
    */
   protected void setResult(IResource resource)
   {
      ArrayList list = new ArrayList();
      list.add(resource);
      this.setResult(list);
      this.updateOKStatus();
   }
}
