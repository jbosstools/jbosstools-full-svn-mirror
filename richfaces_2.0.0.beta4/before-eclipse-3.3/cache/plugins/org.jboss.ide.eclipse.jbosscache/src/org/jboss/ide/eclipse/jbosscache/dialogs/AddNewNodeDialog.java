/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.dialogs;

import org.eclipse.jface.dialogs.Dialog;
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
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheInstance;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstance;
import org.jboss.ide.eclipse.jbosscache.model.internal.TreeCacheManager;
import org.jboss.ide.eclipse.jbosscache.views.config.TreeCacheView;

/**
 * When 
 * @author Gurkaner
 */
public class AddNewNodeDialog extends Dialog
{

   private Label lblFqnParentName;

   private Text txtFqnParentName;

   private Label lblFqnNodeName;

   private Text txtFqnNodeName;

   private String parentFqn;

   private static final String ADD_NEW_NODE = "Add New Node";

   private TreeCacheView cacheView;

   /**
    * Constructor
    * @param provider
    */
   public AddNewNodeDialog(Shell parentShell, ViewPart provider)
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

      lblFqnParentName = new Label(comp, SWT.NONE);
      lblFqnParentName.setText("Parent Fqn:"); //TODO Internaniotnalize
      txtFqnParentName = new Text(comp, SWT.BORDER);
      txtFqnParentName.setLayoutData(gridData);
      txtFqnParentName.setText(parentFqn);
      txtFqnParentName.setEnabled(false);

      lblFqnNodeName = new Label(comp, SWT.NONE);
      lblFqnNodeName.setText("Node Fqn:"); //TODO Internaniotnalize
      txtFqnNodeName = new Text(comp, SWT.BORDER);
      txtFqnNodeName.setLayoutData(gridData);
      if (parentFqn.equals(ICacheConstants.SEPERATOR))
         txtFqnNodeName.setText(parentFqn);
      else
         txtFqnNodeName.setText(parentFqn + ICacheConstants.SEPERATOR);

      return comp;
   }

   /**
    * Ok pressed
    */
   protected void okPressed()
   {
      String childName = txtFqnNodeName.getText();
      Object obj = cacheView.getSelection();
      TreeCacheManager manager = null;

      if (obj instanceof ICacheRootInstance)
         manager = ((ICacheRootInstance) obj).getTreeCacheManager();
      else if (obj instanceof ICacheInstance)
         manager = ((ICacheInstance) obj).getRootInstance().getTreeCacheManager();
      else
         return;

      try
      {
         TreeCacheManager.put_(manager, childName, null);
      }
      catch (Exception e)
      {
         //TODO Exception
      }
      super.okPressed();
   }

   public Text getTxtFqnNodeName()
   {
      return txtFqnNodeName;
   }

   public void setTxtFqnNodeName(Text txtFqnNodeName)
   {
      this.txtFqnNodeName = txtFqnNodeName;
   }

   public Text getTxtFqnParentName()
   {
      return txtFqnParentName;
   }

   public void setTxtFqnParentName(Text txtFqnParentName)
   {
      this.txtFqnParentName = txtFqnParentName;
   }

   public String getParentFqn()
   {
      return parentFqn;
   }

   public void setParentFqn(String parentFqn)
   {
      this.parentFqn = parentFqn;
   }

   protected void configureShell(Shell shell)
   {
      super.configureShell(shell);
      shell.setText(AddNewNodeDialog.ADD_NEW_NODE);
   }

}