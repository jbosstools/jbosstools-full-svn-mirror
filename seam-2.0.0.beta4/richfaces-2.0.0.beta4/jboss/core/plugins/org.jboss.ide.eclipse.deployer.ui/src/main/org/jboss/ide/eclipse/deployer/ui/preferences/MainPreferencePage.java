/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.deployer.ui.preferences;

import java.util.Collection;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.internal.dialogs.ListContentProvider;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.deployer.core.DeployerCorePlugin;
import org.jboss.ide.eclipse.deployer.core.target.ITarget;
import org.jboss.ide.eclipse.deployer.ui.DeployerUIMessages;
import org.jboss.ide.eclipse.deployer.ui.DeployerUIPlugin;
import org.jboss.ide.eclipse.deployer.ui.TargetUIAdapter;
import org.jboss.ide.eclipse.deployer.ui.dialogs.TargetChoiceDialog;
import org.jboss.ide.eclipse.deployer.ui.dialogs.TargetEditDialog;
import org.jboss.ide.eclipse.deployer.ui.util.TargetLabelProvider;
import org.jboss.ide.eclipse.ui.util.StringViewSorter;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class MainPreferencePage extends PreferencePage implements IWorkbenchPreferencePage
{
   private Button addButton;
   private StructuredViewer debugViewer;
   private Button editButton;
   private Button removeButton;
   private StructuredViewer viewer;


   /** Constructor for the MainPreferencePage object */
   public MainPreferencePage()
   {
      super();
      this.setPreferenceStore(DeployerUIPlugin.getDefault().getPreferenceStore());
      this.setDescription(DeployerUIMessages.getString("MainPreferencePage.description"));//$NON-NLS-1$
   }


   /**
    * Description of the Method
    *
    * @param workbench  Description of the Parameter
    */
   public void init(IWorkbench workbench) { }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public boolean performCancel()
   {
      // Reload targets
      DeployerCorePlugin.getDefault().refreshTargets();
      return super.performCancel();
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public boolean performOk()
   {
      // Save targets
      DeployerCorePlugin.getDefault().saveTargets();
      return super.performOk();
   }


   /** Description of the Method */
   protected void assign()
   {
      this.addButton.addSelectionListener(
         new SelectionAdapter()
         {
            public void widgetSelected(SelectionEvent e)
            {
               doAdd();
            }
         });

      this.editButton.addSelectionListener(
         new SelectionAdapter()
         {
            public void widgetSelected(SelectionEvent e)
            {
               doEdit();
            }
         });

      this.removeButton.addSelectionListener(
         new SelectionAdapter()
         {
            public void widgetSelected(SelectionEvent e)
            {
               doRemove();
            }
         });

      this.viewer.addDoubleClickListener(
         new IDoubleClickListener()
         {
            public void doubleClick(DoubleClickEvent event)
            {
               doEdit();
            }
         });
   }


   /**
    * Description of the Method
    *
    * @param parent  Description of the Parameter
    * @return        Description of the Return Value
    */
   protected Composite createButtons(Composite parent)
   {
      // The buttons composite
      Composite composite = new Composite(parent, SWT.NONE);
      GridLayout layout = new GridLayout(1, false);
      layout.marginHeight = 0;
      layout.marginWidth = 0;
      composite.setLayout(layout);
      composite.setLayoutData(new GridData(GridData.FILL_VERTICAL));

      this.addButton = new Button(composite, SWT.PUSH);
      this.addButton.setText(DeployerUIMessages.getString("MainPreferencePage.button.add"));//$NON-NLS-1$
      this.addButton.setToolTipText(DeployerUIMessages.getString("MainPreferencePage.button.add.tip"));//$NON-NLS-1$
      this.addButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      this.editButton = new Button(composite, SWT.PUSH);
      this.editButton.setText(DeployerUIMessages.getString("MainPreferencePage.button.edit"));//$NON-NLS-1$
      this.editButton.setToolTipText(DeployerUIMessages.getString("MainPreferencePage.button.edit.tip"));//$NON-NLS-1$
      this.editButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      this.removeButton = new Button(composite, SWT.PUSH);
      this.removeButton.setText(DeployerUIMessages.getString("MainPreferencePage.button.remove"));//$NON-NLS-1$
      this.removeButton.setToolTipText(DeployerUIMessages.getString("MainPreferencePage.button.remove.tip"));//$NON-NLS-1$
      this.removeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      return composite;
   }


   /**
    * Description of the Method
    *
    * @param parent  Description of the Parameter
    * @return        Description of the Return Value
    */
   protected Control createContents(Composite parent)
   {
      DeployerCorePlugin.getDefault().refreshTargets();
      DeployerCorePlugin.getDefault().refreshDebugTargets();

      Composite composite = new Composite(parent, SWT.NONE);
      composite.setLayout(new GridLayout(1, false));
      GridData layoutData = new GridData(GridData.FILL_BOTH);
      composite.setLayoutData(layoutData);

      Label descDebugTarget = new Label(composite, SWT.WRAP);
      descDebugTarget.setText(DeployerUIMessages.getString("MainPreferencePage.debug.target.description"));//$NON-NLS-1$
      layoutData = new GridData(GridData.FILL_HORIZONTAL);
      descDebugTarget.setLayoutData(layoutData);

      Table debugTargetList = new Table(composite, SWT.BORDER | SWT.SINGLE
         | SWT.V_SCROLL);
      layoutData = new GridData(GridData.FILL_BOTH);
      debugTargetList.setLayoutData(layoutData);
      debugTargetList.setEnabled(false);

      this.debugViewer = new TableViewer(debugTargetList);
      this.debugViewer.setContentProvider(new ListContentProvider());
      this.debugViewer.setLabelProvider(new TargetLabelProvider());
      this.debugViewer.setSorter(new StringViewSorter());
      this.debugViewer.setInput(DeployerCorePlugin.getDefault().getDebugTargets());

      Label descUserTarget = new Label(composite, SWT.WRAP);
      descUserTarget.setText(DeployerUIMessages.getString("MainPreferencePage.user.target.description"));//$NON-NLS-1$
      layoutData = new GridData(GridData.FILL_HORIZONTAL);
      descUserTarget.setLayoutData(layoutData);

      Composite userComposite = new Composite(composite, SWT.NONE);
      GridLayout gridLayout = new GridLayout(2, false);
      gridLayout.marginHeight = 0;
      gridLayout.marginWidth = 0;
      userComposite.setLayout(gridLayout);
      layoutData = new GridData(GridData.FILL_BOTH);
      userComposite.setLayoutData(layoutData);

      Table targetList = new Table(userComposite, SWT.BORDER | SWT.SINGLE
         | SWT.V_SCROLL);
      layoutData = new GridData(GridData.FILL_BOTH);
      layoutData.grabExcessHorizontalSpace = true;
      targetList.setLayoutData(layoutData);

      this.viewer = new TableViewer(targetList);
      this.viewer.setContentProvider(new ListContentProvider());
      this.viewer.setLabelProvider(new TargetLabelProvider());
      this.viewer.setSorter(new StringViewSorter());
      this.viewer.setInput(DeployerCorePlugin.getDefault().getTargets());

      this.createButtons(userComposite);
      this.assign();

      return composite;
   }


   /** Description of the Method */
   protected void doAdd()
   {
      ITarget target = null;
      Collection choices = TargetUIAdapter.getInstance().getAvailableTargets();
      TargetChoiceDialog choiceDialog = new TargetChoiceDialog(AbstractPlugin.getShell(), choices);

      // Get the selected target and clone it
      if (choiceDialog.open() == IDialogConstants.OK_ID)
      {
         target = choiceDialog.getTarget().cloneTarget();
      }
      // Edit the newly created target
      if (target != null)
      {
         TargetEditDialog dialog = TargetUIAdapter.getInstance().getDialog(AbstractPlugin.getShell(), target);
         if (dialog.open() == IDialogConstants.OK_ID)
         {
            DeployerCorePlugin.getDefault().addTarget(target);
            this.viewer.refresh();
         }
      }
   }


   /** Description of the Method */
   protected void doEdit()
   {
      ITarget target = this.getCurrent();
      if (target != null)
      {
         TargetEditDialog dialog = TargetUIAdapter.getInstance().getDialog(AbstractPlugin.getShell(), target);
         if (dialog.open() == IDialogConstants.OK_ID)
         {
            this.viewer.refresh();
         }
      }
   }


   /** Description of the Method */
   protected void doRemove()
   {
      ITarget target = this.getCurrent();
      if (target != null)
      {
         DeployerCorePlugin.getDefault().removeTarget(target);
         this.viewer.refresh();
      }
   }


   /**
    * Gets the current attribute of the MainPreferencePage object
    *
    * @return   The current value
    */
   protected ITarget getCurrent()
   {
      ITarget current = null;
      IStructuredSelection selection = (IStructuredSelection) this.viewer.getSelection();
      if (!selection.isEmpty())
      {
         current = (ITarget) selection.getFirstElement();
      }
      return current;
   }
}
