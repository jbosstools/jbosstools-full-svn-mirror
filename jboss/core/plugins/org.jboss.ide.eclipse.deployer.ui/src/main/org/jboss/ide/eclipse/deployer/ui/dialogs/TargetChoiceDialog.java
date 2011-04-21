/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.deployer.ui.dialogs;

import java.util.Collection;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.jboss.ide.eclipse.deployer.core.target.ITarget;
import org.jboss.ide.eclipse.deployer.ui.DeployerUIMessages;
import org.jboss.ide.eclipse.deployer.ui.util.TargetLabelProvider;
import org.jboss.ide.eclipse.ui.util.ListContentProvider;
import org.jboss.ide.eclipse.ui.util.StringViewSorter;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class TargetChoiceDialog extends Dialog
{
   /** Description of the Field */
   private Collection choices;
   /** Description of the Field */
   private ITarget data = null;
   /** Description of the Field */
   private StructuredViewer viewer;


   /**
    *Constructor for the DataChoiceDialog object
    *
    * @param parentShell  Description of the Parameter
    * @param choices      Description of the Parameter
    */
   public TargetChoiceDialog(Shell parentShell, Collection choices)
   {
      super(parentShell);
      this.data = null;
      this.choices = choices;
   }


   /**
    * Gets the xDocletData attribute of the DataChoiceDialog object
    *
    * @return   The xDocletData value
    */
   public ITarget getTarget()
   {
      return this.data;
   }


   /**
    * Description of the Method
    *
    * @param shell  Description of the Parameter
    */
   protected void configureShell(Shell shell)
   {
      super.configureShell(shell);
      shell.setText(DeployerUIMessages.getString("TargetChoiceDialog.title"));//$NON-NLS-1$
   }



   /**
    * Description of the Method
    *
    * @param parent  Description of the Parameter
    * @return        Description of the Return Value
    * @see           org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
    */
   protected Control createDialogArea(Composite parent)
   {
      Composite composite = (Composite) super.createDialogArea(parent);

      Table dataList = new Table(composite, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
      dataList.setLayoutData(new GridData(GridData.FILL_BOTH));

      this.viewer = new TableViewer(dataList);
      this.viewer.setContentProvider(new ListContentProvider());
      this.viewer.setLabelProvider(new TargetLabelProvider());
      this.viewer.setSorter(new StringViewSorter());
      this.viewer.setInput(this.choices);
      this.viewer.addDoubleClickListener(new IDoubleClickListener()
         {
            public void doubleClick(DoubleClickEvent event)
            {
               okPressed();
            }
         });

      return composite;
   }


   /** Description of the Method */
   protected void okPressed()
   {
      IStructuredSelection selection = (IStructuredSelection) this.viewer.getSelection();
      if (!selection.isEmpty())
      {
         this.data = (ITarget) selection.getFirstElement();
      }
      super.okPressed();
   }
}
